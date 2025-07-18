package mc.zyntra.match.event;

import mc.zyntra.bukkit.event.CustomEvent;
import mc.zyntra.room.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public abstract class MatchEvent extends CustomEvent {

    private final Room room;
}
