package org.team10424102.blackserver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by yy on 8/30/15.
 */
public class JsonUtils {
    public static ObjectMapper mapper = new ObjectMapper();

    public static String convertToString(Object obj) throws JsonProcessingException{
        return mapper.writeValueAsString(obj);
    }

    public static Object convertToObject(String json, Class clazz) throws IOException{
        return mapper.readValue(json, clazz);
    }
}
