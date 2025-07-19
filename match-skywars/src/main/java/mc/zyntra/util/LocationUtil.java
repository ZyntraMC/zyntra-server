package mc.zyntra.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static Location parseLocation(String input) {
        if (input == null || input.isEmpty()) return null;
        String[] parts = input.split(",");
        if (parts.length < 6) return null;

        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }

    public static String serializeLocation(Location loc) {
        return String.format(
                "%s,%.2f,%.2f,%.2f,%.1f,%.1f",
                loc.getWorld().getName(),
                loc.getX(), loc.getY(), loc.getZ(),
                loc.getYaw(), loc.getPitch()
        );
    }

}
