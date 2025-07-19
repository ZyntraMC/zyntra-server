package mc.zyntra.arena.controller;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.game.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaStartController {

    private final Arena arena;

    public ArenaStartController(Arena arena) {
        this.arena = arena;
    }

    public void startCountDown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (arena.getState()) {
                    case WAITING:
                        if (arena.getPlayers().size() >= arena.getMinPlayers()) {
                            arena.setTime(20);
                            arena.setState(GameState.STARTING);
                            sendMessage("§eA partida irá começar em §b20§e segundos!");
                        }
                        break;

                    case STARTING:
                        int time = arena.getTime() - 1;
                        arena.setTime(time);

                        if (arena.getPlayers().size() < arena.getMinPlayers()) {
                            arena.setState(GameState.WAITING);
                            sendMessage("§cA partida foi cancelada por falta de jogadores.");
                            cancel();
                            return;
                        }

                        if (time <= 10) {
                            sendMessage("§eA partida irá começar em §b" + time + "§e segundos!");
                        }

                        if (time <- 0) {
                            arena.setState(GameState.INGAME);
                            cancel();
                        }
                        break;

                    default:
                        cancel();
                        break;
                }
            }
        }.runTaskTimer(Skywars.getInstance(), 0L, 20L);
    }

    private void sendMessage(String message) {
        for (Player player : arena.getPlayers()) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 5.0f, 5.0f);
            player.sendMessage(message);
        }
    }
}
