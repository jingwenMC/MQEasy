package top.jingwenmc.mqeasy.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import top.jingwenmc.mqeasy.bukkit.configuration.BukkitConfigurationManager;
import top.jingwenmc.mqeasy.common.MQEasyCommon;
import top.jingwenmc.mqeasy.common.configuration.ConfigurationManager;
import top.jingwenmc.mqeasy.common.messaging.MQEasyMessenger;
import top.jingwenmc.mqeasy.common.platform.PlatformInfo;
import top.jingwenmc.mqeasy.common.platform.PlatformType;

import javax.jms.JMSException;

public final class MQEasyBukkit extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("Loading Configuration...");
        try {
            //load configuration and set logger
            saveDefaultConfig();
            reloadConfig();
            MQEasyCommon.getCommon().setLogger(getLogger());
            ConfigurationManager configurationManager = new BukkitConfigurationManager();
            MQEasyCommon.getCommon().setPlatformInfo(new PlatformInfo(PlatformType.BUKKIT
                    , configurationManager.loadConfiguration()));
        }catch (Throwable t) {
            t.printStackTrace();
            System.err.println("===============[MQEasy-Init-Error]===============");
            System.err.println("Error loading configuration.");
            System.err.println("Please check configuration file.");
            System.err.println("Plugin will not enable.");
            System.err.println("===============[MQEasy-Init-Error]===============");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Connecting to broker service...");
        try {
            //try to connect broker
            MQEasyCommon.getCommon().setMessenger(new MQEasyMessenger(
                    MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getConnectionConfiguration()
                    , MQEasyCommon.getCommon().getPlatformInfo().getConfigurationInfo().getPassword()));
        }catch (Throwable t) {
            t.printStackTrace();
            System.err.println("===============[MQEasy-Init-Error]===============");
            System.err.println("Error connecting broker.");
            System.err.println("Please check configuration file or your connection.");
            System.err.println("Plugin will not enable.");
            System.err.println("===============[MQEasy-Init-Error]===============");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Finalizing...");
        MQEasyCommon.getCommon().setOnlineValidator(player -> {
            boolean online = false;
            for(Player player1 : Bukkit.getOnlinePlayers()) {
                if(player1.getName().equals(player))online = true;
            }
            return online;
        });
        MQEasyCommon.getCommon().setLoaded(true);
        getLogger().info("Load Complete!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabling Plugin...");
        if(MQEasyCommon.getCommon().isLoaded()) {
            try {
                MQEasyCommon.getCommon().getMessenger().stop();
            } catch (JMSException e) {
                e.printStackTrace();
                System.err.println("===============[MQEasy-Stop-Error]===============");
                System.err.println("Error stopping plugin.");
                System.err.println("This error shouldn't exist.");
                System.err.println("===============[MQEasy-Stop-Error]===============");
            }
        }
        MQEasyCommon.getCommon().setLoaded(false);
    }

    @Getter
    private static MQEasyBukkit instance;
}
