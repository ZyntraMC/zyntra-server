package mc.zyntra.lobby.listener;

import mc.zyntra.Main;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.lobby.generator.user.User;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.lobby.npc.NPCController;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultListener implements Listener {

    private static final Set<Material> BLOCKED_BLOCKS = new HashSet<>(Arrays.asList(
            Material.CHEST, Material.TRAPPED_CHEST, Material.FURNACE, Material.BURNING_FURNACE,
            Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.BREWING_STAND,
            Material.ENCHANTMENT_TABLE, Material.ANVIL, Material.BEACON, Material.ENDER_CHEST,
            Material.JUKEBOX, Material.BEACON,

            //portas

            Material.WOOD_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.IRON_DOOR,

            // portoes

            Material.FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE,
            Material.ACACIA_FENCE, Material.DARK_OAK_FENCE
    ));

    @EventHandler
    public void jump(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Block slime = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        if (slime.getType() == Material.SLIME_BLOCK) {
            double multiplier = Main.getInstance().getConfig().getDouble("slime-jump-distance", 4.0);

            Vector direction = player.getLocation().getDirection().multiply(multiplier);
            Vector velocity = new Vector(direction.getX(), 1.0D, direction.getZ());

            player.setVelocity(velocity);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 66, 4));
            player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 5.0F, 5.0F);
        }
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            Location location = Main.getInstance().getLocationFromConfig("spawn") == null
                    ? Bukkit.getWorlds().get(0).getSpawnLocation()
                    : Main.getInstance().getLocationFromConfig("spawn");
            event.getEntity().teleport(location);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player shooter = (Player) arrow.getShooter();
                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(shooter.getUniqueId());
                Location hitLocation = arrow.getLocation();
                if (zyntraPlayer.hasCooldown("navigate")) {
                    shooter.sendMessage("§cAguarde para poder viajar novamente!");
                    arrow.remove();
                    shooter.getInventory().setItem(9, new ItemBuilder(Material.ARROW).build());
                    return;
                } else {
                    shooter.teleport(hitLocation);
                    zyntraPlayer.addCooldown("navigate", 3);
                    arrow.remove();
                    shooter.getInventory().setItem(9, new ItemBuilder(Material.ARROW).build());
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        User user = Main.getInstance().getUserParser().get(player.getUniqueId());
        if (zyntraPlayer.hasGroupPermission(Group.ADMIN) && user.isBuildEnabled())
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        User user = Main.getInstance().getUserParser().get(player.getUniqueId());
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
        User user = Main.getInstance().getUserParser().get(player.getUniqueId());
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        NPCController npcController = Main.getInstance().getNpcController();
        Player player = event.getPlayer();

        if (npcController != null) {
            npcController.knockbackPlayersNearNPCs(player);
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        if (BLOCKED_BLOCKS.contains(block.getType())) {
            Player player = e.getPlayer();
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
            User user = Main.getInstance().getUserParser().get(player.getUniqueId());

            if (!zyntraPlayer.hasGroupPermission(Group.CREATOR) || !user.isBuildEnabled()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInvetoryOpen(InventoryOpenEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        Player player = (Player) e.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        User user = Main.getInstance().getUserParser().get(player.getUniqueId());


        if (holder instanceof org.bukkit.block.BrewingStand || holder instanceof org.bukkit.block.Hopper ||
                holder instanceof org.bukkit.block.Chest) {
            if (!zyntraPlayer.hasGroupPermission(Group.CREATOR) || !user.isBuildEnabled()) {
                e.setCancelled(true);
            }
        }
    }
}
