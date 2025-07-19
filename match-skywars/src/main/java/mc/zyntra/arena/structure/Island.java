package mc.zyntra.arena.structure;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.arena.structure.loot.LootChest;
import mc.zyntra.arena.structure.loot.LootFactory;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Island {

    private final Location spawn;
    private final Location cageOrigin;
    private final List<Location> chestLocations = new ArrayList<Location>(3);

    private Player player;

    public Island(Location spawn, Location cageOrigin) {
        this.spawn = spawn;
        this.cageOrigin = cageOrigin;
    }

    public boolean isTaken() {
        return player != null;
    }

    public void reset() {
        this.player = null;
    }

    public void addChestLocation(Location location) {
        if (chestLocations.size() >= 3) return;
        chestLocations.add(location);
        fillChest(location);
    }

    public void clearChestLocations() {
        chestLocations.clear();
    }

    private void fillChest(Location location) {
        Block block = location.getBlock();
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            ItemStack[] loot = LootFactory.generate(LootChest.Type.NORMAL).toArray(new ItemStack[0]);
            chest.getInventory().setContents(loot);
        }
    }
}
