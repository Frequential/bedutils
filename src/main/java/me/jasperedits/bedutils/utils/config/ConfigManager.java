package me.jasperedits.bedutils.utils.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ConfigManager {
    private Map<String, Config> configs = new ConcurrentHashMap<>();

    public ConfigManager() {
        configs.put("config", new Config("config"));
        configs.put("messages", new Config("messages"));
    }

    public FileConfiguration getConfig(String config) {
        return configs.get(config).getFileConfiguration();
    }

}

