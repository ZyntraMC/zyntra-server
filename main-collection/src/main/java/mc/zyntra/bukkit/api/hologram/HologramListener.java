package mc.zyntra.bukkit.api.hologram;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.List;

public class HologramListener implements Listener {

    private final HologramManager manager;

    public HologramListener() {
        this.manager = BukkitMain.getInstance().getHologramManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateHolograms(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.removePlayerHolograms(event.getPlayer());
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        Bukkit.getOnlinePlayers().forEach(this::updateHolograms);
    }

    private void updateHolograms(Player player) {
        Location playerLoc = player.getLocation();

        List<Hologram> holograms = manager.getPlayerHolograms()
                .getOrDefault(player, Collections.emptyList());

        if (!holograms.isEmpty()) {
            for (Hologram h : holograms) {
                if (manager.canSpawn(h, playerLoc)) {
                    h.spawnTo(player);
                } else {
                    h.despawnTo(player);
                }
            }
        }
        
        for (Hologram h : manager.getGlobalHolograms()) {
            if (manager.canSpawn(h, playerLoc)) {
                h.spawnTo(player);
            } else {
                h.despawnTo(player);
            }
        }
    }
}
