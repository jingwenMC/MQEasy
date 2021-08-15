package top.jingwenmc.mqeasy.common.broker;

import org.apache.activemq.broker.BrokerService;
import top.jingwenmc.mqeasy.common.MQEasyCommon;

public class MQEasyBrokerService {

    private static BrokerService brokerService;

    private static boolean isLoaded = false;

    public static void launchBroker(String ipport) throws Exception {
        MQEasyCommon.debug("Launching Broker: tcp://"+ipport);
        if(isLoaded)throw new IllegalStateException("Broker Already Launched!");
        brokerService = new BrokerService();
        brokerService.setBrokerName("mqeasyemb");
        brokerService.addConnector("tcp://"+ipport);
        brokerService.start();
        while(true) {
            if(brokerService.isStarted())break;
        }
        isLoaded=true;
    }

    public static void stopIfLaunched() throws Exception {
        if(isLoaded)brokerService.stop();
    }
}
