package top.jingwenmc.mqeasy.bukkit.configuration;

import top.jingwenmc.mqeasy.bukkit.MQEasyBukkit;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.configuration.BrokerConfigurationInfo;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationInfo;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationManager;

public class BukkitConfigurationManager implements ConfigurationManager {
    @Override
    public ConfigurationInfo loadConfiguration() throws IllegalStateException{
        BrokerConfigurationInfo brokerConfigurationInfo =
                new BrokerConfigurationInfo(getBooleanIfExist("broker.enable"),getStringIfExist("broker.ipport"));
        return new ConfigurationInfo(brokerConfigurationInfo,getStringIfExist("remote.ipport"),getStringIfExist("id"),getBooleanIfExist("debug"));
    }

    public String getStringIfExist(String key) throws IllegalStateException {
        if(!MQEasyBukkit.getInstance().getConfig().contains(key))throw new IllegalStateException("Key Not Found: "+key);
        if(!MQEasyBukkit.getInstance().getConfig().isString(key))throw new IllegalStateException("Unexpected Type, expecting String: "+key);
        return MQEasyBukkit.getInstance().getConfig().getString(key);
    }

    public boolean getBooleanIfExist(String key) throws IllegalStateException {
        if(!MQEasyBukkit.getInstance().getConfig().contains(key))throw new IllegalStateException("Key Not Found: "+key);
        if(!MQEasyBukkit.getInstance().getConfig().isBoolean(key))throw new IllegalStateException("Unexpected Type, expecting String: "+key);
        return MQEasyBukkit.getInstance().getConfig().getBoolean(key);
    }

    @Override
    public void validateVersion() throws IllegalStateException{
        String nowVersion = getStringIfExist("version");
        if(!nowVersion.equalsIgnoreCase(MQEasyCommon.MQEASY_CONFIG_VERSION))throw new IllegalStateException("Configuration version not correct! " +
                "Excepting: "+MQEasyCommon.MQEASY_CONFIG_VERSION+" Found: "+nowVersion);
    }
}
