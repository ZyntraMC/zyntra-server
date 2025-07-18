package mc.zyntra.match.event.player.win;

import mc.zyntra.match.event.player.MatchPlayerEvent;
import mc.zyntra.room.Room;
import org.bukkit.entity.Player;


public final class MatchPlayerWinEvent extends MatchPlayerEvent {
    public MatchPlayerWinEvent(Room room, Player player) {
        super(room, player);
    }
}
