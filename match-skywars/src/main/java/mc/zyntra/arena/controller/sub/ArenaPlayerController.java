package mc.zyntra.arena.controller.sub;

import mc.zyntra.arena.Arena;
import mc.zyntra.arena.structure.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ArenaPlayerController {

    private final Arena arena;

    public ArenaPlayerController(Arena arena) {
        this.arena = arena;
    }

    public void addPlayer(Player player) {
        resetPlayer(player);

        Island island = arena.findAvailableIsland();
        if (island == null) {
            player.sendMessage("§cNão há ilhas disponíveis para você!");
            return;
        }

        Location spawn = island.getSpawn();
        if (spawn == null) {
            player.sendMessage("§cA sua ilha não tem um spawn configurado.");
            return;
        }

        island.setPlayer(player);
        arena.getPlayers().add(player);
        player.teleport(spawn);

        arena.getPlayers().forEach(p -> {
            if (!p.equals(player)) {
                p.sendMessage("§b" + player.getName() + " §eentrou na partida! (" +
                        arena.getPlayers().size() + "/" + arena.getMaxPlayers() + ")");
            }
        });
    }

    public void removePlayer(Player player) {
        resetPlayer(player);
        arena.getPlayers().remove(player);
        arena.getIslands().forEach(island -> {
            if (player.equals(island.getPlayer())) {
                island.setPlayer(null);
            }
        });
    }

    public void addSpectator(Player player) {
        resetPlayer(player);
        arena.getSpectators().add(player);
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
