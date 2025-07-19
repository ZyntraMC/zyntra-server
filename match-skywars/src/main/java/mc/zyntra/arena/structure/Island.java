package mc.zyntra.arena.structure;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.arena.structure.loot.LootChest;
import mc.zyntra.arena.structure.loot.LootFactory;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Island {

    private final Location spawn;
    private final Location cageOrigin;

    // Mapeia localização para tipo do baú
    private final Map<Location, LootChest.Type> chestLocations = new HashMap<>();

    private org.bukkit.entity.Player player;

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
        if (chestLocations.size() >= 3) return; // máximo 3 baús
        chestLocations.put(location, LootChest.Type.NORMAL);
        fillChest(location, LootChest.Type.NORMAL);
    }

    public void clearChestLocations() {
        chestLocations.clear();
    }

    public void fillChest(Location location, LootChest.Type type) {
        Block block = location.getBlock();
        if (block.getState() instanceof Chest) {
            Chest chest = (Chest) block.getState();
            ItemStack[] loot = LootFactory.generate(type).toArray(new ItemStack[0]);
            chest.getInventory().setContents(loot);
        }
    }
}
