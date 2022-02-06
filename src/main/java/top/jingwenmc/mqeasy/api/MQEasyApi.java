package top.jingwenmc.mqeasy.api;

import top.jingwenmc.mqeasy.api.exception.IdAlreadyExistException;
import top.jingwenmc.mqeasy.api.exception.MQEasyNotLoadException;
import top.jingwenmc.mqeasy.api.exception.PluginAlreadyRegisteredException;
import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.MessageType;
import top.jingwenmc.mqeasy.api.message.Receipt;
import top.jingwenmc.mqeasy.api.plugin.MQEasyPlugin;
import top.jingwenmc.mqeasy.common.MQEasyCommon;

import java.util.UUID;
import java.util.function.Consumer;

public class MQEasyApi {
    private final MQEasyPlugin plugin;

    public MQEasyApi(MQEasyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Send a message that need return to a player inside the network
     * @param to the destination player
     * @param body the body of message
     * @param messageConsumer what your plugin will do on receiving return message
     * @throws MQEasyNotLoadException
     * @throws IdAlreadyExistException
     */
    public void sendMessageToBukkitPlayerNeedReturn(String to, String body, Consumer<CommonMessage<Receipt>> messageConsumer) throws MQEasyNotLoadException, IdAlreadyExistException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        String id = UUID.randomUUID().toString();
        CommonMessage<String> commonMessage = new CommonMessage<>(plugin.getPluginInfo().getName(),id, MessageType.BUKKIT_PLAYER_WITH_RETURN,
                MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(),to,body);
        if(!plugin.addConsumer(id,messageConsumer)) throw new IdAlreadyExistException("Message ID already exist!");
        MQEasyCommon.getCommon().getMessenger().createTopic(commonMessage);
    }

    /**
     * Send a message that need return to a server inside the network
     * @param to the destination server , in server id
     * @param body the body of message
     * @param messageConsumer what your plugin will do on receiving return message
     * @throws MQEasyNotLoadException
     * @throws IdAlreadyExistException
     */
    public void sendMessageToServerNeedReturn(String to, String body, Consumer<CommonMessage<Receipt>> messageConsumer) throws MQEasyNotLoadException, IdAlreadyExistException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        String id = UUID.randomUUID().toString();
        CommonMessage<String> commonMessage = new CommonMessage<>(plugin.getPluginInfo().getName(),id, MessageType.SERVER_WITH_RETURN,
                MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(),to,body);
        if(!plugin.addConsumer(id,messageConsumer)) throw new IdAlreadyExistException("Message ID already exist!");
        MQEasyCommon.getCommon().getMessenger().produceMessage(commonMessage);
    }

    /**
     * Send a message that don't need to return to a player inside the network
     * @param to the destination player
     * @param body the body of message
     * @throws MQEasyNotLoadException
     */
    public void sendMessageToBukkitPlayerNoReturn(String to, String body) throws MQEasyNotLoadException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        String id = UUID.randomUUID().toString();
        CommonMessage<String> commonMessage = new CommonMessage<>(plugin.getPluginInfo().getName(),id, MessageType.BUKKIT_PLAYER_NO_RETURN,
                MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(),to,body);
        MQEasyCommon.getCommon().getMessenger().createTopic(commonMessage);
    }

    /**
     * Send a message that don't need to return to a player inside the network on bungeecord instance
     * @param to the destination player
     * @param body the body of message
     * @throws MQEasyNotLoadException
     */
    public void sendMessageToBungeePlayerNoReturn(String to, String body) throws MQEasyNotLoadException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        String id = UUID.randomUUID().toString();
        CommonMessage<String> commonMessage = new CommonMessage<>(plugin.getPluginInfo().getName(),id, MessageType.BUNGEE_PLAYER_NO_RETURN,
                MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(),to,body);
        MQEasyCommon.getCommon().getMessenger().createTopic(commonMessage);
    }

    /**
     * Send a message that need return to a player inside the network on bungeecord instance
     * @param to the destination player
     * @param body the body of message
     * @param messageConsumer what your plugin will do on receiving return message
     * @throws MQEasyNotLoadException
     * @throws IdAlreadyExistException
     */
    public void sendMessageToBungeePlayerNeedReturn(String to, String body, Consumer<CommonMessage<Receipt>> messageConsumer) throws MQEasyNotLoadException, IdAlreadyExistException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        String id = UUID.randomUUID().toString();
        CommonMessage<String> commonMessage = new CommonMessage<>(plugin.getPluginInfo().getName(),id, MessageType.BUNGEE_PLAYER_WITH_RETURN,
                MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(),to,body);
        if(!plugin.addConsumer(id,messageConsumer)) throw new IdAlreadyExistException("Message ID already exist!");
        MQEasyCommon.getCommon().getMessenger().createTopic(commonMessage);
    }

    /**
     * Send a message that don't need to return to a server inside the network
     * @param to the destination server , in server id
     * @param body the body of message
     * @throws MQEasyNotLoadException
     */
    public void sendMessageToServerNoReturn(String to, String body) throws MQEasyNotLoadException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        String id = UUID.randomUUID().toString();
        CommonMessage<String> commonMessage = new CommonMessage<>(plugin.getPluginInfo().getName(),id, MessageType.SERVER_NO_RETURN,
                MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(),to,body);
        MQEasyCommon.getCommon().getMessenger().produceMessage(commonMessage);
    }

    /**
     * Register new plugin
     * @param plugin the instance of plugin
     * @throws MQEasyNotLoadException
     * @throws PluginAlreadyRegisteredException
     */
    public static void registerPlugin(MQEasyPlugin plugin) throws MQEasyNotLoadException, PluginAlreadyRegisteredException {
        if(!MQEasyCommon.getCommon().isLoaded()) {
            throw new MQEasyNotLoadException("MQEasy Not Loaded.");
        }
        if(!MQEasyCommon.getCommon().getPluginManager().registerNewPlugin(plugin))
            throw new PluginAlreadyRegisteredException("Plugin Already Exist!");
    }
}
