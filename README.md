# MiQroBreath Backend

智能呼气检测仪后端系统 - 为微信小程序提供设备管理、检测数据记录及健康分析服务

---

## 技术栈

- **JDK**: 17+
- **框架**: Spring Boot 3.2.3
- **ORM**: MyBatis Plus 3.5.15
- **数据库**: MySQL 8.0+ / Redis 6.0+
- **权限**: Sa-Token 1.45.0
- **API 文档**: Knife4j 4.4.0

---

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 2. 数据库初始化

```bash
# 创建数据库
CREATE DATABASE biosensor DEFAULT CHARACTER SET utf8mb4;

# 导入表结构
use biosensor;
source misc/sql/biosensor-v0.0.1.sql;
```

### 3. 配置修改

编辑 `src/main/resources/config/application-dev.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/biosensor?serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
      database: 4
```

### 4. 编译运行

```bash
# 编译
mvn clean package -DskipTests

# 运行
mvn spring-boot:run
# 或 java -jar target/mof-based-biosensor-0.0.1.jar
```

### 5. 访问

- **服务地址**: http://localhost:9093/api
- **API 文档**: http://localhost:9093/api/doc.html

---

## 项目结构

```
src/main/java/com/miqroera/biosensor/
├── domain/          # 领域模型层
│   ├── mapper/      # MyBatis Mapper
│   ├── model/       # 实体类 (Entity/DTO/VO)
│   └── service/     # 业务逻辑层
├── infra/           # 基础设施层
│   ├── config/      # 配置类
│   ├── domain/      # 通用模型 (R 响应类)
│   └── util/        # 工具类
└── web/             # Controller 层
    ├── AuthController.java      # 认证接口
    ├── DeviceController.java    # 设备接口
    ├── RecordController.java    # 记录接口
    └── FeedbackController.java  # 反馈接口
```

---

## API 接口

| 模块 | 前缀 | 说明 |
|------|------|------|
| 认证 | `/api/v1/auth` | 登录、登出、Token 刷新 |
| 用户 | `/api/v1/user` | 用户信息查询更新 |
| 设备 | `/api/v1/devices` | 设备绑定解绑查询 |
| 记录 | `/api/v1/records` | 检测记录上报查询 |
| 反馈 | `/api/v1/feedbacks` | 用户反馈提交查询 |

### 接口示例

**微信小程序登录**
```http
POST /api/v1/auth/login/wx
{
  "code": "微信登录 code"
}
```

**设备绑定**
```http
POST /api/v1/devices/bind
Authorization: Bearer {accessToken}
{
  "deviceSn": "SN20260403001"
}
```

**检测记录上报**
```http
POST /api/v1/records
Authorization: Bearer {accessToken}
{
  "recordId": "uuid",
  "deviceSn": "SN20260403001",
  "concentration": 125.5,
  "level": 2,
  "sceneType": 1
}
```

详细文档：http://localhost:9093/api/doc.html

---

## 数据库表

| 表名 | 说明 |
|------|------|
| sys_user | 用户信息表 |
| t_device | 设备信息表 |
| t_user_device | 用户设备绑定关系 |
| t_record | 检测记录表 |
| t_feedback | 用户反馈表 |
| sys_config | 系统配置表 |

---

## 配置说明

**应用端口**: 9093  
**Context Path**: /api  
**日志目录**: ./logs  
**日志保留**: 1 年

配置文件:
- `application.yaml` - 主配置
- `application-dev.yaml` - 开发环境
- `application-knife4j.yaml` - API 文档
- `logback-spring.xml` - 日志配置

---

## 常用命令

```bash
# 清理打包
mvn clean package -DskipTests

# 编译
mvn compile

# 运行测试
mvn test

# 代码检查
mvn checkstyle:check
```

---

## 常见问题

**端口被占用**
```bash
# Windows
netstat -ano | findstr :9093
taskkill /F /PID <PID>
```

**数据库连接失败**  
检查 MySQL 服务、配置、防火墙

**Redis 连接失败**  
检查 Redis 服务、密码配置

**Token 失效**  
调用 `/api/v1/auth/refresh` 刷新 Token

---

## 开发规范

- **类名**: PascalCase (如 `DeviceController`)
- **方法/变量**: camelCase (如 `getUserById`)
- **响应格式**: `R.ok(data)` / `R.fail(message)`
- **日志**: `log.error()` / `log.info()` / `log.debug()`
- **异常**: 使用 `ServiceException`

---

## 更新日志

### v0.0.1 (2026-03-31)

- ✅ 用户认证（微信登录、手机号登录）
- ✅ 设备管理（绑定、解绑、查询）
- ✅ 检测记录（单条上报、批量上报、分页查询）
- ✅ 用户反馈功能
- ✅ API 文档集成

---

## 联系方式

- **团队**: MiQroEra 研发团队
- **邮箱**: support@miqroera.com

---

<div align="center">

Made with ❤️ by MiQroEra Team

</div>
