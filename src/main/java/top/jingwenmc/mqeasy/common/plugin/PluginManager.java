package top.jingwenmc.mqeasy.common.plugin;

import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.Receipt;
import top.jingwenmc.mqeasy.api.plugin.MQEasyPlugin;
import top.jingwenmc.mqeasy.api.plugin.MQEasyPluginInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PluginManager {
    private final Map<String, MQEasyPlugin> pluginMap = new HashMap<>();

    public boolean registerNewPlugin(MQEasyPlugin mqEasyPlugin) {
        MQEasyPluginInfo info = mqEasyPlugin.getPluginInfo();
        String pluginName = info.getName();
        if(pluginMap.containsKey(pluginName))return false;
        pluginMap.put(pluginName,mqEasyPlugin);
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
