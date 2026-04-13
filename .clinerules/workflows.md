# 开发工作流

## 需求分析与任务拆解

1. 拆解用户提供的需求，对不清晰的需求提出疑问
2. 确定需求，进行任务规划
3. 询问用户是否需要执行，**强制禁止**用户没有明确需要执行时调用工具执行

## 新增功能开发流程

**原因**: 规范开发步骤，确保代码质量和一致性。

1. 创建数据库表 (SQL 脚本)
2. 生成 Entity 实体类
3. 创建 Mapper 接口
4. 实现 Service 层
5. 添加 Controller 接口
6. 编译检查是否有语法错误

**示例 - 新增用户模块**:
```sql
-- 1. 创建表
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    deleted TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```
```java
// 2. 生成 Entity (domain/entity/User.java)
@Entity
public class User {
    private Long id;
    private String name;
    private String password;
}

// 3. Mapper (infra/mapper/UserMapper.java)
public interface UserMapper extends BaseMapper<User> {}

// 4. Service (domain/service/IUserService.java)
public interface IUserService {
    R<UserVO> getUserById(Long id);
}

// 5. Controller (web/UserController.java)
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;
    
    @GetMapping("/{id}")
    public R<UserVO> getById(@PathVariable Long id) {
        return R.ok(userService.getUserById(id));
    }
}
```

## 修改现有功能流程

1. 定位相关代码 (使用搜索工具)
2. 查看调用链
3. 评估影响范围
4. 实施修改并测试

## 调试技巧

**原因**: 帮助快速定位和解决问题。

- 日志文件：`logs/sys-info.log` / `logs/sys-error.log`
- 远程调试：IDEA 配置 Remote JVM Debug (端口 5005)
- 数据库日志：开启 MyBatis SQL 输出

## 编译与打包

```bash
# 编译
mvn compile -q

# 清理并打包
mvn clean package -DskipTests
```
