package top.jingwenmc.mqeasy.api.plugin;

import org.bukkit.plugin.Plugin;

import java.util.List;

public class BukkitMQEasyPluginInfo implements MQEasyPluginInfo{

    public BukkitMQEasyPluginInfo(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Plugin plugin;

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public List<String> getAuthors() {
        return plugin.getDescription().getAuthors();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getDescription() {
        return plugin.getDescription().getDescription();
    }

    @Override
    public String getWebsite() {
        return plugin.getDescription().getWebsite();
    }
}
