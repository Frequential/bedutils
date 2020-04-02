package me.jasperedits.bedutils.listeners;

import me.jasperedits.bedutils.BedUtilsPlugin;
import me.jasperedits.bedutils.utilities.StringUtilities;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class BedListener implements Listener {

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        String world = event.getPlayer().getWorld().getName();
        if (BedUtilsPlugin.getInstance().getConfig().getBoolean("modules.betterSleep.enabled"))
        if (Bukkit.getWorld(world).getTime() > 12542
                || Bukkit.getWorld(world).isThundering()) {
            float sleepingPlayers = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isSleeping() || event.getPlayer() == player) {
                    sleepingPlayers++;
                }
            }
            float sleepingPlayersPercentage = sleepingPlayers / Bukkit.getOnlinePlayers().size() * 100;
            if (sleepingPlayersPercentage == 100) {
                if (BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.lonelyPlayer").equals("0")) return;
                Bukkit.broadcastMessage(StringUtilities.stripColorCodes(BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.lonelyPlayer")));
                return;
            }
            if (sleepingPlayersPercentage >= BedUtilsPlugin.getInstance().getConfig().getInt("modules.betterSleep.playerPercentage")) {
                Bukkit.getWorld(world).setTime(0);
                if (BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.success").equals("0")) return;
                Bukkit.broadcastMessage(StringUtilities.stripColorCodes(BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.success")));
                return;
            }
            if (BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.moreNeeded").equals("0")) return;
            Bukkit.broadcastMessage(StringUtilities.stripColorCodes(BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.moreNeeded")
                    .replaceAll("%sleeping%", String.valueOf((int) sleepingPlayers))
                    .replaceAll("%connected%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                    .replaceAll("%sleepingPercent%", String.valueOf(Math.round(sleepingPlayersPercentage)))
                    .replaceAll("%percentNeeded%", String.valueOf(BedUtilsPlugin.getInstance().getConfig().getInt("modules.betterSleep.playerPercentage")))));
        } else {
            if (BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.youCantSleepNow").equals("0")) return;
            event.getPlayer().sendMessage(StringUtilities.stripColorCodes(BedUtilsPlugin.getInstance().getConfig().getString("modules.betterSleep.messages.youCantSleepNow")));
        }
    }
}
