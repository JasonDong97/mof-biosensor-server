# 架构规范

## 目录约定

**原因**: 统一的目录结构让团队成员能快速定位代码，降低学习成本。

| 目录 | 用途 |
|------|------|
| `/src/main/java/com/miqroera/biosensor/web` | Controller 和入口 |
| `/src/main/java/com/era/miqroproject/domain` | 领域模型和业务逻辑 |
| `/src/main/java/com/era/miqroproject/infra` | 基础设施 |
| `/src/main/resources/mapper` | MyBatis XML 映射文件 |

## 分层架构约束

### Controller 层

**职责**: 接收 HTTP 请求，参数校验，调用 Service，返回响应。

**原因**: Controller 专注于请求处理，业务逻辑下沉到 Service 层，便于测试和复用。

- **不直接访问数据库**
- **响应格式**: 统一使用 `R.ok(data)` / `R.fail(message)`
- **分页响应类**: 统一使用 `R<PageResult<T>>`

**示例** (参考 `src/main/java/com/miqroera/biosensor/web/RecordController.java`):
```java
@RestController
@RequestMapping("/api/records")
public class RecordController {
    
    private final IRecordService recordService;
    
    @GetMapping
    public R<PageResult<RecordVO>> list(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int pageSize) {
        return R.ok(recordService.pageRecords(page, pageSize));
    }
}
```

### Service 层

**职责**: 处理所有业务逻辑，包括事务管理。

**原因**: 业务逻辑集中管理，便于复用和单元测试。

- **分页数据封装**: 使用 `PageResult.build(page, clazz)`

**示例** (参考 `src/main/java/com/miqroera/biosensor/domain/service/impl/RecordServiceImpl.java`):
```java
@Service
public class RecordServiceImpl implements IRecordService {
    
    public R<PageResult<RecordVO>> pageRecords(int page, int pageSize) {
        Page<Record> recordPage = new Page<>(page, pageSize);
        Page<Record> result = recordMapper.selectPage(recordPage, queryWrapper);
        return R.ok(PageResult.build(result, RecordVO.class));
    }
}
```

## 其他约束

**原因**: 统一技术选型，减少团队内部的技术栈碎片化。

- 统一使用 MyBatis Plus 的 BaseMapper
- 所有 IO 操作必须异步或使用连接池 (避免阻塞主线程)
