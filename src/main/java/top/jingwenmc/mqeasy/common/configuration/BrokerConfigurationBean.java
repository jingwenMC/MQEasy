package top.jingwenmc.mqeasy.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BrokerConfigurationBean {
    private boolean isEnabled;
    private ConnectionConfigurationBean connectionConfiguration;
}
