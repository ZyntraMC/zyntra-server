package mc.zyntra.arena.controller;

import mc.zyntra.arena.Arena;
import org.bukkit.entity.Player;

public class ArenaPlayerController {

    private final Arena arena;

    public ArenaPlayerController(Arena arena) {
        this.arena = arena;
    }

    public void addPlayer(Player player) {
        resetPlayer(player);
        arena.getPlayers().add(player);
        player.teleport(arena.getLobbyLocation());

        arena.getPlayers().forEach(p ->
                p.sendMessage("§b" + player.getName() + " §eentrou na partida! (" +
                        arena.getPlayers().size() + "/" + arena.getMaxPlayers() + ")")
        );
    }

    public void removePlayer(Player player) {
        resetPlayer(player);
        arena.getPlayers().remove(player);
    }

    public void addSpectator(Player player) {
        resetPlayer(player);
        arena.getSpectators().remove(player);
    }

    public void removeSpectator(Player player) {
        resetPlayer(player);
        arena.getSpectators().remove(player);
    }

    public void resetPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
    }
}
