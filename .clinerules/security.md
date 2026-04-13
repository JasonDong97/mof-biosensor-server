# 安全规范

## 密码安全

**原因**: 防止用户数据泄露后密码被直接利用，即使数据库被拖库也无法获取明文密码。

- 密码必须使用 BCrypt 加密存储
- 禁止明文保存任何用户密码
- 禁止在日志中打印用户密码

**示例**:
```java
// ✅ 正确 - 使用 BCrypt
String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

// ❌ 错误 - 明文存储
user.setPassword(rawPassword);
```

## API 密钥

**原因**: API 密钥泄露会导致未授权访问和数据安全风险，必须与环境隔离。

- 使用环境变量读取密钥，禁止硬编码在代码中
- 切勿将密钥提交到版本控制系统
- 密钥轮换时及时更新环境变量

**示例**:
```java
// ✅ 正确 - 使用环境变量
String apiKey = System.getenv("THIRD_PARTY_API_KEY");

// ❌ 错误 - 硬编码
String apiKey = "sk-xxxxxx";
```

## SQL 安全

**原因**: 防止 SQL 注入攻击，这是 Web 安全中最常见且危害严重的攻击手段。

- 禁止 SQL 注入：所有查询必须使用参数化
- MyBatis 中使用 `#{}` 而非 `${}`

**示例**:
```java
// ✅ 正确 - 参数化查询
@Select("SELECT * FROM t_user WHERE id = #{userId}")
User findById(@Param("userId") Long userId);

// ❌ 错误 - 字符串拼接，存在 SQL 注入风险
@Select("SELECT * FROM t_user WHERE id = " + userId)
```

## 配置安全

**原因**: 敏感配置文件泄露可能导致生产环境被攻击。

- **不要**提交 `.env` 或包含密码的配置文件到版本控制
- 使用 `.gitignore` 排除敏感文件
- 生产环境配置通过环境变量或配置中心注入

**示例** (.gitignore):
```
# 环境配置
.env
*.local.yml
application-prod.yml
```
