package top.jingwenmc.mqeasy.api.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import top.jingwenmc.mqeasy.common.MQEasyCommon;

import java.io.IOException;

public class MQEasyJsonUtil {
    public static String parseObject(Object object) throws JsonProcessingException {
        return MQEasyCommon.getCommon().getObjectMapper().writeValueAsString(object);
    }

    public static <T> T parseJSON(String input, Class<T> clazz) throws IOException {
        return MQEasyCommon.getCommon().getObjectMapper().readValue(input,clazz);
    }
}
