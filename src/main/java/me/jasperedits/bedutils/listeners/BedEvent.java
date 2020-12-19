package me.jasperedits.bedutils.listeners;

import me.jasperedits.bedutils.BedUtilsPlugin;
import me.jasperedits.bedutils.utils.MathUtils;
import me.jasperedits.bedutils.utils.StringUtils;
import me.jasperedits.bedutils.utils.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedEvent implements Listener {

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        Config messages = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("messages");
        Config preferences = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("config");

        Player player = event.getPlayer();
        World world = player.getWorld();
        World.Environment dimension = event.getPlayer().getWorld().getEnvironment();

        // Checks if you are in the right dimension.
        if (dimension != World.Environment.NORMAL) {
            return;
        }

        // Checks if you are too far away.
        if (!MathUtils.isBedReachable(player, event.getBed())) {
            if (!messages.getFileConfiguration().getString("betterBeds.failureFarAway").isEmpty()) {
                player.sendMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("betterBeds.failureFarAway")));
                return;
            }
            return;
        }

        // Checks if if's the right time or is thundering.
        if (!(world.getTime() > 12541 && world.getTime() < 23458) || world.isThundering()) {
            if (!messages.getFileConfiguration().getString("betterBeds.failureWrongTime").isEmpty()) {
                player.sendMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("betterBeds.failureWrongTime")));
                return;
            }
            return;
        }

        // Checks if there's only one player online.
        if (Bukkit.getOnlinePlayers().size() == 1) {
            if (preferences.getFileConfiguration().getBoolean("preferences.betterBeds.changeIfAlone")) {
                world.setTime(0);
                world.setThundering(false);
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("betterBeds.dayPassed")));
                return;
            }
            if (!messages.getFileConfiguration().getString("betterBeds.alone").isEmpty()) {
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("betterBeds.alone")));
                return;
            }
            return;
        }

        // How many players are sleeping?
        int sleepingPlayers = 0;
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (player.isSleeping() || event.getPlayer() == player) {
                sleepingPlayers++;
            }
        }

        int sleepingPlayersPercentage = sleepingPlayers / Bukkit.getOnlinePlayers().size() * 100;
        if (sleepingPlayersPercentage >= preferences.getFileConfiguration().getInt("preferences.betterBeds.percentageNeeded")) {
            world.setTime(0);
            world.setThundering(false);
            if (!messages.getFileConfiguration().getString("betterBeds.dayPassed").isEmpty())
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("betterBeds.dayPassed")));
        } else {
            if (!messages.getFileConfiguration().getString("betterBeds.moreNeeded").isEmpty()) {
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getFileConfiguration().getString("betterBeds.moreNeeded")
                        .replaceAll("%sleeping%", String.valueOf(sleepingPlayers))
                        .replaceAll("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replaceAll("%sleepingPercentage%", String.valueOf(sleepingPlayersPercentage))
                        .replaceAll("%percentageNeeded%", String.valueOf(preferences.getFileConfiguration().getInt("preferences.betterBeds.percentageNeeded")))));
            }
        }
    }

}
