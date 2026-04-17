package com.miqroera.biosensor.infra.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.miqroera.biosensor.infra.config.WechatConfig;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 微信工具类
 */
@Slf4j
public class WeChatUtil {
    private static final String CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 获取微信登录信息
     *
     * @param code 微信登录code
     * @return 微信用户信息
     */
    @SneakyThrows
    public static String code2Session(String code, WechatConfig config) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(StrUtil.format(CODE_2_SESSION_URL
                                + "?appid={}&secret={}&js_code={}&grant_type={}",
                        config.getAppId(), config.getAppSecret(), code, "authorization_code"))
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                String json = body.string();
                log.info("GET: {}\n{}", request.url(), json);
                JSONObject data = JSONObject.parseObject(json);
                Integer errcode = data.getInteger("errcode");
                if (errcode == null || errcode == 0) {
                    return data.getString("openid");
                }

                switch (errcode) {
                    case 40029:
                        throw ServiceException.of("[{}] code 无效", errcode);
                    case 40226:
                        throw ServiceException.of("[{}] 高风险等级用户，小程序登录拦截 。风险等级详见用户安全解方案: https://developers.weixin.qq.com/miniprogram/dev/framework/operation.html", errcode);
                    case 45011:
                        throw ServiceException.of("[{}] API 调用太频繁，请稍候再试", errcode);
                    default:
                        throw ServiceException.of("[{}] 系统繁忙，此时请开发者稍候再试", errcode);
                }
            }
        }
        return null;
    }

}
