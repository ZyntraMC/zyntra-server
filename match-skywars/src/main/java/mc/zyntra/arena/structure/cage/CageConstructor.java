package mc.zyntra.arena.structure.cage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CageConstructor {

    public static void build(Location origin, Material material) {
        if (origin == null || material == null) return;

        int baseX = origin.getBlockX();
        int baseY = origin.getBlockY();
        int baseZ = origin.getBlockZ();

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 2 && z == 0) continue; // topo aberto
                    if (x == 0 && y == 1 && z == 0) continue; // espaço para player
                    if (y == 0 || x != 0 || z != 0) {
                        Block block = origin.getWorld().getBlockAt(baseX + x, baseY + y, baseZ + z);
                        block.setType(material);
                    }
                }
            }
        }
    }

    public static void destroy(Location origin) {
        if (origin == null) return;

        int baseX = origin.getBlockX();
        int baseY = origin.getBlockY();
        int baseZ = origin.getBlockZ();

        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = origin.getWorld().getBlockAt(baseX + x, baseY + y, baseZ + z);
                    block.setType(Material.AIR);
                }
            }
        }
    }
}
