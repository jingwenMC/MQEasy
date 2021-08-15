package top.jingwenmc.mqeasy.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import top.jingwenmc.mqeasy.common.messaging.MQEasyMessenger;
import top.jingwenmc.mqeasy.common.platform.OnlineValidator;
import top.jingwenmc.mqeasy.common.platform.PlatformInfo;
import top.jingwenmc.mqeasy.common.plugin.PluginManager;

import java.util.logging.Logger;

public class MQEasyCommon {
    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Setter
    @Getter
    private PlatformInfo platformInfo;

    @Getter
    @Setter
    private boolean loaded = false;

    @Getter
    @Setter
    private Logger logger;

    @Getter
    @Setter
    private MQEasyMessenger messenger;

    @Setter
    private OnlineValidator onlineValidator;

    @Getter
    private final PluginManager pluginManager = new PluginManager();

    @Getter
    private static final MQEasyCommon common = new MQEasyCommon();

    public static final String MQEASY_GLOBAL_TOPIC = "mqeasyglobal";

    public static final String MQEASY_CONFIG_VERSION = "1a";

    public static boolean isOnline(String player) {
        return getCommon().onlineValidator.isOnline(player);
    }

    public static void debug(String message) {
        if(getCommon().getPlatformInfo().getConfigurationInfo().isDebug())getCommon().getLogger().info("DEBUG|"+message);
    }
}
