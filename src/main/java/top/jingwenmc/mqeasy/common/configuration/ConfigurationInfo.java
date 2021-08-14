package top.jingwenmc.mqeasy.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigurationInfo {
    private String version;
    private BrokerConfigurationInfo brokerConfiguration;
    private ConnectionConfigurationInfo connectionConfiguration;
    private String id;
    private String password;
    private boolean debug;
}
