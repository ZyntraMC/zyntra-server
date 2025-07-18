package mc.zyntra.bukkit.event.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mc.zyntra.bukkit.event.CustomEvent;
import mc.zyntra.general.account.group.GroupData;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class PlayerGroupAddEvent extends CustomEvent {

    private final Player player;
    private final GroupData group;
    private final long duration;

}
