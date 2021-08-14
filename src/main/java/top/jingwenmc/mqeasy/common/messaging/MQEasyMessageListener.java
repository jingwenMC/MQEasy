package top.jingwenmc.mqeasy.common.messaging;

import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.api.message.MessageType;
import top.jingwenmc.mqeasy.api.message.Receipt;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.platform.PlatformType;

import javax.jms.*;
import java.util.Timer;
import java.util.TimerTask;

public class MQEasyMessageListener implements MessageListener,ExceptionListener{
    private final Connection connection;
    private final MessageConsumer queueMessageConsumer;
    private final MessageConsumer topicMessageConsumer;
    private final Session session;

    public MQEasyMessageListener() throws JMSException {
        connection = MQEasyCommon.getCommon().getMessenger().factory.createConnection(
                MQEasyCommon.MQEASY_DEFAULT_USERNAME,MQEasyCommon.getCommon().getMessenger().password
        );
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
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                CommonMessage<?> commonMessage = MQEasyCommon.getCommon().getObjectMapper()
                        .convertValue(textMessage.getText(), CommonMessage.class);
                switch (commonMessage.getMessageType()) {
                    case PLAYER_NO_RETURN:
                        if(MQEasyCommon.isOnline(commonMessage.getTo()) &&
                                MQEasyCommon.getCommon().getPlatformInfo().getPlatformType().equals(PlatformType.BUKKIT)) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendMessageToPlugin(plugin,commonMessage);
                        }
                    case SERVER_NO_RETURN:
                        if(commonMessage.getTo().equals(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId())) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendMessageToPlugin(plugin,commonMessage);
                        }
                    case PLAYER_WITH_RETURN:
                        if(MQEasyCommon.isOnline(commonMessage.getTo()) &&
                                MQEasyCommon.getCommon().getPlatformInfo().getPlatformType().equals(PlatformType.BUKKIT)) {
                            String plugin = commonMessage.getPlugin();
                            Receipt<?> receipt
                                    = MQEasyCommon.getCommon().getPluginManager().sendNeedReturnMessageToPlugin(plugin,commonMessage);
                            if(receipt!=null)
                            MQEasyCommon.getCommon().getMessenger().produceMessage(
                                    new CommonMessage<Receipt<?>>(commonMessage.getPlugin(), commonMessage.getId(), MessageType.RETURNING_MESSAGE_PLAYER
                                            , MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(), commonMessage.getFrom(), receipt));
                        }
                    case SERVER_WITH_RETURN:
                        if(commonMessage.getTo().equals(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId())) {
                            String plugin = commonMessage.getPlugin();
                            Receipt<?> receipt
                                    = MQEasyCommon.getCommon().getPluginManager().sendNeedReturnMessageToPlugin(plugin,commonMessage);
                            if(receipt!=null)
                            MQEasyCommon.getCommon().getMessenger().produceMessage(
                                    new CommonMessage<Receipt<?>>(commonMessage.getPlugin(), commonMessage.getId(), MessageType.RETURNING_MESSAGE_SERVER
                                            , MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId(), commonMessage.getFrom(), receipt));
                        }
                    case RETURNING_MESSAGE_PLAYER:
                    case RETURNING_MESSAGE_SERVER:
                        if(commonMessage.getTo().equals(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getId())) {
                            String plugin = commonMessage.getPlugin();
                            MQEasyCommon.getCommon().getPluginManager().sendReturnMessageToPlugin(plugin, (CommonMessage<Receipt<?>>) commonMessage);
                        }
                }
            } catch (JMSException e) {
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
        System.err.println("Try to reconnect in 15 seconds.");
        System.err.println("===============[MQEasy-Runtime-Error]===============");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    MQEasyCommon.getCommon().getMessenger().messageListener = new MQEasyMessageListener();
                } catch (JMSException e) {
                    MQEasyCommon.getCommon().getMessenger().messageListener.onException(e);
                }
            }
        },15000);
    }
}
