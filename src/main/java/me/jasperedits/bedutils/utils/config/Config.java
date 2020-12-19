package me.jasperedits.bedutils.utils.config;

import lombok.Getter;
import lombok.Setter;
import me.jasperedits.bedutils.BedUtilsPlugin;
import me.jasperedits.bedutils.utils.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class Config {
    private final String fileName;
    @Setter
    private FileConfiguration fileConfiguration;
    private final File file;

    /**
     * @param fileName the name of the configuration file
     */
    public Config(String fileName) {
        this.fileName = fileName;
        this.file = new File(BedUtilsPlugin.getInstance().getDataFolder(), fileName + ".yml");

        if (!file.exists())
            BedUtilsPlugin.getInstance().saveResource(fileName + ".yml", false);

        if (file.length() == 0)
            BedUtilsPlugin.getInstance().getServer().getConsoleSender().sendMessage(StringUtils.coloredMessage("&cERROR &7(BedUtils) &8> &cConfig file &3\"" + fileName + ".yml\" &cis empty, things might not work."));

        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public boolean delete() {
        return file.delete();
    }

}
