package mc.zyntra.bukkit.api.cooldown.event;

import mc.zyntra.bukkit.api.cooldown.types.Cooldown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
@Setter
public class CooldownStartEvent extends CooldownEvent implements Cancellable {
	
    private boolean cancelled;

    public CooldownStartEvent(Player player, Cooldown cooldown) {
        super(player, cooldown);
    }

}
