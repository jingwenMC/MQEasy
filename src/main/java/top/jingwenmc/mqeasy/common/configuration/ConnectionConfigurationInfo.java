package top.jingwenmc.mqeasy.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionConfigurationInfo {
    private String ip;
    private String port;
}
