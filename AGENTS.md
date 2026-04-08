# AGENTS.md - AI Agent Configuration

## 1. 项目概述

MiQroBreath 是一套配合智能呼气检测硬件的微信小程序及云平台系统，实现蓝牙设备连接、实时健康检测、数据云端同步及运营数据分析的全链路健康管理解决方案。

该项目为 **MiQroBreath 的后端部分**：

**采用的技术栈如下**:

- Java 17+ / Maven 单体项目
- Spring Boot + MyBatis Plus
- MySQL + Redis

## 2. 代码规范

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
- **异常处理**: 使用 `ServiceException.of(format, args)` 封装业务异常
- **集合操作**: 必须进行空值检查，避免 NPE
- **避免重复代码**: 相同或相似逻辑必须抽象出公共方法调用
- **方法长度**: 方法体内容不应过长，建议控制在 80-100 行之间，方法内的步骤需抽象出私有方法调用，且方法名要简单明了

### 目录约定

- `/src/main/java/com/miqroera/biosensor/web` - Controller 和入口
- `/src/main/java/com/era/miqroproject/domain` - 领域模型和业务逻辑
- `/src/main/java/com/era/miqroproject/infra` - 基础设施
- `/src/main/resources/mapper` - MyBatis XML 映射文件

## 3. 重要规则

### 关键约束:

- Controller 不直接访问数据库
  - **响应格式**: 统一使用 `R.ok(data)` / `R.fail(message)`
  - **分页响应类**: 统一使用 `R<PageResult<T>>`
- Service 层处理所有业务逻辑
  - **分页数据封装**：`PageResult.build(page, clazz)` , `page` 为 MyBatis Plus 分页对象，`clazz` 是需要转换的泛型类
- 统一使用 MyBatis Plus 的 BaseMapper
- 所有 IO 操作必须异步或使用连接池

### 强制约束

- **复杂 SQL 必须使用 XML 方式**：禁止在 Service 层使用 MyBatis Plus 的 `LambdaUpdateWrapper.setSql()` 等原生 SQL 方法
- **XML 文件位置**: `src/main/resources/mapper/{MapperName}.xml`
- **实现步骤**:
  1. 在 Mapper 接口中定义方法，参数使用 `@Param` 注解
  2. 在对应的 XML 文件中编写 `<update>`/`<select>`/`<insert>`/`<delete>` 标签
  3. Service 层通过调用 Mapper 方法执行 SQL

### 安全规范

- **禁止 SQL 注入**: 所有查询必须使用参数化
- **敏感数据**: 密码必须加密存储 (BCrypt)
- **API 密钥**: 使用环境变量，禁止硬编码

### 否定约束

- **不要**使用 `any` 类型的 Java 等价物 (如裸 Object)
- **不要**在 Controller 层写业务逻辑
- **不要**直接修改 `target/` 目录下的文件
- **不要**提交 `.env` 或包含密码的配置文件
- **不要**使用过时的 API (参考废弃标记)

### 推荐做法

- 优先使用 Stream API 处理集合
- 使用 Optional 避免 null 检查
- 遵循单一职责原则 (每个方法不超过 50 行)
- 注释使用英文或中文 (保持统一)

## 4. 常见任务

### 需求分析与任务拆解

1. 拆解用户提供的需求，对不清晰的需求提出疑问
2. 确定需求，进行任务规划
3. 询问用户是否需要执行，**强制禁止**用户没有明确需要执行时调用工具执行

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

## 5. 编译与打包

**常用命令**

```bash
# 编译
mvn compile -q

# 清理并打包
mvn clean package -DskipTests
```

## 6. 其他

参考文件

- `misc/docs/` - 项目文档 (产品需求文档、接口开发进度等)
- `misc/sql/` - SQL 脚本备份
