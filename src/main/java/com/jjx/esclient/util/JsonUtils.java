package com.jjx.esclient.util;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * json序列,反序列工具
 *
 * @author jiangjx
 */
@SuppressWarnings("unused")
public class JsonUtils {

    public static <T> String toJSONString(T obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

}
