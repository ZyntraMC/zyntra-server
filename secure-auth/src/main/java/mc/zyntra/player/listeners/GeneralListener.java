package mc.zyntra.player.listeners;

import mc.zyntra.Secure;
import mc.zyntra.player.generator.User;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GeneralListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            Location location = Secure.getInstance().getLocationFromConfig("spawn") == null ?
                    Bukkit.getWorlds().get(0).getSpawnLocation() : Secure.getInstance().getLocationFromConfig("spawn");
            event.getEntity().teleport(location);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        User user = Secure.getInstance().getUserParser().get(player.getUniqueId());
        if (zyntraPlayer.hasGroupPermission(Group.ADMIN) && user.isBuildEnabled())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        User user = Secure.getInstance().getUserParser().get(player.getUniqueId());
        if (zyntraPlayer.hasGroupPermission(Group.ADMIN) && user.isBuildEnabled())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void spawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.DISPENSE_EGG
                && event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM
                && event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        User user = Secure.getInstance().getUserParser().get(player.getUniqueId());
        if (zyntraPlayer.hasGroupPermission(Group.ADMIN) && user.isBuildEnabled())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onLeaves(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAchievement(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        event.getEntity().remove();
    }
}
