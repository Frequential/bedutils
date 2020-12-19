package me.jasperedits.bedutils.utils;

import org.bukkit.ChatColor;

public class StringUtils {
    /**
     * @param string the string you want to translate.
     * @return a color-translated string.
     */
    public static String coloredMessage(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
