package mc.zyntra.arena.controller;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.game.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaEndController {

    private final Arena arena;

    public ArenaEndController(Arena arena) {
        this.arena = arena;
    }

    public void endGame() {
        arena.setState(GameState.ENDED);
        arena.setTime(15);

        new BukkitRunnable() {
            @Override
            public void run() {
                int time = arena.getTime() - 1;
                arena.setTime(time);

                for (Player winner : arena.getPlayers()) {
                    winner.sendMessage("§aVocê venceu a partida!");
                    winner.playSound(winner.getLocation(), Sound.FIREWORK_LAUNCH, 5.0f, 5.0f);
                }

                for (Player spectator : arena.getSpectators()) {
                    spectator.sendMessage("§cNão foi dessa vez!!");
                }

                if (time <= 0) {
                    arena.reset();
                    cancel();
                }
            }
        }.runTaskTimer(Skywars.getInstance(), 0l, 20L);
    }
}
