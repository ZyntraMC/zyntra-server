package mc.zyntra.match.listener;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.api.actionbar.ActionBarAPI;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.room.Room;
import mc.zyntra.util.Platform;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.generator.player.fight.FightLoader;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        Player player = e.getEntity();
        EntityDamageEvent entityDamageEvent = player.getLastDamageCause();
        MatchPlayer gamePlayer = Platform.getPlayerLoader().get(player);
        Room room = Leading.getInstance().getRoom();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();

        room.getPlayers().remove(player);
        room.addSpec(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                player.teleport(player.getKiller().getLocation());
            }
        }.runTaskLater(Leading.getInstance(), 1L);

        if (player.getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            MatchPlayer killerPlayer = Platform.getPlayerLoader().get(killer);

            killerPlayer.getStatistics().addKill();
            killerPlayer.getStatistics().addCoins(1);

            Bukkit.getOnlinePlayers().forEach(players -> {
                ActionBarAPI.send(players, "§eRestam §b" + room.getPlayers().size() + "§e jogadores vivos.");
            });
            ZyntraPlayer killerAPlayer = Core.getAccountController().get(killer.getUniqueId());
            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            Bukkit.broadcastMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §efoi morto por §7" + killerAplayer.getTagHandler().getSelectedTag().getColor() + killer.getName() + "§e.");
            Core.getDataStatus().save(killerPlayer.getStatistics());
            player.teleport(killer.getLocation());
            //GameStatistics statistics = Core.getDataStatus().load(killer.getUniqueId(), StatisticsType.SW_SOLO);
        } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (FightLoader.inCombat(player)) {
                Player killer = e.getEntity().getKiller();
                MatchPlayer killerPlayer = Platform.getPlayerLoader().get(killer);

                killerPlayer.getStatistics().addKill();
                killerPlayer.getStatistics().addCoins(1);

                Bukkit.getOnlinePlayers().forEach(players -> {
                    ActionBarAPI.send(players, "§eRestam §b" + room.getPlayers().size() + "§e jogadores vivos.");
                });
                ZyntraPlayer killerAPlayer = Core.getAccountController().get(killer.getUniqueId());
                killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                Bukkit.broadcastMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §efoi morto por §7" + killerAplayer.getTagHandler().getSelectedTag().getColor() + killer.getName() + "§e.");
                Core.getDataStatus().save(killerPlayer.getStatistics());
                player.teleport(killer.getLocation());
            }
        } else if (damageCause == EntityDamageEvent.DamageCause.VOID) {
            if (FightLoader.inCombat(player)) {
                Player killer = e.getEntity().getKiller();
                MatchPlayer killerPlayer = Platform.getPlayerLoader().get(killer);

                killerPlayer.getStatistics().addKill();
                killerPlayer.getStatistics().addCoins(1);

                Bukkit.getOnlinePlayers().forEach(players -> {
                    ActionBarAPI.send(players, "§eRestam §b" + room.getPlayers().size() + "§e jogadores vivos.");
                });
                ZyntraPlayer killerAPlayer = Core.getAccountController().get(killer.getUniqueId());
                killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                Bukkit.broadcastMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §efoi morto por §7" + killerAplayer.getTagHandler().getSelectedTag().getColor() + killer.getName() + "§e.");
                Core.getDataStatus().save(killerPlayer.getStatistics());
                player.teleport(killer.getLocation());
            } else {
                Bukkit.getOnlinePlayers().forEach(players -> {
                    ActionBarAPI.send(players, "§eRestam §b" + room.getPlayers().size() + "§e jogadores vivos.");
                });
                player.teleport(room.getPlayers().get(0).getLocation());
                Bukkit.broadcastMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §emorreu sozinho.");
            }
        } else {
            if (FightLoader.inCombat(player)) {
                Player killer = e.getEntity().getKiller();
                MatchPlayer killerPlayer = Platform.getPlayerLoader().get(killer);

                killerPlayer.getStatistics().addKill();
                killerPlayer.getStatistics().addCoins(1);

                Bukkit.getOnlinePlayers().forEach(players -> {
                    ActionBarAPI.send(players, "§eRestam §b" + room.getPlayers().size() + "§e jogadores vivos.");
                });
                ZyntraPlayer killerAPlayer = Core.getAccountController().get(killer.getUniqueId());
                killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                Bukkit.broadcastMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §efoi morto por §7" + killerAplayer.getTagHandler().getSelectedTag().getColor() + killer.getName() + "§e.");
                Core.getDataStatus().save(killerPlayer.getStatistics());
                player.teleport(killer.getLocation());
            } else {
                Bukkit.getOnlinePlayers().forEach(players -> {
                    ActionBarAPI.send(players, "§eRestam §b" + room.getPlayers().size() + "§e jogadores vivos.");
                });
                player.teleport(room.getPlayers().get(0).getLocation());
                Bukkit.broadcastMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §emorreu sozinho.");
            }
        }
        gamePlayer.getStatistics().addDeath();
        gamePlayer.getStatistics().addMatch();
        Core.getDataStatus().save(gamePlayer.getStatistics());
        room.check();
    }
}
