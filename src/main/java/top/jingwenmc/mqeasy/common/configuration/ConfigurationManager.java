package top.jingwenmc.mqeasy.common.configuration;

public interface ConfigurationManager {
    ConfigurationInfo loadConfiguration();
    void validateVersion() throws IllegalStateException;
}
