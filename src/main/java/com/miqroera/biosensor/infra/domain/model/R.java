package com.miqroera.biosensor.infra.domain.model;

import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
@Data
public class R<T> implements Serializable {
    /**
     * 成功
     */
    public static final int OK_CODE = 200;
    /**
     * 失败
     */
    public static final int FAIL_CODE = 500;
    /**
     * 语言配置文件中的key
     */
    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "返回标记：成功标记=200，失败标记=500", example = "200")
    private int code = OK_CODE;

    @Schema(description = "返回消息", example = "请求成功")
    private String msg;

    @Schema(description = "返回数据")
    private transient T data;

    public static <T> R<T> ok() {
        return build(OK_CODE, "请求成功", null);
    }

    public static <T> R<T> ok(T data) {
        return build(OK_CODE, "请求成功", data);
    }

    public static <T> R<T> ok(T data, String msg) {
        return build(OK_CODE, msg, data);
    }

    public static <T> R<T> fail() {
        return build(FAIL_CODE, "请求失败", null);
    }

    public static <T> R<T> fail(String msg) {
        return build(FAIL_CODE, msg, null);
    }

    public static <T> R<T> fail(String msg, T data) {
        return build(FAIL_CODE, msg, data);
    }

    public static <T> R<T> build(int code, String msg) {
        return build(code, msg, null);
    }

    public static <T> R<T> build(int code, String msg, T data) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    public String json() {
        return JSON.toJSONString(this);
    }

}
