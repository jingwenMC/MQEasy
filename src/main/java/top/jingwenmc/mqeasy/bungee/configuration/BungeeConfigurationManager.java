package top.jingwenmc.mqeasy.bungee.configuration;

import top.jingwenmc.mqeasy.bungee.MQEasyBungee;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.configuration.BrokerConfigurationInfo;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationInfo;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationManager;

public class BungeeConfigurationManager implements ConfigurationManager {
    ConfigUtil configUtil;

    public BungeeConfigurationManager() {
        configUtil = new ConfigUtil(MQEasyBungee.getInstance());
    }

    @Override
    public ConfigurationInfo loadConfiguration() throws IllegalStateException{
        BrokerConfigurationInfo brokerConfigurationInfo =
                new BrokerConfigurationInfo(getBooleanIfExist("broker.enable"),getStringIfExist("broker.ipport"));
        return new ConfigurationInfo(brokerConfigurationInfo,getStringIfExist("remote.ipport"),"bungee",getBooleanIfExist("debug"));
    }

    public String getStringIfExist(String key) throws IllegalStateException {
        if(!configUtil.getConfig().contains(key))throw new IllegalStateException("Key Not Found: "+key);
        return configUtil.getConfig().getString(key);
    }

    public boolean getBooleanIfExist(String key) throws IllegalStateException {
        if(!configUtil.getConfig().contains(key))throw new IllegalStateException("Key Not Found: "+key);
        return configUtil.getConfig().getBoolean(key);
    }

    @Override
    public void validateVersion() throws IllegalStateException{
        String nowVersion = getStringIfExist("version");
        if(!nowVersion.equalsIgnoreCase(MQEasyCommon.MQEASY_CONFIG_VERSION))throw new IllegalStateException("Configuration version not correct! " +
                "Excepting: "+MQEasyCommon.MQEASY_CONFIG_VERSION+" Found: "+nowVersion);
    }
}
