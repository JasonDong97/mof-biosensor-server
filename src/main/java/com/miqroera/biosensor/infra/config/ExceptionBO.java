package com.miqroera.biosensor.infra.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionBO {
    private static final String PACKAGE_PREFIX;

    static {
        String name = GlobalExceptionHandler.class.getPackage().getName();
        String[] packageNames = name.split("\\.");
        StringBuilder packagePrefix = new StringBuilder();
        for (int i = 0; i < packageNames.length; i++) {
            if (i >= 3) {
                break;
            }
            packagePrefix.append(packageNames[i]).append(".");
        }
        packagePrefix.setLength(packagePrefix.length() - 1);
        PACKAGE_PREFIX = packagePrefix.toString();
    }

    private String message;
    private String originalMessage;
    private List<String> stackTraceLines;

    @JsonIgnore
    private String stackTrace;

    public static ExceptionBO of(Throwable e, String msg) {

        ArrayList<String> stackTraceLines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage()).append("\n");
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().contains(PACKAGE_PREFIX)) {
                String st = stackTraceElement.toString();
                stackTraceLines.add(st);
                sb.append("\tat ").append(st).append("\n");
            }
        }
        String originalMsg = ExceptionUtil.getRootCauseMessage(e);
        return ExceptionBO.builder()
                .message(StrUtil.isBlank(msg) ? originalMsg : msg)
                .originalMessage(originalMsg)
                .stackTraceLines(stackTraceLines)
                .stackTrace(!sb.isEmpty() ? sb.toString() : ExceptionUtil.stacktraceToString(e))
                .build();
    }

    public static ExceptionBO of(Exception e) {
        return ExceptionBO.of(e, StrUtil.EMPTY);
    }
}

