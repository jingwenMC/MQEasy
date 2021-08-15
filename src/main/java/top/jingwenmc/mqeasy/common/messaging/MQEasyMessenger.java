package top.jingwenmc.mqeasy.common.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.activemq.ActiveMQConnectionFactory;
import top.jingwenmc.mqeasy.api.message.CommonMessage;
import top.jingwenmc.mqeasy.common.MQEasyCommon;

import javax.jms.*;
import java.util.UUID;

public class MQEasyMessenger {
    ConnectionFactory factory;
    MQEasyMessageListener messageListener;
    public MQEasyMessenger(String ipport) {
        MQEasyCommon.debug("Connecting:"+"tcp://"+ipport);
        factory = new ActiveMQConnectionFactory("tcp://"+ipport);
    }

    public void initListener() throws InterruptedException {
        try {
            if (messageListener != null) return;
            messageListener = new MQEasyMessageListener();
            MQEasyCommon.getCommon().getLogger().info("Listener is now OK.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("===============[MQEasy-Init-Error]===============");
            System.err.println("Error connecting broker.");
            System.err.println("Please check your connection or your configuration.");
            System.err.println("Plugin(s) may not work properly.");
            System.err.println("Try to reconnect in 15 seconds...");
            System.err.println("===============[MQEasy-Init-Error]===============");
            Thread.sleep(15000);
            initListener();
        }
    }

    public void produceMessage(CommonMessage<?> message) {
        try {
            MQEasyCommon.debug("Produce:"+MQEasyCommon.getCommon().getObjectMapper().writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                Connection connection = factory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Queue queue = session.createQueue(message.getTo());
                MessageProducer producer = session.createProducer(queue);
                if(message.getId().isEmpty())
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

    public void createTopic(CommonMessage<?> message) {
        try {
            MQEasyCommon.debug("Topic:"+MQEasyCommon.getCommon().getObjectMapper().writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            try {
                Connection connection = factory.createConnection();
                connection.start();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic(MQEasyCommon.MQEASY_GLOBAL_TOPIC);
                MessageProducer producer = session.createProducer(topic);
                if(message.getId().isEmpty())
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
