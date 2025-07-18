package mc.zyntra.bukkit.api.npc;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class NPC {

    private static int npcCount = 0;

    protected final NPCManager npcManager;

    @Getter
    private final int npcId;

    @Getter
    @Setter
    protected Location location;

    @Getter
    @Setter
    private onClickListener onClickListener;

    public NPC(NPCManager npcManager, @NonNull Location location) {
        this.npcId = npcCount++;
        this.npcManager = npcManager;
        this.location = location;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public List<Player> getPlayers() {
        return location.getWorld().getPlayers();
    }

    public abstract int getEntityId();

    public abstract boolean isSpawned();

    public abstract void spawn();

    public abstract void despawn();

    public abstract double getEyeHeight();

    public interface onClickListener {

        void onClick(Player player);

    }
}