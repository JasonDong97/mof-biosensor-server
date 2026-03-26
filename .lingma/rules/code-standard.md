---
trigger: model_decision
description: 1. **新功能开发**：参考命名规范和代码格式	2. **代码审查**：检查是否符合各层规范	3. **问题排查**：检查导入、注释、事务等配置
---

# 项目编码规范

## 使用场景
1. **新功能开发**：参考命名规范和代码格式
2. **代码审查**：检查是否符合各层规范
3. **问题排查**：检查导入、注释、事务等配置

## 命名规范速查

### 类命名

| 类型         | 命名规则                             | 示例                                    |
| ------------ | ------------------------------------ | --------------------------------------- |
| Controller   | `{功能}Controller`                   | `LoginController`、`ProjectController`  |
| Service 接口  | `I{功能}Service`                     | `IProjectService`、`IUserService`       |
| Service 实现  | `{功能}ServiceImpl`                  | `ProjectServiceImpl`、`UserServiceImpl` |
| Mapper 接口   | `{实体}Mapper`                       | `ProjectMapper`、`UserMapper`           |
| Mapper XML   | `{实体}Mapper.xml`                   | `ProjectMapper.xml`                     |
| Entity/Model | `{实体名}`                           | `Project`、`SysUser`                    |
| DTO          | `{功能}{DTO/AddDTO/UpdateDTO}`       | `ProjectAddDTO`、`UserUpdateDTO`        |
| VO           | `{功能}VO/{功能}ListVO/{功能}PageVO` | `ProjectVO`、`ProjectListVO`            |
| Query        | `{功能}Query/{功能}PageQuery`        | `ProjectQuery`、`ProjectPageQuery`      |
| Enum         | `{功能}Status/{类型}Enum`            | `ProjectStatus`、`TaskStatus`           |
| Constants    | `{功能}Constants`                    | `CacheConstants`、`DataPermissions`     |

### 方法命名

| 类型         | 命名规则                      | 示例                              |
| ------------ | ----------------------------- | --------------------------------- |
| 查询单个     | `get{名称}By{条件}`           | `getProjectById`、`getUserByName` |
| 查询列表     | `list{名称}`                  | `listProjects`、`listUsers`       |
| 分页查询     | `page{名称}`                  | `pageProjects`                    |
| 新增         | `add{名称}` / `save{名称}`    | `addProject`、`saveUser`          |
| 更新         | `update{名称}`                | `updateProject`                   |
| 删除         | `delete{名称}`                | `deleteProject`                   |
| 计数         | `count{名称}`                 | `countProjects`                   |
| 判断是否存在 | `exist{名称}` / `check{名称}` | `existProject`、`checkPermission` |
| 获取必须存在 | `require{名称}`               | `requireById`                     |

### 变量命名

| 类型       | 命名规则           | 示例                                             |
| ---------- | ------------------ | ------------------------------------------------ |
| 成员变量   | `camelCase`        | `private String projectName;`                    |
| 局部变量   | `camelCase`        | `String userName = "admin";`                     |
| 常量       | `UPPER_SNAKE_CASE` | `private static final String CACHE_KEY = "xxx";` |
| 枚举值     | `LOWER_SNAKE_CASE` | `planning`、`in_progress`                        |
| DTO/VO 字段 | `camelCase`        | `private String projectTitle;`                   |
| 数据库字段 | `snake_case`       | `project_no`、`create_time`                      |

## 代码格式要点

### 导入语句规范

**顺序要求**（各类别之间使用空行分隔）：
1. 标准 Java 类库导入（`java.` 开头）
2. 第三方库导入（`javax.`、`cn.`、`com.` 等）
3. 项目内部类导入（`com.era.miqroproject.` 开头）
4. 同类别内的导入按类名字母顺序排列

**强制要求**：
- 所有外部引用的类**必须**通过显式的 `import` 语句导入
- **禁止**在方法上使用全包名（fully qualified name）
- 定期检查并移除未使用的导入

### 注释规范

**文件头注释**：
```java
/**
 * <p>
 * 项目
 * </p>
 *
 * @author dongjingxiang
 * @since 2025-09-03
 */
```

**方法注释**：
```java
/**
 * 分页查询
 *
 * @param page           分页参数
 * @param param          查询参数
 * @param containsPublic 是否包含公开项目
 * @return 分页结果
 */
```

## 各层规范要点

### Controller 层

**基本结构**：
```java
@Api(tags = "登录登出")
@ApiSupport(order = 10001)
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final SysLoginService loginService;
    
    @SaIgnore
    @PostMapping("/login")
    @ApiOperation("登录")
    public R<TokenVO> login(@Validated @RequestBody LoginBody loginBody) {
        TokenVO token = loginService.login(username, password, code);
        return R.ok(token);
    }
}
```

**响应规范**：
```java
R.ok()                          // 无返回
R.ok(data)                      // 带数据返回
R.ok(data, "自定义消息")         // 带数据和消息
R.fail("错误消息")               // 失败响应
```

### Service 层

**接口规范**：
```java
public interface IProjectService extends IService<Project> {
    Long addProject(LoginUser loginUser, ProjectAddDTO dto);
    ProjectVO getProjectById(LoginUser loginUser, Long id);
    void updateByDTO(LoginUser loginUser, ProjectUpdateDTO dto);
}
```

**实现规范**：
```java
@Slf4j
@CacheConfig(cacheNames = CacheConstants.PROJECT_CACHE_NAME)
@RequiredArgsConstructor(onConstructor_ = @Lazy)
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> 
    implements IProjectService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addProject(LoginUser loginUser, ProjectAddDTO dto) {
        // 业务逻辑
        return projectId;
    }
}
```

### Model/Entity 层

**Entity 规范**：
```java
@Data
@ApiModel(value = "Project 对象", description = "项目")
public class Project implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "项目名称", position = 20)
    private String title;
}
```

**DTO 校验规范**：
```java
@Data
public class ProjectAddDTO implements Serializable {
    @NotBlank(message = "项目名称不能为空")
    private String title;
    
    @NotNull(message = "项目状态不能为空")
    private ProjectStatus projectStatus;
}
```

### Mapper 层

**接口规范**：
```java
public interface ProjectMapper extends BaseMapper<Project> {
    IPage<ProjectListVO> selectPage2(@Param("page") Page<Object> page,
                                     @Param("param") ProjectListQuery param,
                                     @Param("containsPublic") boolean containsPublic);
}
```

**XML 规范**：
```xml
<select id="selectPage2" resultType="com.era.miqroproject.domain.model.vo.ProjectListVO">
    select distinct t.* from (<include refid="projectListVOSql"/>) t
    <where>
        t.del_flag = 0
        <if test="param.title != null and param.title != ''">
            AND t.title LIKE CONCAT('%', #{param.title}, '%')
        </if>
    </where>
</select>
```

## 重要规范提醒

### 事务规范
- 默认使用 `@Transactional(rollbackFor = Exception.class)`
- 了解不同传播行为：`REQUIRED`（默认）、`REQUIRES_NEW`、`NOT_SUPPORTED`

### 日志规范
```java
@Slf4j
public class SomeService {
    public void someMethod() {
        log.info("项目信息：{}", projectId);  // 使用占位符
        log.error("错误信息：{}", errorMsg, exception);
    }
}
```

### 异常处理
```java
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
```

### 编译验证
**重要**：完成任何 Java 开发任务后，必须执行：
```bash
mvn clean compile 
```

确保看到 `BUILD SUCCESS`。