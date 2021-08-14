package top.jingwenmc.mqeasy.common.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.configuration.ConnectionConfigurationInfo;

import javax.jms.*;
import java.util.UUID;

public class MQEasyMessenger {
    final ConnectionFactory factory;
    final String password;
    MQEasyMessageListener messageListener;
    public MQEasyMessenger(ConnectionConfigurationInfo connectionConfigurationInfo, String password) throws JMSException {
        factory = new ActiveMQConnectionFactory("tcp://"+connectionConfigurationInfo.getIp()
                +":"+connectionConfigurationInfo.getPort());
        this.password = password;
        Connection testConnection = factory.createConnection(MQEasyCommon.MQEASY_DEFAULT_USERNAME,password);
        testConnection.start();
        testConnection.close();
        MQEasyCommon.getCommon().getLogger().info("MQ Connection Test Passed.");
        messageListener = new MQEasyMessageListener();
        MQEasyCommon.getCommon().getLogger().info("Listener is now OK.");
    }

    public void produceMessage(CommonMessage<?> message) {
        new Thread(() -> {
            try {
                Connection connection = factory.createConnection(MQEasyCommon.MQEASY_DEFAULT_USERNAME, password);
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Queue queue = session.createQueue(message.getTo());
                MessageProducer producer = session.createProducer(queue);
                message.setId(UUID.randomUUID().toString());
                TextMessage textMessage = session.createTextMessage(MQEasyCommon.getCommon()
                    .getObjectMapper().writeValueAsString(message));
                producer.send(textMessage);
                producer.close();
                session.close();
                connection.close();
            } catch (Throwable t) {
                t.printStackTrace();
                System.err.println("===============[MQEasy-Runtime-Error]===============");
                System.err.println("Error sending message.");
                System.err.println("Please check your connection.");
                System.err.println("Plugin(s) may not work properly.");
                System.err.println("===============[MQEasy-Runtime-Error]===============");
            }
        }).start();
    }

    public void createTopic(CommonMessage<?> message) throws JMSException {
        new Thread(() -> {
            try {
                Connection connection = factory.createConnection(MQEasyCommon.MQEASY_DEFAULT_USERNAME, password);
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic(MQEasyCommon.MQEASY_GLOBAL_TOPIC);
                MessageProducer producer = session.createProducer(topic);
                message.setId(UUID.randomUUID().toString());
                TextMessage textMessage = session.createTextMessage(MQEasyCommon.getCommon()
                        .getObjectMapper().writeValueAsString(message));
                producer.send(textMessage);
                producer.close();
                session.close();
                connection.close();
            } catch (Throwable t) {
                t.printStackTrace();
                System.err.println("===============[MQEasy-Runtime-Error]===============");
                System.err.println("Error sending message.");
                System.err.println("Please check your connection.");
                System.err.println("Plugin(s) may not work properly.");
                System.err.println("===============[MQEasy-Runtime-Error]===============");
            }
        }).start();
    }

    public void stop() throws JMSException {
        messageListener.stop();
    }
}
