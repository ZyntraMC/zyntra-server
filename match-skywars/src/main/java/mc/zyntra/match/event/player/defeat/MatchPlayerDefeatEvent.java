package mc.zyntra.match.event.player.defeat;

import mc.zyntra.match.event.player.MatchPlayerEvent;
import mc.zyntra.room.Room;
import org.bukkit.entity.Player;


public final class MatchPlayerDefeatEvent extends MatchPlayerEvent {
    public MatchPlayerDefeatEvent(Room room, Player player) {
        super(room, player);
    }
}