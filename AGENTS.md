# AGENTS.md - AI Agent Configuration

## 1. Project Overview (项目概览)

**项目名称**: MiQroBreath 
**项目类型**: Spring Boot 企业级后端系统  
**技术栈**: 
- Java 17+ / Maven 单体项目
- Spring Boot + MyBatis Plus
- MySQL + Redis

## 2. Architecture (系统架构)

### 分层架构规则
```
Controller 层 → Service 层 → Mapper 层
     ↓              ↓           ↓
   DTO          Entity       SQL
```

**关键约束**:
- Controller 不直接访问数据库
  - **响应格式**: 统一使用 `R.ok(data)` / `R.fail(message)`
  - **分页响应类**: 统一使用 `R<PageResult<T>>`
- Service 层处理所有业务逻辑
  - **分页数据封装**：`PageResult.build(page, clazz)` , `page` 为 MyBatis Plus 分页对象，`clazz` 是需要转换的泛型类 
- 统一使用 MyBatis Plus 的 BaseMapper
- 所有 IO 操作必须异步或使用连接池

### 目录约定
- `/src/main/java/com/miqroera/biosensor/api` - Controller 和入口
- `/src/main/java/com/era/miqroproject/domain` - 领域模型和业务逻辑
- `/src/main/java/com/era/miqroproject/infra` - 基础设施
- `/src/main/resources/mapper` - MyBatis XML 映射文件

## 3. Coding Standards (编码规范)

### 命名规范
- **类名**: PascalCase (如 `ResourceCalendarController`)
- **方法/变量**: camelCase (如 `getUserById`)
- **常量**: UPPER_SNAKE_CASE (如 `DEFAULT_PAGE_SIZE`)
- **数据库表**: `snake_case` 前缀 `t_` (如 `t_resource_calendar`)

### 代码风格
- **响应格式**: 统一使用 `R.ok(data)` / `R.fail(message)`
- **日志级别**: 
  - 错误用 `log.error()` (带堆栈)
  - 信息用 `log.info()` (不带堆栈)
  - 调试用 `log.debug()`
- **异常处理**: 使用 `ServiceException` 封装业务异常
- **集合操作**: 必须进行空值检查，避免 NPE
- **避免重复代码**: 相同逻辑必须抽取为公共方法，禁止复制粘贴代码
- **方法长度**: 每个方法不超过 100 行，超出必须拆分

## 3. Build & Test (构建与测试)

### 常用命令
```bash

# 清理并打包
mvn clean package -DskipTests

# 代码检查
mvn checkstyle:check
```

### 数据库迁移
- SQL 脚本位置：`misc/sql/update-v{版本}.sql`
- 执行顺序：按版本号递增执行
- 禁止修改历史 SQL 文件

## 5. Common Tasks (常见任务)

### 新增功能开发流程
1. 创建数据库表 (SQL 脚本)
2. 生成 Entity 实体类
3. 创建 Mapper 接口
4. 实现 Service 层
5. 添加 Controller 接口
6. 编译检查是否有语法错误

### 修改现有功能
1. 定位相关代码 (使用搜索工具)
2. 查看调用链 (`search_symbol` with `called_by`)
3. 评估影响范围
4. 实施修改并测试

### 调试技巧
- 日志文件：`logs/sys-info.log` / `logs/sys-error.log`
- 远程调试：IDEA 配置 Remote JVM Debug (端口 5005)
- 数据库日志：开启 MyBatis SQL 输出

## 6. Important Rules (重要规则)

### ⚠️ 安全规范
- **禁止 SQL 注入**: 所有查询必须使用参数化
- **敏感数据**: 密码必须加密存储 (BCrypt)
- **API 密钥**: 使用环境变量，禁止硬编码

### ❌ 否定约束 (Negative Constraints)
- **不要**使用 `any` 类型的 Java 等价物 (如裸 Object)
- **不要**在 Controller 层写业务逻辑
- **不要**直接修改 `target/` 目录下的文件
- **不要**提交 `.env` 或包含密码的配置文件
- **不要**使用过时的 API (参考废弃标记)

### ✅ 推荐做法 (Best Practices)
- 优先使用 Stream API 处理集合
- 使用 Optional 避免 null 检查
- 遵循单一职责原则 (每个方法不超过 50 行)
- 注释使用英文或中文 (保持统一)

## 7. Workflow (工作流)

### 代码提交流程
```
1. 本地编译通过 (mvn compile)
2. 运行代码检查 (mvn checkstyle:check)
3. 提交信息遵循 Conventional Commits
   - feat: 新功能
   - fix: bug 修复
   - docs: 文档更新
   - refactor: 重构
   - test: 测试相关
```

### 代码审查清单
- [ ] 代码符合编码规范
- [ ] 没有引入新的警告
- [ ] 数据库操作已优化 (无 N+1 查询)
- [ ] 日志记录完整 (包含关键信息)

## 8. Common Pitfalls (常见踩坑)

### 数据库操作
- 批量操作时必须分页 (每次不超过 1000 条)
- 事务注解 `@Transactional` 只能用于 public 方法
- 注意 MyBatis 缓存可能导致的数据不一致

### 集合处理
- 从数据库查询结果必须先判空再使用
- 使用 `CollectionUtils.isEmpty()` 而非 `== null`
- 并发场景使用线程安全的集合类

### 环境变量
- 修改 `.env` 后必须重启应用
- 开发环境和生产环境配置隔离
- 敏感配置使用加密

## 9. AGENTS.md Maintenance (AGENTS.md 维护)

### 🔄 自动更新规则
**AI Agent 应定期审查和更新本文件**，以确保配置信息始终与项目实际状态保持一致：

1. **触发更新场景**:
   - 项目架构发生重大变化时
   - 引入新技术栈或框架时
   - 发现现有规则需要调整时
   - 用户明确要求修改配置时

2. **更新流程**:
   - 分析当前项目结构和代码
   - 识别需要更新的配置项
   - 使用 `replace_in_file` 工具进行精准修改
   - 保持文件格式和风格一致

3. **审查清单**:
   - [ ] 技术栈信息是否最新
   - [ ] 目录约定是否准确
   - [ ] 编码规范是否完整
   - [ ] 常用命令是否可用
   - [ ] 重要规则是否覆盖全面

### 📝 版本记录
| 版本 | 日期 | 更新内容 |
|------|------|----------|
| v0.0.1 | 2026-03-31 | 初始版本 |

---

## 10. Related Files (相关文件)

### 核心配置文件
- `pom.xml` - Maven 项目配置
- `.env` - 环境变量
- `application.yml` - Spring Boot 配置
- `mybatis-config.xml` - MyBatis 配置

### 规则文件 (Rules)
- `.lingma/rules/code-standard.md` - 代码编写规范

### 文档资源
- `misc/docs/` - 项目文档 (数据库设计、API 文档等)
- `README.md` - 项目说明
- `logs/` - 运行日志

