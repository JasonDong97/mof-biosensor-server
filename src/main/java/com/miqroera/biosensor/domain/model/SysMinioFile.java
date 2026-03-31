package com.miqroera.biosensor.domain.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * MinIO 文件记录
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@TableName(value = "sys_minio_file")
@Data
public class SysMinioFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片原始文件名称
     */
    private String originalName;

    /**
     * 后缀名称
     */
    private String suffix;

    /**
     * 桶名称
     */
    private String bucket;

    /**
     * 存储地址
     */
    private String object;

    /**
     * 文件大小
     */
    private Long objectSize;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}