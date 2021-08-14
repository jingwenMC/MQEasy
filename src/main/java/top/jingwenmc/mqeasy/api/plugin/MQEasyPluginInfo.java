package top.jingwenmc.mqeasy.api.plugin;

import java.util.List;

public interface MQEasyPluginInfo {
    String getName();
    List<String> getAuthors();
    String getVersion();
    String getDescription();
    String getWebsite();
}
