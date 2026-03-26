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

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@Order
@RestControllerAdvice
@Slf4j(topic = "error_log")
public class GlobalExceptionHandler {

    private static void replaceContentType() {
        ServletUtils.getResponse().setContentType("application/json;charset=UTF-8");
    }

    private static String getPath() {
        try {
            return ServletUtils.getRequest().getRequestURI();
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static R<?> toR(Exception e, boolean printStackTrace) {
        return toR(500, e, StrUtil.EMPTY, printStackTrace);
    }

    public static R<?> toR(Exception e, String msg, boolean printStackTrace) {
        return toR(500, e, msg, printStackTrace);
    }

    public static R<?> toR(int code, Exception e, String msg, boolean printStackTrace) {
        HttpServletRequest request = ServletUtils.getRequest();

        if (StrUtil.isEmpty(msg)) {
            msg = e.getMessage();
        }

        Object data;
        String stackTrace = null;
        if (printStackTrace) {
            ExceptionBO ebo = ExceptionBO.of(e, msg);
            data = ebo;
            stackTrace = ebo.getStackTrace();
        } else {
            data = ExceptionUtil.getMessage(e);
        }

        log.error("[{}][{}] {} - {}, {}{}",
                ServletUtils.getClientIP(),
                SecurityUtils.getNickName(),
                request.getMethod(),
                request.getRequestURI(),
                msg,
                StrUtil.isEmpty(stackTrace) ? StrUtil.EMPTY : "\n" + stackTrace);
        return R.build(code, msg, data);
    }

    public static R<?> toR(Exception e, ERROR_TYPE type, boolean printStackTrace) {
        return toR(500, e, type.getMsg(), printStackTrace);
    }

    public static R<?> toR(int code, Exception e, ERROR_TYPE type, boolean printStackTrace) {
        return toR(code, e, type.getMsg(), printStackTrace);
    }


    /**
     * 系统异常
     *
     * @param e Exception
     * @return R<ExceptionBO>
     */
    @ExceptionHandler(Exception.class)
    R<?> handle(Exception e) {
        replaceContentType();
        return toR(e, ERROR_TYPE.UNKNOWN, true);
    }

    /**
     * 非法参数异常
     *
     * @param e IllegalArgumentException
     * @return R<ExceptionBO>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    R<?> handle(IllegalArgumentException e) {
        return toR(e, ERROR_TYPE.PARAM, true);
    }

    /**
     * 业务异常
     *
     * @param e ServiceException
     * @return R<ExceptionBO>
     */
    @ExceptionHandler(ServiceException.class)
    R<?> handle(ServiceException e) {
        String originalMessage = e.getOriginalMessage();
        R<?> r = toR(e, true);
        Object data = r.getData();
        if (originalMessage != null && data instanceof ExceptionBO) {
            ((ExceptionBO) data).setOriginalMessage(originalMessage);
        }
        return r;
    }

    /**
     * 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    R<?> handle(NotLoginException e) {
        return toR(HttpStatus.UNAUTHORIZED.value(), e, ERROR_TYPE.NO_LOGIN, false);
    }

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    R<?> handle(NotPermissionException e) {
        return toR(HttpStatus.FORBIDDEN.value(), e, ERROR_TYPE.NO_PERMISSION, true);
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    R<?> handle(NotRoleException e) {
        return toR(HttpStatus.FORBIDDEN.value(), e, ERROR_TYPE.NO_PERMISSION, true);
    }

    /**
     * 请求中断异常
     */
    @ExceptionHandler(ClientAbortException.class)
    void handle(ClientAbortException e) {
        log.warn("请求中断 - {}", e.getMessage());
    }

    /**
     * 方法参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    R<JSONObject> handle(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        R<JSONObject> fail = R.fail(objectError != null ? objectError.getDefaultMessage() : e.getMessage());
        if (!(objectError instanceof FieldError fieldError)) {
            return fail;
        }

        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String msg = StrUtil.format("{}", fieldError.getDefaultMessage(), field, rejectedValue);
        msg = StrUtil.replaceFirst(msg, "{field}", field);
        JSONObject data = new JSONObject();
        data.put(field, rejectedValue);
        fail.setMsg(field + " " + msg);
        fail.setData(data);
        log.warn("{}, 参数错误 - {}:{} ", getPath(), fail.getMsg(), fail.getData());
        return fail;
    }

    /**
     * 方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    R<?> handle(MethodArgumentTypeMismatchException e) {
        String msg = StrUtil.format("参数 '{}' 期望类型为 {}, 但接收的类型为 '{}', 当前值: '{}'",
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知",
                e.getValue() != null ? e.getValue().getClass().getSimpleName() : "未知",
                e.getValue());
        return toR(e, "参数类型错误! " + msg, false);
    }

    @ExceptionHandler(BindException.class)
    R<?> handle(BindException e) {
        if (!e.getFieldErrors().isEmpty()) {
            String msg = e.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getRejectedValue())
                    .collect(Collectors.joining(", "));
            return toR(e, "参数类型错误! " + msg, false);
        }
        return toR(e, ERROR_TYPE.PARAM, true);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    R<?> handle(ConstraintViolationException e) {
        String message = StreamUtils.join(e.getConstraintViolations(), ConstraintViolation::getMessage, ", ");
        return toR(e, "参数验证异常:" + message, false);
    }

    /**
     * json 参数解析异常
     *
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    R<?> handle(HttpMessageNotReadableException e) {
        return toR(e, ERROR_TYPE.PARAM, true);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    R<?> handle(MissingServletRequestParameterException e) {
        return toR(e, "缺少参数: " + e.getParameterName(), true);
    }

    @Getter
    @AllArgsConstructor
    public enum ERROR_TYPE {
        PARAM("参数错误"),
        SYSTEM("系统错误"),
        UNKNOWN("未知错误"),
        NO_LOGIN("未登录"),
        NO_PERMISSION("没有权限");
        private final String msg;
    }

}