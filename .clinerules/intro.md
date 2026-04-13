# 项目介绍

## 项目概述

MiQroBreath 是一套配合智能呼气检测硬件的微信小程序及云平台系统，实现蓝牙设备连接、实时健康检测、数据云端同步及运营数据分析的全链路健康管理解决方案。

该项目为 **MiQroBreath 的后端部分**。

## 技术栈

- Java 17+ / Maven 单体项目
- Spring Boot 3.x + MyBatis Plus 3.5+
- MySQL 8.0+ / Redis 7.x

## 目录结构

```
src/main/java/com/miqroera/biosensor/
├── web/           # Controller 层，接收 HTTP 请求
├── domain/        # 领域模型和业务逻辑
│   ├── entity/    # 数据库实体
│   ├── service/   # 服务接口和实现
│   └── vo/        # 视图对象
├── infra/         # 基础设施
│   ├── config/    # 配置类
│   ├── mapper/    # MyBatis Mapper
│   └── common/    # 公共工具类
src/main/resources/
├── mapper/        # MyBatis XML 映射文件
└── application.yml # 应用配置
```

## 代码示例参考

| 场景 | 参考文件 |
|------|----------|
| Controller 规范 | `src/main/java/com/miqroera/biosensor/web/RecordController.java` |
| Service 规范 | `src/main/java/com/miqroera/biosensor/domain/service/impl/RecordServiceImpl.java` |
| 分页响应 | `src/main/java/com/era/miqroproject/common/R.java` |
| 异常处理 | `src/main/java/com/era/miqroproject/common/ServiceException.java` |

## 参考文件

- `misc/docs/` - 项目文档 (产品需求文档、接口开发进度等)
- `misc/sql/` - SQL 脚本备份
