package com.miqroera.biosensor.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miqroera.biosensor.domain.model.SysMinioFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * MinIO 文件 Mapper
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Mapper
public interface SysMinioFileMapper extends BaseMapper<SysMinioFile> {
}
