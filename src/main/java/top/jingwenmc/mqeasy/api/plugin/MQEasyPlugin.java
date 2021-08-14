package top.jingwenmc.mqeasy.api.plugin;

import lombok.Getter;
import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.MessageType;
import top.jingwenmc.mqeasy.api.message.Receipt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MQEasyPlugin {
    @Getter
    final Map<String, Consumer<CommonMessage<Receipt<?>>>> returningMap = new HashMap<>();

    /**
     * Get plugin's info
     * @return Your plugin's Info
     */
    public abstract MQEasyPluginInfo getPluginInfo();

    /**
     * What your plugin will do on receiving a message that not have to return
     * @param messageType The type of message
     * @param to The destination, can be a player or server name
     * @param message The full message
     */
    public abstract void onReceiveNoReturn(MessageType messageType, String to, CommonMessage<?> message);

    /**
     * What your plugin will do on receiving a message that have to return
     * @param messageType The type of message
     * @param to The destination, can be a player or server name
     * @param message The full message
     * @return A receipt message
     */
    public abstract Receipt<?> onReceiveNeedReturn(MessageType messageType, String to, CommonMessage<?> message);

    public final void onReceiveReturnMessage(MessageType messageType, String id, CommonMessage<Receipt<?>> message) {
        if(messageType.equals(MessageType.RETURNING_MESSAGE_PLAYER) || messageType.equals(MessageType.RETURNING_MESSAGE_SERVER)) {
            if(returningMap.containsKey(id)) {
                Consumer<CommonMessage<Receipt<?>>> consumer = returningMap.get(id);
                consumer.accept(message);
            }
        }
    }
}
