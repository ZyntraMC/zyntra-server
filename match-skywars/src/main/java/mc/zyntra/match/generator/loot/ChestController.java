package mc.zyntra.match.generator.loot;

import mc.zyntra.Leading;
import mc.zyntra.room.Room;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ChestController {

    public enum Type {
        NORMAL, MINIFEAST, FEAST;
    }

    public void addChest(Room room, Block block, Type type) {
        if (type.equals(Type.FEAST)) {
            room.getFChests().add(block);
        } else {
            room.getChests().add(block);
        }
        set(room, type, block);
        Leading.getInstance().saveConfig();
    }

    public void set(Room room, Type type, Block block) {
        List<String> chest = new ArrayList<>();
        for (Block c : room.getChests()) {
            if (c.equals(block)) {
                chest.add(Leading.getInstance().getLocation().serialize(block.getLocation()));
            }
        }

        List<String> fchest = new ArrayList<>();
        for (Block c : room.getFChests()) {
            if (c.equals(block)) {
                fchest.add(Leading.getInstance().getLocation().serialize(block.getLocation()));
            }
        }
        if (type.equals(Type.FEAST)) {
            Leading.getInstance().getConfig().set("room." + room.getName() + ".chests." + type.toString().toLowerCase(), fchest);
        } else {
            Leading.getInstance().getConfig().set("room." + room.getName() + ".chests." + type.toString().toLowerCase(), chest);
        }
    }
}
