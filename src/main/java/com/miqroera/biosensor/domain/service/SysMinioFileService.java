package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.SysMinioFile;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * MinIO 文件服务接口
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
public interface SysMinioFileService extends IService<SysMinioFile> {

    /**
     * 插入文件到 MinIO 并保存记录
     *
     * @param minioFilePath MinIO 文件路径
     * @param file          上传的文件
     * @return 文件记录 ID
     */
    Long insertFile(String minioFilePath, MultipartFile file);

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param response HTTP 响应对象
     */
    void download(String fileName, HttpServletResponse response);

    /**
     * 上传单个文件
     *
     * @param path                    上传路径
     * @param file                    上传的文件
     * @param defaultAllowedExtension 允许的文件扩展名数组
     * @return 文件上传结果 VO
     */
    Object upload(String path, MultipartFile file, String[] defaultAllowedExtension);


    /**
     * 根据 ID 下载文件
     *
     * @param id       文件 ID
     * @param response HTTP 响应对象
     */
    void downloadById(Long id, HttpServletResponse response);

    /**
     * 根据 ID 获取文件记录（不存在则抛出异常）
     *
     * @param id 文件 ID
     * @return 文件记录
     */
    SysMinioFile requireById(Long id);

    /**
     * 批量上传文件
     *
     * @param path  上传路径
     * @param files 文件数组
     * @return 上传结果列表
     */
    List<Object> uploadBatch(String path, MultipartFile[] files);
}