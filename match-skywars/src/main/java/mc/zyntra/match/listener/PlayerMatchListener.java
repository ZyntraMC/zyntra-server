package mc.zyntra.match.listener;

import mc.zyntra.room.Room;
import mc.zyntra.stage.MatchStage;
import mc.zyntra.Leading;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.util.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerMatchListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreatureSpawnListener(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player);
            if (matchPlayer.getRoom().getStage() != MatchStage.EM_JOGO) e.setCancelled(true);
            else {
                int matchTime = Leading.getInstance().getRoom().getTime();
                if (matchTime >= 596) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void inVoid(PlayerMoveEvent event) {
        if (event.getTo().getY() < 0) {
            if (Leading.getInstance().getRoom().getPlayers().contains(event.getPlayer())) {
                event.getPlayer().closeInventory();
            } else {
                event.getPlayer().teleport(Leading.getInstance().getRoom().getPlayers().get(0).getLocation());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player);
        if (matchPlayer.getRoom().getStage() != MatchStage.EM_JOGO || matchPlayer.getRoom().getSpectators().contains(player)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player);
        if (matchPlayer.getRoom().getStage() != MatchStage.EM_JOGO || matchPlayer.getRoom().getSpectators().contains(player)) e.setCancelled(true);
    }


    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        if (matchPlayer.getRoom().getStage() != MatchStage.EM_JOGO || matchPlayer.getRoom().getSpectators().contains(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Room room = Leading.getInstance().getRoom();

        if (!room.getPlayers().contains(player)) event.setCancelled(true);
    }
}
