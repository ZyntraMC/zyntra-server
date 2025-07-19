package mc.zyntra.arena;

import lombok.Getter;
import lombok.Setter;
import lombok.var;
import mc.zyntra.arena.enums.ArenaType;
import mc.zyntra.arena.structure.Island;
import mc.zyntra.arena.controller.GamerController;
import mc.zyntra.arena.enums.GameState;
import mc.zyntra.arena.structure.loot.LootChest;
import mc.zyntra.arena.structure.loot.LootFactory;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Arena {

    private final String name;
    private ArenaType type;
    private GameState state = GameState.WAITING;

    private Location lobbyLocation;

    private int minPlayers;
    private int maxPlayers;

    private int time = 0;

    private final List<Island> islands = new ArrayList<Island>();
    private final List<Player> players = new ArrayList<Player>();
    private final List<Player> spectators = new ArrayList<Player>();

    private final List<Location> feastChests = new ArrayList<>();
    private final List<Location> miniFeastChests = new ArrayList<>();

    private final GamerController gamerController;

    public Arena(String name) {
        this.name = name;
        this.gamerController = new GamerController(this);
    }

    public void reset() {
        this.state = GameState.WAITING;
        this.time = 0;

        for (Island island : islands) {
            island.reset();
        }

        this.players.clear();
        this.spectators.clear();
    }

    public void addIsland(Island island) {
        this.islands.add(island);
    }

    public List<Location> getSpawnLocations() {
        List<Location> locations = new ArrayList<Location>();
        for (Island island : islands) {
            locations.add((Location) island.getSpawn());
        }
        return locations;
    }

    public Island findAvailableIsland() {
        for (Island island : getIslands()) {
            if (!island.isTaken()) {
                return island;
            }
        }
        return null;
    }


    public List<Location> getFeastChestLocations() {
        return feastChests;
    }

    public List<Location> getMiniFeastChestLocations() {
        return miniFeastChests;
    }

    public void addFeastChest(Location location) {
        feastChests.add(location);
    }

    public void addMiniFeastChest(Location location) {
        miniFeastChests.add(location);
    }

    public void clearFeastChests() {
        feastChests.clear();
    }

    public void clearMiniFeastChests() {
        miniFeastChests.clear();
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean canStart() {
        return players.size() >= minPlayers;
    }
}
