package top.jingwenmc.mqeasy.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigurationBean {
    private String version;
    private BrokerConfigurationBean brokerConfiguration;
    private ConnectionConfigurationBean connectionConfiguration;
}
