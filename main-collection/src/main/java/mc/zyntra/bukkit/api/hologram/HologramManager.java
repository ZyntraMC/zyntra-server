package mc.zyntra.bukkit.api.hologram;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.*;

public class HologramManager {

    protected static double bukkitRange = NumberConversions.square(Bukkit.getViewDistance() << 4);

    @Getter
    private final Set<Hologram> globalHolograms = new HashSet<>();
    @Getter
    private final Map<Player, List<Hologram>> playerHolograms = new HashMap<>();

    public boolean canSpawn(Hologram h, Location b) {
        return h.getLocation().getWorld().equals(b.getWorld()) &&
                h.getLocation().distanceSquared(b) <= bukkitRange;
    }

    public void addGlobal(Hologram hologram) {
        if (globalHolograms.contains(hologram)) {
            for (Player player : hologram.getWorld().getPlayers()) {
                if (canSpawn(hologram, player.getLocation())) {
                    hologram.spawnTo(player);
                }
            }
        }
    }

    public void addPlayerHologram(Player player, Hologram hologram) {
        getPlayerHolograms(player).add(hologram);
    }

    public List<Hologram> getPlayerHolograms(Player player) {
        return playerHolograms.computeIfAbsent(player, v -> new ArrayList<>());
    }

    public void removePlayerHolograms(Player player) {
        globalHolograms.forEach(h -> h.despawnTo(player));
        playerHolograms.remove(player);
    }

}
