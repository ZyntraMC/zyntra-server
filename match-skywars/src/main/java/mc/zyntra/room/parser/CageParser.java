package mc.zyntra.room.parser;

import mc.zyntra.Leading;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;


public class CageParser {
    private Player[] players;
    private Location location;
    private HashSet<Block> blocks;

    public CageParser(Location location, Player... players) {
        this.players = players;
        this.location = location;
        this.blocks = new HashSet<>();
                blocks.add(location.clone().add(0, 0, 1).getBlock());
                blocks.add(location.clone().add(1, 0, 0).getBlock());
                blocks.add(location.clone().add(0, 1, 1).getBlock());
                blocks.add(location.clone().add(1, 1, 0).getBlock());
                blocks.add(location.clone().add(0, 2, 1).getBlock());
                blocks.add(location.clone().add(1, 2, 0).getBlock());
                blocks.add(location.clone().add(0, 3, 0).getBlock());
                blocks.add(location.clone().add(0, -1, 0).getBlock());
                blocks.add(location.clone().add(0, 0, -1).getBlock());
                blocks.add(location.clone().add(-1, 0, 0).getBlock());
                blocks.add(location.clone().add(0, 1, -1).getBlock());
                blocks.add(location.clone().add(-1, 1, 0).getBlock());
                blocks.add(location.clone().add(0, 2, -1).getBlock());
                blocks.add(location.clone().add(-1, 2, 0).getBlock());

        Leading.getInstance().getServer().getScheduler().runTask(Leading.getInstance(), () -> {
            blocks.forEach(block -> block.setType(Material.GLASS));
        });

        for (Player player : players) {
            if (player != null && player.isOnline()) {
                player.teleport(location.clone().add(0D, 1.5D, 0D));
            }
        }
        stepEffects();
    }

    public void clearBlocks() {
        stepEffects();

        Leading.getInstance().getServer().getScheduler().runTask(Leading.getInstance(), () -> {
            blocks.forEach(block -> block.setType(Material.AIR));
        });
    }

    @SuppressWarnings("deprecation")
    public void stepEffects() {
        blocks.forEach(block -> block.getWorld().spigot().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId(), block.getData(), 0.0F, 0.0F, 0.0F, 1.0F, 1, 64));
    }
}
