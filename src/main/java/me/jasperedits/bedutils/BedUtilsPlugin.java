package me.jasperedits.bedutils;

import me.jasperedits.bedutils.listeners.BedListener;
import me.jasperedits.bedutils.utilities.StringUtilities;
import org.bukkit.plugin.java.JavaPlugin;

public final class BedUtilsPlugin extends JavaPlugin {

    private static BedUtilsPlugin instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        getServer().getConsoleSender().sendMessage(StringUtilities.stripColorCodes(getInstance().getConfig().getString("enableMessage")));
        getServer().getPluginManager().registerEvents(new BedListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(StringUtilities.stripColorCodes(getInstance().getConfig().getString("disableMessage")));
    }

    public static BedUtilsPlugin getInstance() {
        return instance;
    }

}
