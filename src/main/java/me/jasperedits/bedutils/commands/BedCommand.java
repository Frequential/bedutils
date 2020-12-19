package me.jasperedits.bedutils.commands;

import me.jasperedits.bedutils.BedUtilsPlugin;
import me.jasperedits.bedutils.utils.StringUtils;
import me.jasperedits.bedutils.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedCommand implements CommandExecutor {
    Config messages = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("messages");
    Config preferences = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("config");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(preferences.getFileConfiguration().getString("permissions.reload"))
                &&args.length > 0
                && args[0].matches("(?i)(reload|recargar)")) {
            Config preferences = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("config");

            messages.reload();
            preferences.reload();
            if (!messages.getFileConfiguration().getString("bedCommand.reload").isEmpty())
                sender.sendMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("bedCommand.reload")));
            return true;
        } else if (sender instanceof Player && args.length > 0 && args[0].matches("(?i)(able|posible|dormir)")) {
            Player player = Bukkit.getServer().getPlayer(sender.getName());

            if (!(player.getWorld().getTime() > 12541 && player.getWorld().getTime() < 23458) || player.getWorld().isThundering()) {
                if (!messages.getFileConfiguration().getString("bedCommand.sleepingNotPossible").isEmpty())
                    sender.sendMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("bedCommand.sleepingNotPossible")));
            } else {
                if (!messages.getFileConfiguration().getString("bedCommand.sleepingPossible").isEmpty())
                    sender.sendMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("bedCommand.sleepingPossible")));
            }
            return true;
        } else {
            if (!messages.getFileConfiguration().getString("bedCommand.arguments").isEmpty())
                sender.sendMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("bedCommand.arguments")));
            return false;
        }
    }
}
