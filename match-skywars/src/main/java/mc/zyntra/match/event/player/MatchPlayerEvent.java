package mc.zyntra.match.event.player;

import mc.zyntra.match.event.MatchEvent;
import mc.zyntra.room.Room;
import lombok.Getter;
import org.bukkit.entity.Player;


@Getter
public abstract class MatchPlayerEvent extends MatchEvent {

    private final Player player;

    public MatchPlayerEvent(Room room, Player player) {
        super(room);
        this.player = player;
    }
}
