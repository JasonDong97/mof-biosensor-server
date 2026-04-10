package com.miqroera.biosensor.domain.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.SysMinioFileMapper;
import com.miqroera.biosensor.domain.model.SysMinioFile;
import com.miqroera.biosensor.domain.service.SysMinioFileService;
import com.miqroera.biosensor.infra.config.MinIoConfig.MinIoProperties;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.util.FileUtils;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * MinIO 文件服务实现
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMinioFileServiceImpl extends ServiceImpl<SysMinioFileMapper, SysMinioFile> implements SysMinioFileService {

    private final MinioClient minioClient;
    private final MinIoProperties minIoProperties;

    @Override
    public Long insertFile(String minioFilePath, MultipartFile file) {
        SysMinioFile minioFile = new SysMinioFile();
        minioFile.setOriginalName(file.getOriginalFilename());
        minioFile.setSuffix(getSuffix(file.getOriginalFilename()));
        minioFile.setBucket(minIoProperties.getBucketName());
        minioFile.setObject(minioFilePath);
        minioFile.setObjectSize(file.getSize());
        // createBy 和 updateBy 暂时设为固定值（无用户关联）
        minioFile.setCreateBy("system");
        minioFile.setUpdateBy("system");
        save(minioFile);
        return minioFile.getId();
    }

    private String getSuffix(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public void download(String fileName, HttpServletResponse response) {
        try {

            GetObjectArgs args = buildGetObjectArgs(fileName);

            SysMinioFile one = this.lambdaQuery().eq(SysMinioFile::getBucket, args.bucket())
                    .eq(SysMinioFile::getObject, args.object())
                    .one();
            if (one == null) {
                throw new ServiceException("文件记录不存在");
            }

            try (InputStream in = minioClient.getObject(args)) {
                // 设置响应头
                FileUtils.setAttachmentResponseHeader(response, one.getOriginalName());
                IoUtil.copy(in, response.getOutputStream());
            }
        } catch (Exception e) {
            log.error("下载文件失败: {}", fileName, e);
            throw new ServiceException("下载文件失败");
        }
    }

    private GetObjectArgs buildGetObjectArgs(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            throw new ServiceException("文件名不能为空");
        }
        int i = fileName.indexOf("/");
        String bucket = fileName.substring(0, i);
        String object = fileName.substring(i + 1);
        return GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build();
    }

    @Override
    public Object upload(String path, MultipartFile file, String[] defaultAllowedExtension) {
        return null;
    }

    @Override
    public void downloadById(Long id, HttpServletResponse response) {
        SysMinioFile file = requireById(id);
        download(file.getObject(), response);
    }

    @Override
    public SysMinioFile requireById(Long id) {
        SysMinioFile file = getById(id);
        if (file == null) {
            throw new ServiceException("文件记录不存在");
        }
        return file;
    }

    @Override
    public java.util.List<Object> uploadBatch(String path, MultipartFile[] files) {
        return null;
    }
}
