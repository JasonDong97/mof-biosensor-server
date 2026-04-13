# 数据库规范

## MyBatis Plus 使用规范

### 基础规范

- 统一使用 MyBatis Plus 的 BaseMapper

### 复杂 SQL 规范

**复杂 SQL 必须使用 XML 方式**：

**原因**: XML 方式便于复杂 SQL 的维护、版本管理和 SQL 审核，禁止在 Service 层使用 MyBatis Plus 的 `LambdaUpdateWrapper.setSql()` 等原生 SQL 方法。

**XML 文件位置**: `src/main/resources/mapper/{MapperName}.xml`

### 实现步骤

1. 在 Mapper 接口中定义方法，参数使用 `@Param` 注解
2. 在对应的 XML 文件中编写 `<update>`/`<select>`/`<insert>`/`<delete>` 标签
3. Service 层通过调用 Mapper 方法执行 SQL

### XML 示例

**Mapper 接口** (`infra/mapper/RecordMapper.java`):
```java
public interface RecordMapper extends BaseMapper<Record> {
    
    /**
     * 复杂查询 - 使用 XML
     * @param userId 用户ID
     * @param startDate 开始日期
     * @return 记录列表
     */
    List<RecordVO> selectComplexByCondition(@Param("userId") Long userId,
                                             @Param("startDate") LocalDateTime startDate);
}
```

**XML 文件** (`resources/mapper/RecordMapper.xml`):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.miqroera.biosensor.infra.mapper.RecordMapper">
    
    <select id="selectComplexByCondition" resultType="com.miqroera.biosensor.domain.vo.RecordVO">
        SELECT r.*, u.name as user_name
        FROM t_record r
        LEFT JOIN t_user u ON r.user_id = u.id
        WHERE r.user_id = #{userId}
          AND r.create_time >= #{startDate}
          AND r.deleted = 0
        ORDER BY r.create_time DESC
    </select>
    
</mapper>
```

## 安全规范

**原因**: 防止 SQL 注入攻击和敏感数据泄露。

- **禁止 SQL 注入**: 所有查询必须使用参数化
- **敏感数据**: 密码必须加密存储 (BCrypt)
- **API 密钥**: 使用环境变量，禁止硬编码
