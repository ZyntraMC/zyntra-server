package mc.zyntra.match.listener;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.controller.ArenaController;
import mc.zyntra.arena.controller.sub.ArenaEndController;
import mc.zyntra.arena.enums.GameState;
import mc.zyntra.arena.player.Gamer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ArenaListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getEntity();
        ArenaController arenaController = Skywars.getInstance().getArenaController();
        Arena arena = arenaController.getArenaByPlayer(dead);
        if (arena == null) return;

        Gamer deadGamer = arena.getGamerController().getGamer(dead);
        if (deadGamer == null) return;

        arena.getPlayers().remove(dead);
        arena.getSpectators().add(dead);

        deadGamer.setSpectator(true);
        dead.setGameMode(GameMode.SPECTATOR);

        event.setDeathMessage(null);

        for (Player player : arena.getPlayers()) {
            player.sendMessage("§c" + dead.getName() + " morreu!");
        }

        if (arena.getPlayers().size() == 1) {
            Player winner = arena.getPlayers().get(0);
            winner.sendMessage("§aVocê venceu a partida!");
            new ArenaEndController(arena).endGame();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ArenaController arenaController = Skywars.getInstance().getArenaController();
        Arena arena = arenaController.getArenaByPlayer(player);
        if (arena == null) return;

        if (arena.getState() != GameState.INGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPvPDamage(EntityDamageByEntityEvent event) {
        ArenaController arenaController = Skywars.getInstance().getArenaController();
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();


        Arena arena = arenaController.getArenaByPlayer(damaged);
        if (arena == null) return;

        if (arena.getState() != GameState.INGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ArenaController arenaController = Skywars.getInstance().getArenaController();
        Player player = event.getPlayer();
        Arena arena = arenaController.getArenaByPlayer(player);
        if (arena != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ArenaController arenaController = Skywars.getInstance().getArenaController();
        Player player = event.getPlayer();
        Arena arena = arenaController.getArenaByPlayer(player);
        if (arena != null) {
            event.setCancelled(true);
        }
    }
}
