package mc.zyntra.bukkit.api.worldedit.block.future;

import org.bukkit.Location;
import org.bukkit.Material;

import lombok.Getter;

@SuppressWarnings("deprecation")
public abstract class FutureBlock {

	private final Location location;
	private final Material type;
	private final byte data;
	
	@Getter
	private boolean async;

	public FutureBlock(Location location, Material type, byte data) {
		this.location = location;
		this.type = type;
		this.data = data;
	}

	public byte getData() {
		return data;
	}

	public Location getLocation() {
		return location;
	}

	public Material getType() {
		return type;
	}
	
	public FutureBlock async() {
		this.async = true;
		return this;
	}

	public void setBlock() {
		location.getBlock().setType(type);
		location.getBlock().setData(data);
	}

	public abstract void place();
}