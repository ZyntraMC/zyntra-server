package mc.zyntra.match.listener;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.controller.ArenaController;
import mc.zyntra.arena.controller.sub.ArenaEndController;
import mc.zyntra.arena.controller.sub.ArenaPlayerController;
import mc.zyntra.arena.controller.sub.ArenaStartController;
import mc.zyntra.arena.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AutoJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ArenaController arenaController = Skywars.getInstance().getArenaController();

        Bukkit.getScheduler().runTaskLater(Skywars.getInstance(), () -> {
            Arena arena = arenaController.getArenas().stream()
                    .filter(a -> !a.isFull() && a.getState() == GameState.WAITING)
                    .findFirst()
                    .orElse(null);

            if (arena == null) {
                player.sendMessage("§cNão há nenhuma arena disponível.");
                return;
            }

            if (!arena.getPlayers().contains(player)) {
                ArenaPlayerController playerController = new ArenaPlayerController(arena);
                playerController.addPlayer(player);
                arena.getGamerController().createGamer(player);

                player.sendMessage("§aVocê entrou automaticamente na arena §b" + arena.getName() + "§a!");
                arena.getPlayers().forEach(p -> {
                    if (!p.equals(player)) {
                        p.sendMessage("§b" + player.getName() + " §eentrou na arena! (" +
                                arena.getPlayers().size() + "/" + arena.getMaxPlayers() + ")");
                    }
                });

                // Inicia contagem se for o suficiente
                if (arena.getPlayers().size() >= arena.getMinPlayers() && arena.getState() == GameState.WAITING) {
                    new ArenaStartController(arena).startCountDown();
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ArenaController arenaController = Skywars.getInstance().getArenaController();
        Arena arena = arenaController.getArenaByPlayer(player);
        if (arena == null) return;

        ArenaPlayerController playerController = new ArenaPlayerController(arena);
        playerController.removePlayer(player);
        arena.getGamerController().removeGamer(player.getUniqueId());

        arena.getPlayers().forEach(p -> {
            p.sendMessage("§c" + player.getName() + " saiu da arena! (" +
                    arena.getPlayers().size() + "/" + arena.getMaxPlayers() + ")");
        });

        if (arena.getPlayers().size() == 1 && arena.getState() == GameState.INGAME) {
            new ArenaEndController(arena).endGame();
        }

        if (arena.getPlayers().isEmpty() && arena.getSpectators().isEmpty()) {
            arena.reset();
        }
    }
}
