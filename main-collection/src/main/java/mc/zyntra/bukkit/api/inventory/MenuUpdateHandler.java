package mc.zyntra.bukkit.api.inventory;

import org.bukkit.entity.Player;

public interface MenuUpdateHandler {
	
	void onUpdate(Player player, MenuInventory menu);
	
}
