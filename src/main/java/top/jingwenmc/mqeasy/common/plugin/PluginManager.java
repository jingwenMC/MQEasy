package top.jingwenmc.mqeasy.common.plugin;

import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.Receipt;
import top.jingwenmc.mqeasy.api.plugin.MQEasyPlugin;
import top.jingwenmc.mqeasy.api.plugin.MQEasyPluginInfo;
import top.jingwenmc.mqeasy.common.MQEasyCommon;

import java.util.HashMap;
import java.util.Map;

public class PluginManager {
    private final Map<String, MQEasyPlugin> pluginMap = new HashMap<>();

    public boolean registerNewPlugin(MQEasyPlugin mqEasyPlugin) {
        MQEasyPluginInfo info = mqEasyPlugin.getPluginInfo();
        String pluginName = info.getName();
        if(pluginMap.containsKey(pluginName))return false;
        pluginMap.put(pluginName,mqEasyPlugin);
        MQEasyCommon.getCommon().getLogger().info("+++++++++++++++[MQEasy-New-Plugin]+++++++++++++++");
        MQEasyCommon.getCommon().getLogger().info("Name:"+mqEasyPlugin.getPluginInfo().getName());
        StringBuilder builder = new StringBuilder();
        for(String s : mqEasyPlugin.getPluginInfo().getAuthors()) builder.append(s).append(" ");
        MQEasyCommon.getCommon().getLogger().info("Authors:"+ builder);
        MQEasyCommon.getCommon().getLogger().info("Version:"+mqEasyPlugin.getPluginInfo().getVersion());
        MQEasyCommon.getCommon().getLogger().info("Website:"+mqEasyPlugin.getPluginInfo().getWebsite());
        MQEasyCommon.getCommon().getLogger().info("Description:"+mqEasyPlugin.getPluginInfo().getDescription());
        MQEasyCommon.getCommon().getLogger().info("+++++++++++++++[MQEasy-New-Plugin]+++++++++++++++");
        return true;
    }

    public void sendMessageToPlugin(String plugin, CommonMessage<?> message) {
        if(!pluginMap.containsKey(plugin))return;
        MQEasyPlugin mqEasyPlugin = pluginMap.get(plugin);
        mqEasyPlugin.onReceiveNoReturn(message.getMessageType(), message.getTo(), message);
    }

    public Receipt<?> sendNeedReturnMessageToPlugin(String plugin, CommonMessage<?> message) {
        if(!pluginMap.containsKey(plugin))return null;
        MQEasyPlugin mqEasyPlugin = pluginMap.get(plugin);
        return mqEasyPlugin.onReceiveNeedReturn(message.getMessageType(), message.getTo(), message);
    }

    public void sendReturnMessageToPlugin(String plugin, CommonMessage<Receipt<?>> message) {
        if(!pluginMap.containsKey(plugin))return;
        MQEasyPlugin mqEasyPlugin = pluginMap.get(plugin);
        mqEasyPlugin.onReceiveReturnMessage(message.getMessageType(),message.getId(),message);
    }
}
