package top.jingwenmc.mqeasy.bungee;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import top.jingwenmc.mqeasy.bungee.configuration.BungeeConfigurationManager;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.broker.MQEasyBrokerService;
import top.jingwenmc.mqeasy.common.configuration.BrokerConfigurationInfo;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationManager;
import top.jingwenmc.mqeasy.common.messaging.MQEasyMessenger;
import top.jingwenmc.mqeasy.common.platform.PlatformInfo;
import top.jingwenmc.mqeasy.common.platform.PlatformType;

public class MQEasyBungee extends Plugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("Loading Configuration...");
        try {
            //load configuration and set logger
            MQEasyCommon.getCommon().setLogger(getLogger());
            ConfigurationManager configurationManager = new BungeeConfigurationManager();
            configurationManager.validateVersion();
            MQEasyCommon.getCommon().setPlatformInfo(new PlatformInfo(PlatformType.BUNGEE
                    , configurationManager.loadConfiguration()));
        }catch (Throwable t) {
            t.printStackTrace();
            System.err.println("===============[MQEasy-Init-Error]===============");
            System.err.println("Error loading configuration.");
            System.err.println("Please check configuration file.");
            System.err.println("Plugin will not enable.");
            System.err.println("===============[MQEasy-Init-Error]===============");
            onDisable();
            return;
        }
        if(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getBrokerConfiguration().isEnabled()) {
            getLogger().info("Launching broker service...");
            try {
                //try to launch broker
                BrokerConfigurationInfo brokerInfo = MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getBrokerConfiguration();
                MQEasyBrokerService.launchBroker(brokerInfo.getIpport());
            }catch (Throwable t) {
                t.printStackTrace();
                System.err.println("===============[MQEasy-Init-Error]===============");
                System.err.println("Error launching broker.");
                System.err.println("Please check configuration file or your connection.");
                System.err.println("Plugin will not enable.");
                System.err.println("===============[MQEasy-Init-Error]===============");
                onDisable();
                return;
            }
        }
        getLogger().info("Connecting to broker service...");
        try {
            //try to connect broker
            MQEasyCommon.getCommon().setMessenger(new MQEasyMessenger(MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getIpport()));
            MQEasyCommon.getCommon().getMessenger().initListener();
        }catch (Throwable t) {
            t.printStackTrace();
            System.err.println("===============[MQEasy-Init-Error]===============");
            System.err.println("Error connecting broker.");
            System.err.println("Please check configuration file or your connection.");
            System.err.println("Plugin will not enable.");
            System.err.println("===============[MQEasy-Init-Error]===============");
            onDisable();
            return;
        }
        getLogger().info("Finalizing...");
        MQEasyCommon.getCommon().setOnlineValidator(player -> {
            boolean online = false;
            for(ProxiedPlayer player1 : getProxy().getPlayers()) {
                if(player1.getName().equals(player))online = true;
            }
            return online;
        });
        MQEasyCommon.getCommon().setLoaded(true);
        getLogger().info("Load Complete!");
        getLogger().info("+++++++++++++++[MQEasy-Load-Complete]+++++++++++++++");
        getLogger().info("MQEasy Load Complete!");
        getLogger().info("Version:"+this.getDescription().getVersion());
        getLogger().info("Authors:"+ this.getDescription().getAuthor());
        getLogger().info("Github:"+ "https://github.com/jingwenMC/MQEasy");
        getLogger().info("Running on BungeeCord");
        getLogger().info("+++++++++++++++[MQEasy-Load-Complete]+++++++++++++++");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabling Plugin...");
        if(MQEasyCommon.getCommon().isLoaded()) {
            try {
                MQEasyCommon.getCommon().getMessenger().stop();
                MQEasyBrokerService.stopIfLaunched();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("===============[MQEasy-Stop-Error]===============");
                System.err.println("Error stopping plugin.");
                System.err.println("This error shouldn't exist.");
                System.err.println("===============[MQEasy-Stop-Error]===============");
            }
        }
        MQEasyCommon.getCommon().setLoaded(false);
        getLogger().info("Disable Complete!");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getLogger().info("+++++++++++++++[MQEasy-Disable-Complete]+++++++++++++++");
        getLogger().info("MQEasy Disable Complete!");
        getLogger().info("Version:"+this.getDescription().getVersion());
        getLogger().info("Authors:"+ this.getDescription().getAuthor());
        getLogger().info("Github:"+ "https://github.com/jingwenMC/MQEasy");
        getLogger().info("Running on BungeeCord");
        getLogger().info("+++++++++++++++[MQEasy-Disable-Complete]+++++++++++++++");
    }

    @Getter
    private static MQEasyBungee instance;
}
