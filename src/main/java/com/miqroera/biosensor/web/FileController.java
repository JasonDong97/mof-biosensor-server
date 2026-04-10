package com.miqroera.biosensor.web;

import cn.dev33.satoken.annotation.SaIgnore;
import com.miqroera.biosensor.domain.model.vo.FileUploadVO;
import com.miqroera.biosensor.domain.service.SysMinioFileService;
import com.miqroera.biosensor.infra.config.MinIoConfig.MinIoProperties;
import com.miqroera.biosensor.infra.domain.model.R;
import com.miqroera.biosensor.infra.util.MinIoUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * 文件控制器
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Slf4j
@Tag(name = "文件", description = "文件上传下载")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/files")
public class FileController {

    private final MinioClient minioClient;
    private final MinIoProperties minIoProperties;
    private final SysMinioFileService sysMinioFileService;

    /**
     * 上传文件
     */
    @SaIgnore
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件到 MinIO，无需鉴权")
    public R<FileUploadVO> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String suffix = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String object = "files/" + UUID.randomUUID().toString().replace("-", "") + suffix;

            // 上传到 MinIO
            try (InputStream in = file.getInputStream()) {
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(minIoProperties.getBucketName())
                        .object(object)
                        .stream(in, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build();
                minioClient.putObject(args);
            }

            // 构建访问 URL
            String url = MinIoUtil.getPath(minIoProperties.getBucketName(), object);

            // 保存文件记录到数据库
            Long fileId = sysMinioFileService.insertFile(object, file);

            FileUploadVO vo = new FileUploadVO();
            vo.setId(fileId);
            vo.setUrl(url);

            log.info("文件上传成功: id={}, object={}, 大小={} bytes", fileId, object, file.getSize());
            return R.ok(vo);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return R.fail("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    @SaIgnore
    @GetMapping("/download")
    @Operation(summary = "下载文件", description = "从 MinIO 下载文件，无需鉴权")
    public void download(String fileName, HttpServletResponse response) {
        log.info("下载请求，fileName: {}", fileName);
        sysMinioFileService.download(fileName, response);
    }
}
