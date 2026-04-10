package com.miqroera.biosensor.domain.model.vo;

import lombok.Data;

/**
 * 文件上传返回 VO
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Data
public class FileUploadVO {

    /**
     * 文件记录 ID
     */
    private Long id;

    /**
     * 文件访问路径
     */
    private String url;
}
