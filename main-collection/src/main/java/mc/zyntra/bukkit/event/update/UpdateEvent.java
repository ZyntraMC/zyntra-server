package mc.zyntra.bukkit.event.update;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;

@Getter
public class UpdateEvent extends CustomEvent {

    private final UpdateType type;
    private final long currentTick;

    public UpdateEvent(UpdateType type) {
        this(type, -1);
    }

    public UpdateEvent(UpdateType type, long currentTick) {
        this.type = type;
        this.currentTick = currentTick;
    }

    public enum UpdateType {
        TICK, SECOND, MINUTE;
    }
}
