package me.jasperedits.bedutils;

import lombok.Getter;
import me.jasperedits.bedutils.commands.BedCommand;
import me.jasperedits.bedutils.listeners.BedEvent;
import me.jasperedits.bedutils.utils.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BedUtilsPlugin extends JavaPlugin {

    private static BedUtilsPlugin instance;
    public ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager();

        getCommand("bed").setExecutor(new BedCommand());
        getServer().getPluginManager().registerEvents(new BedEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BedUtilsPlugin getInstance() {
        return instance;
    }
}
