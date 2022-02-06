package top.jingwenmc.mqeasy.common.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.MessageType;
import top.jingwenmc.mqeasy.api.message.Receipt;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.platform.PlatformType;

import javax.jms.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MQEasyMessageListener implements MessageListener,ExceptionListener{
    private final Connection connection;
    private final MessageConsumer queueMessageConsumer;
    private final MessageConsumer topicMessageConsumer;
    private final Session session;
    private final Set<String> read = new HashSet<>();

    public MQEasyMessageListener() throws JMSException {
        connection = MQEasyCommon.getCommon().getMessenger().factory.createConnection();
        connection.setExceptionListener(this);
        connection.start();
        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId());
        queueMessageConsumer = session.createConsumer(queue);
        queueMessageConsumer.setMessageListener(this);
        Topic topic = session.createTopic(MQEasyCommon.MQEASY_GLOBAL_TOPIC);
        topicMessageConsumer = session.createConsumer(topic);
        topicMessageConsumer.setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        MQEasyCommon.debug("Message Get!");
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                MQEasyCommon.debug("TextMessage Get:"+textMessage.getText());
                CommonMessage<String> commonMessage = MQEasyCommon.getCommon().getObjectMapper()
                        .readValue(textMessage.getText(), new TypeReference<CommonMessage<String>>(){});
                if(read.contains(commonMessage.getId()))return;
                read.add(commonMessage.getId());
                MQEasyCommon.debug("CommonMessage Get:"+commonMessage);
                switch (commonMessage.getMessageType()) {
                    case BUKKIT_PLAYER_NO_RETURN:
                        if(MQEasyCommon.isOnline(commonMessage.getTo()) &&
                                MQEasyCommon.getCommon().getPlatformInfo().getPlatformType().equals(PlatformType.BUKKIT)) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendMessageToPlugin(plugin,commonMessage);
                        }
                    case BUNGEE_PLAYER_NO_RETURN:
                        if(MQEasyCommon.isOnline(commonMessage.getTo()) &&
                                MQEasyCommon.getCommon().getPlatformInfo().getPlatformType().equals(PlatformType.BUNGEE)) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendMessageToPlugin(plugin,commonMessage);
                        }
                    case SERVER_NO_RETURN:
                        if(commonMessage.getTo().equals(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId())) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendMessageToPlugin(plugin,commonMessage);
                        }
                    case BUKKIT_PLAYER_WITH_RETURN:
                        if(MQEasyCommon.isOnline(commonMessage.getTo()) &&
                                MQEasyCommon.getCommon().getPlatformInfo().getPlatformType().equals(PlatformType.BUKKIT)) {
                            String plugin = commonMessage.getPlugin();
                            Receipt receipt
                                    = MQEasyCommon.getCommon().getPluginManager().sendNeedReturnMessageToPlugin(plugin,commonMessage);
                            if(receipt!=null)
                            MQEasyCommon.getCommon().getMessenger().produceMessage(
                                    new CommonMessage<>(commonMessage.getPlugin(), commonMessage.getId(), MessageType.RETURNING_MESSAGE_BUKKIT_PLAYER
                                            , MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(), commonMessage.getFrom(), receipt));
                        }
                    case BUNGEE_PLAYER_WITH_RETURN:
                        if(MQEasyCommon.isOnline(commonMessage.getTo()) &&
                                MQEasyCommon.getCommon().getPlatformInfo().getPlatformType().equals(PlatformType.BUNGEE)) {
                            String plugin = commonMessage.getPlugin();
                            Receipt receipt
                                    = MQEasyCommon.getCommon().getPluginManager().sendNeedReturnMessageToPlugin(plugin,commonMessage);
                            if(receipt!=null)
                                MQEasyCommon.getCommon().getMessenger().produceMessage(
                                        new CommonMessage<>(commonMessage.getPlugin(), commonMessage.getId(), MessageType.RETURNING_MESSAGE_BUNGEE_PLAYER
                                                , MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(), commonMessage.getFrom(), receipt));
                        }
                    case SERVER_WITH_RETURN:
                        if(commonMessage.getTo().equals(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId())) {
                            String plugin = commonMessage.getPlugin();
                            Receipt receipt
                                    = MQEasyCommon.getCommon().getPluginManager().sendNeedReturnMessageToPlugin(plugin,commonMessage);
                            if(receipt!=null)
                            MQEasyCommon.getCommon().getMessenger().produceMessage(
                                    new CommonMessage<>(commonMessage.getPlugin(), commonMessage.getId(), MessageType.RETURNING_MESSAGE_SERVER
                                            , MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(), commonMessage.getFrom(), receipt));
                        }
                    case RETURNING_MESSAGE_BUKKIT_PLAYER:
                    case RETURNING_MESSAGE_BUNGEE_PLAYER:
                    case RETURNING_MESSAGE_SERVER:
                        if(commonMessage.getTo().equals(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId())) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendReturnMessageToPlugin(plugin,
                                    MQEasyCommon.getCommon().getObjectMapper()
                                            .readValue(textMessage.getText(), new TypeReference<CommonMessage<Receipt>>(){}));
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("===============[MQEasy-Runtime-Error]===============");
                System.err.println("Error receiving message.");
                System.err.println("This error shouldn't appear.");
                System.err.println("Please check your version or report to dev.");
                System.err.println("===============[MQEasy-Runtime-Error]===============");
            }
        }
    }

    public void stop() throws JMSException {
        queueMessageConsumer.close();
        topicMessageConsumer.close();
        session.close();
        connection.close();
    }

    @Override
    public void onException(JMSException exception) {
        exception.printStackTrace();
        System.err.println("===============[MQEasy-Runtime-Error]===============");
        System.err.println("Error checking message.");
        System.err.println("Please check your connection.");
        System.err.println("Plugin(s) may not work properly.");
        System.err.println("Try to reconnect in 15 seconds...");
        System.err.println("===============[MQEasy-Runtime-Error]===============");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    MQEasyCommon.getCommon().getMessenger().messageListener = new MQEasyMessageListener();
                    System.out.println("MQEasy Reconnect Complete.");
                } catch (JMSException e) {
                    MQEasyCommon.getCommon().getMessenger().messageListener.onException(e);
                }
            }
        },15000);
    }
}
