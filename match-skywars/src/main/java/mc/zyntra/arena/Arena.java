package mc.zyntra.arena;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.arena.enums.ArenaType;
import mc.zyntra.arena.structure.Island;
import mc.zyntra.game.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

    public Arena(String name) {
        this.name = name;
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

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean canStart() {
        return players.size() >= minPlayers;
    }
}
