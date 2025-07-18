package mc.zyntra.bukkit.api.worldedit.block.future.types;

import mc.zyntra.bukkit.api.worldedit.block.future.FutureBlock;
import org.bukkit.Location;
import org.bukkit.Material;

public class DefaultFutureBlock extends FutureBlock {

    public DefaultFutureBlock(Location location, Material type, byte data) {
        super(location, type, data);
    }

    @Override
    public void place() {
        setBlock();
    }

}