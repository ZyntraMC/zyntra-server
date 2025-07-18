package mc.zyntra.player.event;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class PlayerLoggedEvent extends CustomEvent {

    private final Player player;

}
