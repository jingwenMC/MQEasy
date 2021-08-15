package top.jingwenmc.mqeasy.api.plugin;

import lombok.Getter;
import top.jingwenmc.mqeasy.api.MQEasyApi;
import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.MessageType;
import top.jingwenmc.mqeasy.api.message.Receipt;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public abstract class MQEasyPlugin {

    public MQEasyPlugin() {
        api =  new MQEasyApi(this);
        timer = new Timer();
    }

    private final Timer timer;

    @Getter
    private final MQEasyApi api;

    private final Map<String, Consumer<CommonMessage<Receipt>>> returningMap = new HashMap<>();

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
    public abstract void onReceiveNoReturn(MessageType messageType, String to, CommonMessage<String> message);

    /**
     * What your plugin will do on receiving a message that have to return
     * @param messageType The type of message
     * @param to The destination, can be a player or server name
     * @param message The full message
     * @return A receipt message
     */
    public abstract Receipt onReceiveNeedReturn(MessageType messageType, String to, CommonMessage<String> message);

    public final void onReceiveReturnMessage(MessageType messageType, String id, CommonMessage<Receipt> message) {
        if(messageType.equals(MessageType.RETURNING_MESSAGE_PLAYER) || messageType.equals(MessageType.RETURNING_MESSAGE_SERVER)) {
            if(returningMap.containsKey(id)) {
                Consumer<CommonMessage<Receipt>> consumer = returningMap.get(id);
                consumer.accept(message);
            }
            returningMap.remove(id);
        }
    }

    public final boolean addConsumer(String id, Consumer<CommonMessage<Receipt>> messageConsumer) {
        if(returningMap.containsKey(id)) return false;
        returningMap.put(id,messageConsumer);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                returningMap.remove(id);
            }
        },10000);
        return true;
    }
}
