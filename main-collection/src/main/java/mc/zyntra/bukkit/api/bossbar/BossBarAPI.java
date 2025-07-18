package mc.zyntra.bukkit.api.bossbar;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.bossbar.entity.DragonBoss;
import mc.zyntra.bukkit.api.bossbar.entity.WitherBoss;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.Via;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarAPI implements Listener {

    private static final Map<UUID, BossBarEntity> entityMap = new HashMap<>();

    public static void setBar(Player player, String message, float percent) {
        Preconditions.checkNotNull(message, "Message cannot be null.");
        Preconditions.checkArgument(percent >= 0F && percent <= 100F, "Health must be between 0 and 100.");
        BossBarEntity entity = entityMap.computeIfAbsent(player.getUniqueId(), v -> createBoss(player));
        if (entity != null && !entity.hasTask()) {
            boolean update = false;
            update |= entity.setTitle(message);
            update |= entity.setHealth(percent);
            if (update)
                entity.update();
        }
    }

    public static void setBar(Player player, String message, int period) {
        Preconditions.checkNotNull(message, "Message cannot be null.");
        Preconditions.checkArgument(period > 0, "Period must be greater than 0.");
        BossBarEntity entity = entityMap.computeIfAbsent(player.getUniqueId(), v -> createBoss(player));
        if (entity != null && !entity.hasTask()) {
            entity.setTitle(message);
            entity.setHealth(100F);
            entity.update();
            entity.startTask(new BukkitRunnable() {
                float health = 100F;

                public void run() {
                    health -= (100F / period);
                    if (health > 1F) {
                        entity.setTitle(message);
                        entity.setHealth(health);
                        entity.update();
                    } else {
                        removeBar(player);
                    }
                }
            });
        }
    }

    public static boolean hasBar(Player player) {
        return entityMap.containsKey(player.getUniqueId());
    }

    public static void removeBar(Player player) {
        if (entityMap.containsKey(player.getUniqueId())) {
            BossBarEntity entity = entityMap.remove(player.getUniqueId());
            if (entity.hasTask())
                entity.cancelTask();
            entity.remove();
        }
    }

    private static BossBarEntity createBoss(Player player) {
        return Via.getAPI().getPlayerVersion(player.getUniqueId()) >= 47
                ? new WitherBoss(player)
                : new DragonBoss(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMoveUpdate(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        BossBarEntity entity = entityMap.get(player.getUniqueId());
        if (entity != null) {
            entity.move(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Bukkit.getScheduler().runTaskLater(BukkitMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                BossBarEntity entity = entityMap.get(player.getUniqueId());
                if (entity != null) {
                    entity.move(event);
                }
            }
        }, 2L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        BossBarEntity entity = entityMap.get(player.getUniqueId());
        if (entity != null) {
            entity.remove();
            entity.spawn();
            entity.update();
        }
    }

}
