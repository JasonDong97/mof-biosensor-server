package com.miqroera.biosensor.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miqroera.biosensor.domain.model.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 更新用户首次检测日期和累计检测次数
     * @param userId 用户 ID
     * @param measureDate 检测日期
     */
    void updateUserMeasureInfo(@Param("userId") Long userId, @Param("measureDate") java.time.LocalDate measureDate);
}
