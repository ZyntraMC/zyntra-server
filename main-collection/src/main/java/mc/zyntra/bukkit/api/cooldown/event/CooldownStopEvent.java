package mc.zyntra.bukkit.api.cooldown.event;

import mc.zyntra.bukkit.api.cooldown.types.Cooldown;
import org.bukkit.entity.Player;

public class CooldownStopEvent extends CooldownEvent {

    public CooldownStopEvent(Player player, Cooldown cooldown) {
        super(player, cooldown);
    }

}