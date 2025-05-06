package org.example.common;

import cn.hutool.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class CommonResult extends JSONObject {

    private static final long serialVersionUID = 1L;

    private CommonResult(int code, String message, Object data) {
        this.set("code", code);
        this.set("message", message);
        this.set("data", data);
    }

    // 成功返回
    public static CommonResult success(Object data) {
        return new CommonResult(200, "success", data);
    }
    // 成功返回
    public static CommonResult success() {
        return new CommonResult(200, "success", null);
    }

    // 错误返回
    public static CommonResult error(int code, String message) {
        return new CommonResult(code, message, null);
    }

    // 错误返回（默认错误码 500）
    public static CommonResult error(String message) {
        return new CommonResult(500, message, null);
    }
}