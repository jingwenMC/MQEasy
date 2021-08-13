package top.jingwenmc.mqeasy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import top.jingwenmc.mqeasy.common.platform.PlatformInfo;

public class MQEasyCommon {
    public MQEasyCommon(PlatformInfo platformInfo) {
        this.platformInfo = platformInfo;
    }

    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private final PlatformInfo platformInfo;
}
