package me.jasperedits.bedutils.listeners;

import me.jasperedits.bedutils.BedUtilsPlugin;
import me.jasperedits.bedutils.utils.MathUtils;
import me.jasperedits.bedutils.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedEvent implements Listener {

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        FileConfiguration messages = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("messages").getFileConfiguration();
        FileConfiguration preferences = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("config").getFileConfiguration();

        Player player = event.getPlayer();
        World world = player.getWorld();
        World.Environment dimension = event.getPlayer().getWorld().getEnvironment();

        // Checks if you are in the right dimension.
        if (dimension != World.Environment.NORMAL) {
            return;
        }

        // Checks if you are too far away.
        if (!MathUtils.isBedReachable(player, event.getBed())) {
            if (!messages.getString("betterBeds.failureFarAway").isEmpty()) {
                player.sendMessage(StringUtils.coloredMessage(messages.getString("betterBeds.failureFarAway")));
            }
            event.setCancelled(true);
            return;
        }

        // Checks if if's the right time or is thundering.
        if (!((world.getTime() > preferences.getInt("preferences.betterBeds.startingTicksToSleep") && world.getTime() < preferences.getInt("preferences.betterBeds.endingTicksToSleep")) || world.isThundering())) {
            if (!messages.getString("betterBeds.failureWrongTime").isEmpty()) {
                player.sendMessage(StringUtils.coloredMessage(messages.getString("betterBeds.failureWrongTime")));
            }
            event.setCancelled(true);
            return;
        }

        // Checks if there's only one player online.
        if (Bukkit.getOnlinePlayers().size() == 1) {
            if (preferences.getBoolean("preferences.betterBeds.changeIfAlone")) {
                world.setTime(0);
                world.setThundering(false);
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getString("betterBeds.dayPassed")));
                return;
            }
            if (!messages.getString("betterBeds.alone").isEmpty()) {
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getString("betterBeds.alone")));
                return;
            }
            return;
        }

        // How many players are sleeping?
        int sleepingPlayers = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.isSleeping() || event.getPlayer() == onlinePlayer) {
                sleepingPlayers++;
            }
        }

        int sleepingPlayersPercentage = Math.round((float) sleepingPlayers / (float) Bukkit.getOnlinePlayers().size() * 100);

        if (sleepingPlayersPercentage >= preferences.getInt("preferences.betterBeds.percentageNeeded")) {
            world.setTime(0);
            world.setThundering(false);
            if (!messages.getString("betterBeds.dayPassed").isEmpty())
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getString("betterBeds.dayPassed")));
        } else {
            if (!messages.getString("betterBeds.moreNeeded").isEmpty()) {
                Bukkit.broadcastMessage(StringUtils.coloredMessage(messages.getString("betterBeds.moreNeeded")
                        .replaceAll("%sleeping%", String.valueOf(sleepingPlayers))
                        .replaceAll("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                        .replaceAll("%sleepingPercentage%", String.valueOf(sleepingPlayersPercentage))
                        .replaceAll("%percentageNeeded%", String.valueOf(preferences.getInt("preferences.betterBeds.percentageNeeded")))));
                event.setCancelled(true);
            }
        }
    }

}
