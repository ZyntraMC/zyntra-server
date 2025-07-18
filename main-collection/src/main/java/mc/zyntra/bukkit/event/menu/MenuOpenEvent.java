package mc.zyntra.bukkit.event.menu;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
@RequiredArgsConstructor
public class MenuOpenEvent extends CustomEvent {

    private final Player player;
    private final Inventory inventory;

}
