package mc.zyntra.arena.player;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.structure.Island;
import org.bukkit.entity.Player;

@Getter @Setter
public class Gamer {

    private final Player player;
    private final Arena arena;

    private Island island;
    private boolean spectator;

    public Gamer(Player player, Arena arena) {
        this.player = player;
        this.arena = arena;
    }

    public boolean isAlive() {
        return !spectator;
    }

    public void setIsland(Island island) {
        this.island = island;
        island.setPlayer(player);
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
        if (spectator && island != null) {
            island.setPlayer(null);
            island = null;
        }
    }

    public void reset() {
        this.spectator = false;
        if (this.island != null) {
            this.island.setPlayer(null);
            this.island = null;
        }
    }
}
