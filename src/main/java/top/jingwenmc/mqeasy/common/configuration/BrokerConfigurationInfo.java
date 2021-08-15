package top.jingwenmc.mqeasy.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrokerConfigurationInfo {
    private boolean isEnabled;
    private String ipport;
}
