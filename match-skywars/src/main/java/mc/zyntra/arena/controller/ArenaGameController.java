package mc.zyntra.arena.controller;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.game.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaGameController {

    private final Arena arena;

    public ArenaGameController(Arena arena) {
        this.arena = arena;
    }

    public void startGame() {
        arena.setState(GameState.INGAME);
        arena.setTime(0);

        for (Player player : arena.getPlayers()) {
            player.sendMessage("§aA partida começou!");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 5.0f, 5.0f);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                int time = arena.getTime() + 1;
                arena.setTime(time);

                if (arena.getPlayers().size() == 1) {
                    new ArenaEndController(arena).endGame();
                    cancel();
                }

                if (time == 300) {
                    //refill
                } else if (time == 360) {
                    //refill
                }
            }
        }.runTaskTimer(Skywars.getInstance(), 20L, 20L);
    }
}
