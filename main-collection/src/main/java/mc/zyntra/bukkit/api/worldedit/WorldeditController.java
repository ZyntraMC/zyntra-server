package mc.zyntra.bukkit.api.worldedit;

import mc.zyntra.bukkit.api.inventory.item.InteractHandler;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.worldedit.block.future.FutureBlock;
import mc.zyntra.bukkit.api.worldedit.block.future.types.DefaultFutureBlock;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class WorldeditController {

    private final Map<UUID, Position> positionMap;
    private final ItemStack wand;

    public WorldeditController() {
        positionMap = new HashMap<>();

        wand = new ItemBuilder(Material.WOOD_AXE).build(new InteractHandler() {

            @Override
            public boolean onInteract(Player player, ItemStack item, Action action, Block clicked) {
                if (clicked != null) {
                    if (action == Action.LEFT_CLICK_BLOCK) {
                        setFirstPosition(player, clicked.getLocation());
                        player.sendMessage("§dO local da primeira posição é " + clicked.getX() + ", " + clicked.getY()
                                + ", " + clicked.getZ());
                    } else {
                        setSecondPosition(player, clicked.getLocation());
                        player.sendMessage("§dO local da segunda posição é " + clicked.getX() + ", " + clicked.getY() + ", "
                                + clicked.getZ());
                    }
                }
                return false;
            }
        });
    }

    public void addUndo(Player player, Map<Location, BlockState> map) {
//		player.getInventory().addItem(wand.getItemStack());
        positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).addUndo(map);
    }

    public void removeUndo(Player player, Map<Location, BlockState> map) {
//		player.getInventory().addItem(wand.getItemStack());
        positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).removeUndo(map);
    }

    public void giveWand(Player player) {
        player.getInventory().addItem(wand);
    }

    public void setFirstPosition(Player player, Location location) {
        positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).setFirstLocation(location);
        ;
    }

    public void setSecondPosition(Player player, Location location) {
        positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).setSecondLocation(location);
        ;
    }

    public boolean hasFirstPosition(Player player) {
        return positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).hasFirstLocation();
    }

    public boolean hasSecondPosition(Player player) {
        return positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).hasSecondLocation();
    }

    public boolean hasUndoList(Player player) {
        return !positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).getUndoList().isEmpty();
    }

    public Location getFirstPosition(Player player) {
        return positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).getFirstLocation();
    }

    public Location getSecondPosition(Player player) {
        return positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).getSecondLocation();
    }

    public List<Map<Location, BlockState>> getUndoList(Player player) {
        return positionMap.computeIfAbsent(player.getUniqueId(), v -> new Position()).getUndoList();
    }

    public List<Location> getLocationsFromTwoPoints(Location location1, Location location2) {
        List<Location> locations = new ArrayList<>();

        int topBlockX = (Math.max(location1.getBlockX(), location2.getBlockX()));
        int bottomBlockX = (Math.min(location1.getBlockX(), location2.getBlockX()));

        int topBlockY = (Math.max(location1.getBlockY(), location2.getBlockY()));
        int bottomBlockY = (Math.min(location1.getBlockY(), location2.getBlockY()));

        int topBlockZ = (Math.max(location1.getBlockZ(), location2.getBlockZ()));
        int bottomBlockZ = (Math.min(location1.getBlockZ(), location2.getBlockZ()));

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    locations.add(new Location(location1.getWorld(), x, y, z));
                }
            }
        }

        return locations;
    }

    public List<FutureBlock> load(Location location, File file) {
        BufferedReader reader;
        List<FutureBlock> blocks = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (!line.contains(",") || !line.contains(":")) {
                    continue;
                }

                String[] parts = line.split(":");
                String[] coordinates = parts[0].split(",");
                String[] blockData = parts[1].split("\\.");
                blocks.add(new DefaultFutureBlock(
                        location.clone().add(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[2]),
                                Integer.parseInt(coordinates[1])),
                        Material.values()[Integer.parseInt(blockData[0])],
                        blockData.length > 1 ? Byte.valueOf(blockData[1]) : 0));
            }

            reader.close();
        } catch (Exception ignored) {}

        return blocks;
    }

    public void spawn(Location location, File file) {
        for (FutureBlock futureBlock : load(location, file)) {
            futureBlock.place();
        }
    }

    public boolean setBlockFast(FutureBlock futureBlock) {
        int y = futureBlock.getLocation().getBlockY();

        if (y >= 255 || y < 0) {
            return false;
        }

        futureBlock.place();
        return true;
    }

    @Getter
    public class Position {

        @Setter
        private Location firstLocation;
        @Setter
        private Location secondLocation;

        private final List<Map<Location, BlockState>> undoList;

        public Position() {
            undoList = new ArrayList<>();
        }

        public void addUndo(Map<Location, BlockState> map) {
            undoList.add(map);
        }

        public void removeUndo(Map<Location, BlockState> map) {
            undoList.remove(map);
        }

        public boolean hasFirstLocation() {
            return firstLocation != null;
        }

        public boolean hasSecondLocation() {
            return firstLocation != null;
        }
    }

}
