package me.jasperedits.bedutils.utilities;

        import org.bukkit.ChatColor;

public class StringUtilities {
    public static String stripColorCodes(final String m) {
        return ChatColor.translateAlternateColorCodes('&', m);
    }
}
