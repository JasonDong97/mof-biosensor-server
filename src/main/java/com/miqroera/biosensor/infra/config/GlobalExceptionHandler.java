package com.miqroera.biosensor.infra.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.domain.model.R;
import com.miqroera.biosensor.infra.util.SecurityUtils;
import com.miqroera.biosensor.infra.util.ServletUtils;
import com.miqroera.biosensor.infra.util.StreamUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * <p>
 * 统一处理系统中抛出的各类异常，返回标准化的错误响应
 * </p>
 *
 * @author ruoyi
 */
@Order
@RestControllerAdvice
@Slf4j(topic = "error_log")
public class GlobalExceptionHandler {

    // ==================== 常量定义 ====================

    /**
     * 系统错误码
     */
    private static final int SYSTEM_ERROR_CODE = 500;

    /**
     * JSON 内容类型
     */
    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 未知路径标识
     */
    private static final String UNKNOWN_PATH = "unknown";

    // ==================== 工具方法 ====================

    /**
     * 设置响应内容类型为 JSON
     */
    private static void setJsonContentType() {
        ServletUtils.getResponse().setContentType(JSON_CONTENT_TYPE);
    }

    /**
     * 构建异常响应对象（静态方法，供外部调用）
     *
     * @param code            错误码
     * @param e               异常对象
     * @param msg             错误消息
     * @param printStackTrace 是否打印堆栈
     * @return 统一响应对象
     */
    public static R<?> toR(int code, Throwable e, String msg, boolean printStackTrace) {
        HttpServletRequest request = ServletUtils.getRequest();
        String message = StrUtil.isEmpty(msg) ? e.getMessage() : msg;

        Object responseData;
        String stackTrace = null;
        if (printStackTrace) {
            ExceptionBO exceptionBO = ExceptionBO.of(e, message);
            responseData = exceptionBO;
            stackTrace = exceptionBO.getStackTrace();
        } else {
            responseData = ExceptionUtil.getMessage(e);
        }

        log.error("[{}][{}] {} - {}{}",
                SecurityUtils.getNickName(),
                request.getMethod(),
                request.getRequestURI(),
                message,
                StrUtil.isEmpty(stackTrace) ? StrUtil.EMPTY : "\n" + stackTrace);
        return R.build(code, message, responseData);
    }

    /**
     * 获取当前请求路径
     *
     * @return 请求 URI，获取失败返回 "unknown"
     */
    private static String getRequestPath() {
        try {
            return ServletUtils.getRequest().getRequestURI();
        } catch (Exception e) {
            return UNKNOWN_PATH;
        }
    }

    /**
     * 构建异常响应对象
     *
     * @param e 异常对象
     * @return 统一响应对象
     */
    private static R<?> buildErrorResponse(Exception e) {
        return buildErrorResponse(SYSTEM_ERROR_CODE, e, ErrorType.UNKNOWN.getMessage(), true);
    }

    /**
     * 构建异常响应对象
     *
     * @param e            异常对象
     * @param errorMessage 错误消息
     * @return 统一响应对象
     */
    private static R<?> buildErrorResponse(Exception e, String errorMessage) {
        return buildErrorResponse(SYSTEM_ERROR_CODE, e, errorMessage, true);
    }

    /**
     * 构建异常响应对象
     *
     * @param e               异常对象
     * @param errorType       错误类型
     * @param printStackTrace 是否打印堆栈
     * @return 统一响应对象
     */
    private static R<?> buildErrorResponse(Exception e, ErrorType errorType, boolean printStackTrace) {
        return buildErrorResponse(SYSTEM_ERROR_CODE, e, errorType.getMessage(), printStackTrace);
    }

    /**
     * 构建异常响应对象
     *
     * @param code            错误码
     * @param e               异常对象
     * @param errorMessage    错误消息
     * @param printStackTrace 是否打印堆栈
     * @return 统一响应对象
     */
    private static R<?> buildErrorResponse(int code, Throwable e, String errorMessage, boolean printStackTrace) {
        HttpServletRequest request = ServletUtils.getRequest();
        String msg = StrUtil.isEmpty(errorMessage) ? e.getMessage() : errorMessage;

        Object responseData;
        String stackTrace = null;
        if (printStackTrace) {
            ExceptionBO exceptionBO = ExceptionBO.of(e, msg);
            responseData = exceptionBO;
            stackTrace = exceptionBO.getStackTrace();
        } else {
            responseData = ExceptionUtil.getMessage(e);
        }

        logException(request, msg, stackTrace);
        return R.build(code, msg, responseData);
    }

    /**
     * 记录异常日志
     *
     * @param request    HTTP 请求
     * @param message    错误消息
     * @param stackTrace 堆栈信息
     */
    private static void logException(HttpServletRequest request, String message, String stackTrace) {
        log.error("[{}][{}] {} - {} {}{}",
                ServletUtils.getClientIP(),
                SecurityUtils.getNickName(),
                request.getMethod(),
                request.getRequestURI(),
                message,
                StrUtil.isEmpty(stackTrace) ? StrUtil.EMPTY : "\n" + stackTrace);
    }

    // ==================== 异常处理方法 ====================

    /**
     * 系统级异常处理
     *
     * @param e 未知异常
     * @return 统一响应对象
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        setJsonContentType();
        log.error("", e);
        return buildErrorResponse(e);
    }

    /**
     * 非法参数异常处理
     *
     * @param e 非法参数异常
     * @return 统一响应对象
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(e, ErrorType.PARAM, true);
    }

    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return 统一响应对象
     */
    @ExceptionHandler(ServiceException.class)
    public R<?> handleServiceException(ServiceException e) {
        R<?> response = toR(SYSTEM_ERROR_CODE, e, e.getMessage(), true);
        Optional.ofNullable(response.getData())
                .filter(data -> data instanceof ExceptionBO)
                .map(data -> (ExceptionBO) data)
                .ifPresent(exceptionBO -> {
                    String originalMessage = e.getOriginalMessage();
                    if (originalMessage != null) {
                        exceptionBO.setOriginalMessage(originalMessage);
                    }
                });
        return response;
    }

    /**
     * 未登录异常处理
     *
     * @param e 未登录异常
     * @return 统一响应对象
     */
    @ExceptionHandler(NotLoginException.class)
    public R<?> handleNotLoginException(NotLoginException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED.value(), e, ErrorType.NO_LOGIN.getMessage(), false);
    }

    /**
     * 权限不足异常处理
     *
     * @param e 权限不足异常
     * @return 统一响应对象
     */
    @ExceptionHandler(NotPermissionException.class)
    public R<?> handleNotPermissionException(NotPermissionException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN.value(), e, ErrorType.NO_PERMISSION.getMessage(), true);
    }

    /**
     * 角色不足异常处理
     *
     * @param e 角色不足异常
     * @return 统一响应对象
     */
    @ExceptionHandler(NotRoleException.class)
    public R<?> handleNotRoleException(NotRoleException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN.value(), e, ErrorType.NO_PERMISSION.getMessage(), true);
    }

    /**
     * 客户端请求中断异常处理
     *
     * @param e 客户端请求中断异常
     */
    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException e) {
        log.warn("客户端请求中断 - {}", e.getMessage());
    }

    /**
     * 方法参数校验异常处理
     *
     * @param e 参数校验异常
     * @return 统一响应对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<JSONObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        ObjectError firstError = allErrors.isEmpty() ? null : allErrors.get(0);

        String defaultMessage = firstError != null ? firstError.getDefaultMessage() : e.getMessage();
        R<JSONObject> response = R.fail(defaultMessage);

        if (!(firstError instanceof FieldError fieldError)) {
            return response;
        }

        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String fieldErrorMessage = fieldError.getDefaultMessage();

        // 构建详细的参数错误信息
        String detailedMessage = buildFieldErrorMessage(field, fieldErrorMessage, rejectedValue);
        JSONObject data = new JSONObject();
        data.put(field, rejectedValue);

        response.setMsg(detailedMessage);
        response.setData(data);

        log.warn("参数校验失败 - 路径：{}, 错误：{}, 数据：{}",
                getRequestPath(), detailedMessage, data);
        return response;
    }

    /**
     * 构建字段错误消息
     *
     * @param field          字段名
     * @param defaultMessage 默认错误消息
     * @param rejectedValue  被拒绝的值
     * @return 格式化后的错误消息
     */
    private String buildFieldErrorMessage(String field, String defaultMessage, Object rejectedValue) {
        String msg = StrUtil.format("{}", defaultMessage, field, rejectedValue);
        msg = StrUtil.replaceFirst(msg, "{field}", field);
        return field + " " + msg;
    }

    /**
     * 方法参数类型不匹配异常处理
     *
     * @param e 参数类型不匹配异常
     * @return 统一响应对象
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String expectedType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知";
        String actualType = e.getValue() != null ? e.getValue().getClass().getSimpleName() : "未知";
        String value = String.valueOf(e.getValue());

        String message = StrUtil.format("参数 '{}' 期望类型为 {}, 但接收的类型为 '{}', 当前值：'{}'",
                e.getName(), expectedType, actualType, value);
        return toR(SYSTEM_ERROR_CODE, e, "参数类型错误：" + message, false);
    }

    /**
     * 参数绑定异常处理
     *
     * @param e 参数绑定异常
     * @return 统一响应对象
     */
    @ExceptionHandler(BindException.class)
    public R<?> handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            String errorMessage = fieldErrors.stream()
                    .map(err -> err.getField() + ": " + err.getRejectedValue())
                    .collect(Collectors.joining(", "));
            return toR(SYSTEM_ERROR_CODE, e, "参数类型错误：" + errorMessage, false);
        }
        return buildErrorResponse(e, ErrorType.PARAM, true);
    }

    /**
     * 约束校验异常处理
     *
     * @param e 约束校验异常
     * @return 统一响应对象
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleConstraintViolationException(ConstraintViolationException e) {
        String message = StreamUtils.join(e.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
        return toR(SYSTEM_ERROR_CODE, e, "参数验证异常：" + message, false);
    }

    /**
     * HTTP 消息不可读异常处理（JSON 解析失败）
     *
     * @param e HTTP 消息不可读异常
     * @return 统一响应对象
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return buildErrorResponse(e, ErrorType.PARAM, true);
    }

    /**
     * 缺少请求参数异常处理
     *
     * @param e 缺少请求参数异常
     * @return 统一响应对象
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return toR(SYSTEM_ERROR_CODE, e, "缺少参数：" + e.getParameterName(), true);
    }

    // ==================== 内部枚举类 ====================

    /**
     * 错误类型枚举
     */
    @Getter
    @AllArgsConstructor
    public enum ErrorType {
        /**
         * 参数错误
         */
        PARAM("参数错误"),
        /**
         * 系统错误
         */
        SYSTEM("系统错误"),
        /**
         * 未知错误
         */
        UNKNOWN("未知错误"),
        /**
         * 未登录
         */
        NO_LOGIN("未登录"),
        /**
         * 没有权限
         */
        NO_PERMISSION("没有权限");

        private final String message;
    }
}