package com.sweven.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Sweven on 2019/4/21.
 * Email:sweventears@Foxmail.com
 */
public class JsonUtil {

    public static String object2Json(Object o) {
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T jsonToObject(String json, T t) {
        Class aClass = t.getClass();
        new LogUtil("JSON转对象").i("json:" + json);
        try {
            t = (T) new ObjectMapper().readValue(json, aClass);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return t;
    }
}
