package mc.zyntra.controllers;

import lombok.Getter;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.structure.Island;
import mc.zyntra.arena.structure.cage.CageConstructor;
import mc.zyntra.player.Gamer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class GamerController {

    private final Arena arena;
    private final Map<UUID, Gamer> gamers = new HashMap<UUID, Gamer>();

    public GamerController(Arena arena) {
        this.arena = arena;
    }

    public Gamer getGamer(Player player) {
        return gamers.get(player.getUniqueId());
    }

    public void createGamer(Player player) {
        Gamer gamer = new Gamer(player, arena);
        gamers.put(player.getUniqueId(), gamer);
    }

    public void assignIslands() {
        List<Island> availableIslands = new ArrayList<Island>(arena.getIslands());

        int index = 0;
        for (Player player : arena.getPlayers()) {
            if (index >= availableIslands.size()) break;

            Island island = availableIslands.get(index++);
            Gamer gamer = getGamer(player);
            if (gamer == null) continue;

            gamer.setIsland(island);
            player.teleport(island.getSpawn());
            CageConstructor.build(island.getCageOrigin(), Material.GLASS);
        }
    }

    public void setSpectator(Player player) {
        Gamer gamer = getGamer(player);
        if (gamer != null) {
            gamer.setSpectator(true);
        }
    }

    public void reset() {
        for (Gamer gamer : gamers.values()) {
            gamer.reset();
        }
        gamers.clear();
    }
}
