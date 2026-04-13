# 编码规范

## 命名规范

**原因**: 统一的命名规范提高代码可读性，团队成员能快速理解代码意图。

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | `ResourceCalendarController` |
| 方法/变量 | camelCase | `getUserById` |
| 常量 | UPPER_SNAKE_CASE | `DEFAULT_PAGE_SIZE` |
| 数据库表 | `snake_case` 前缀 `t_` | `t_resource_calendar` |

**示例** (来自 `src/main/java/com/miqroera/biosensor/web/RecordController.java`):
```java
// ✅ 正确
public class RecordController
private int pageSize
private static final int DEFAULT_PAGE_SIZE = 20

// ❌ 错误
public class record_controller
private int page-size
private int defaultpagesize
```

## 代码风格

### 响应格式

**原因**: 统一响应格式让前端能统一处理，降低联调成本。

```java
// ✅ 正确
return R.ok(data);
return R.fail("用户不存在");

// ❌ 错误
return data;
return "error";
```

### 日志级别

**原因**: 正确的日志级别便于问题排查和日志监控。

```java
// 错误 - 需要记录堆栈信息用于排查
log.error("数据库连接失败", e);

// 信息 - 需要记录业务操作
log.info("用户 {} 登录成功", username);

// 调试 - 仅开发环境需要
log.debug("查询参数: {}", params);
```

### 异常处理

**原因**: 使用统一异常封装便于前端统一处理和日志记录。

```java
// ✅ 正确 (参考 src/main/java/com/era/miqroproject/common/ServiceException.java)
throw ServiceException.of("用户 {} 不存在", userId);

// ❌ 错误
throw new RuntimeException("用户不存在");
```

### 集合操作

**原因**: 避免 NPE (NullPointerException) 导致系统崩溃。

```java
// ✅ 正确
List<User> users = getUsers();
if (users != null && !users.isEmpty()) {
    // 处理逻辑
}

// 或使用 Optional
Optional.ofNullable(getUsers())
    .orElse(Collections.emptyList())
    .forEach(this::process);

// ❌ 错误
List<User> users = getUsers();
users.forEach(this::process); // 可能 NPE
```

## 方法长度

**原因**: 短方法更易于测试、复用和维护。

- 方法体内容建议控制在 **50 行以内**
- 复杂逻辑拆分为私有方法
- 方法名要简单明了，体现业务意图

**示例**:
```java
// ✅ 正确 - 逻辑清晰，职责单一
public void processOrder(Long orderId) {
    validateOrder(orderId);
    calculatePrice(orderId);
    sendNotification(orderId);
}

private void validateOrder(Long orderId) {
    // 验证逻辑
}

// ❌ 错误 - 方法过长，职责不清
public void processOrder(Long orderId) {
    // 50+ 行代码，包含验证、计算、通知等所有逻辑
}
```

## 推荐做法

- **Stream API**: 处理集合时优先使用，提高代码简洁性
  ```java
  List<String> names = users.stream()
      .map(User::getName)
      .collect(Collectors.toList());
  ```
- **Optional**: 避免 null 检查
  ```java
  String result = Optional.ofNullable(map.get("key"))
      .orElse("default");
  ```
- **注释**: 使用英文或中文，**保持统一**
