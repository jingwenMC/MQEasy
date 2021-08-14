package top.jingwenmc.mqeasy.common.platform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationInfo;

@AllArgsConstructor
public class PlatformInfo {
    @Getter
    private final PlatformType platformType;

    @Getter
    private final ConfigurationInfo configurationInfo;
}
