package me.jasperedits.bedutils.utils;

import me.jasperedits.bedutils.BedUtilsPlugin;
import me.jasperedits.bedutils.utils.config.Config;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MathUtils {


    public static Config preferences = BedUtilsPlugin.getInstance().getConfigManager().getConfigs().get("config");
    private static final double XZ_DISTANCE = preferences.getFileConfiguration().getDouble("preferences.betterBeds.maximumXZDistanceToSleep");
    private static final double Y_DISTANCE = preferences.getFileConfiguration().getDouble("preferences.betterBeds.maximumYDistanceToSleep");

    public static boolean isBedReachable(Player player, Block bedBlock) {
        Location head = bedBlock.getLocation().clone().add(0.5, 0, 0.5);
        Location base = bedBlock.getRelative(bedBlock.getFace(bedBlock).getOppositeFace()).getLocation().add(0.5, 0, 0.5);
        Location max = Vector.getMaximum(head.toVector(), base.toVector()).toLocation(head.getWorld()).add(XZ_DISTANCE, Y_DISTANCE, XZ_DISTANCE);
        Location min = Vector.getMinimum(head.toVector(), base.toVector()).toLocation(head.getWorld()).subtract(XZ_DISTANCE, Y_DISTANCE, XZ_DISTANCE);
        Location loc = player.getLocation();

        return loc.getX() <= max.getX() && loc.getX() >= min.getX()
                && loc.getY() <= max.getY() && loc.getY() >= min.getY()
                && loc.getZ() <= max.getZ() && loc.getZ() >= min.getZ();
    }
}
