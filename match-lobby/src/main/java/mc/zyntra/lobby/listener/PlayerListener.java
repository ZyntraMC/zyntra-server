package mc.zyntra.lobby.listener;

import mc.zyntra.Main;
import mc.zyntra.cMain;
import mc.zyntra.collectibles.parser.CollectiblePlayer;
import mc.zyntra.general.Constant;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.lobby.generator.user.User;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.bukkit.api.title.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        cMain.getPlayerLoader().create(new CollectiblePlayer(event.getPlayer().getUniqueId()));
        Main.getInstance().getUserParser().create(new User(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        player.setExp(0);
        player.setFireTicks(0);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setWalkSpeed(0.25f);
        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));

        player.setAllowFlight(zyntraPlayer.hasGroupPermission(Group.GOLD) && zyntraPlayer.getConfiguration().isEnabled(ConfigType.FLY));
        player.setFlying(zyntraPlayer.hasGroupPermission(Group.GOLD) && zyntraPlayer.getConfiguration().isEnabled(ConfigType.FLY));

        ((CraftPlayer) player).getHandle().updateAbilities();

        Location location = Main.getInstance().getLocationFromConfig("spawn") == null ?
                Bukkit.getWorlds().get(0).getSpawnLocation() : Main.getInstance().getLocationFromConfig("spawn");
        player.teleport(zyntraPlayer.hasGroupPermission(Group.GOLD) ? location.clone().add(0, 2, 0) : location.clone().add(0, 1, 0));

        for (ZyntraPlayer onlinePlayer : Core.getAccountController().list()) {
            if (onlinePlayer.toPlayer() == null || !onlinePlayer.toPlayer().isOnline())
                return;

            if (!onlinePlayer.getConfiguration().isEnabled(ConfigType.PLAYERS_VISIBILITY))
                onlinePlayer.toPlayer().hidePlayer(player);

            if (!zyntraPlayer.getConfiguration().isEnabled(ConfigType.PLAYERS_VISIBILITY))
                player.hidePlayer(onlinePlayer.toPlayer());

            if (zyntraPlayer.hasGroupPermission(Group.GOLD) && !zyntraPlayer.getTagHandler().getSelectedTag().equals(Tag.MEMBRO))
                onlinePlayer.sendMessage(zyntraPlayer.getTagHandler().getSelectedTag().getColor()
                        + "[" + zyntraPlayer.getTagHandler().getSelectedTag().getName() + "] " +
                        player.getName() + " §6entrou neste lobby!");
        }

        Main.getInstance().getGeneral().handleScoreboard(player);
        Main.getInstance().getGeneral().handleInventory(player);

        // Title animation
        AtomicInteger i = new AtomicInteger(0);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || i.getAndIncrement() >= 60) {
                    cancel();
                    return;
                }

                TitleAPI.setTitle(player, Constant.FORMATTED_NAME, "§fSeja bem-vindo", 0, 3, 1);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Main.getInstance().getUserParser().remove(event.getPlayer().getUniqueId());
    }
}
