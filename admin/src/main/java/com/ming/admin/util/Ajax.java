package com.ming.admin.util;

import java.util.LinkedHashMap;

public class Ajax extends LinkedHashMap<String, Object> {
    Ajax() {
    }

    Ajax(int code) {
        this.put("code", code);
    }

    Ajax(int code, String msg) {
        this.put("code", code);
        this.put("msg", msg);
    }

    Ajax(int code, String msg, Object data) {
        this.put("code", code);
        this.put("msg", msg);
        this.put("data", data);
    }

    public static Ajax success() {
        return new Ajax(200);
    }

    public static Ajax success(int code, String msg) {
        return new Ajax(code, msg);
    }
    public static Ajax success(String msg) {
        return new Ajax(200, msg);
    }

    public static Ajax success(String msg, Object data) {
        return new Ajax(200, msg, data);
    }

    public static Ajax success(Object data) {
        return new Ajax(200, "操作成功", data);
    }

    public static Ajax error() {
        return new Ajax(4000);
    }
    public static Ajax error(int code,String msg) {
        return new Ajax(code, msg);
    }
    public static Ajax error(String msg) {
        return new Ajax(4000, msg);
    }

    public static Ajax error(String msg, Object data) {
        return new Ajax(4000, msg, data);
    }

    public static Ajax error(Object data) {
        return new Ajax(4000, "操作失败", data);
    }

}
