package top.jingwenmc.mqeasy.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigurationInfo {
    private BrokerConfigurationInfo brokerConfiguration;
    private String ipport;
    private String id;
    private boolean debug;
}
