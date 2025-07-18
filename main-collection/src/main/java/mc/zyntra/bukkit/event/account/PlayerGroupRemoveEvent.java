package mc.zyntra.bukkit.event.account;

import mc.zyntra.bukkit.event.CustomEvent;
import mc.zyntra.general.account.group.GroupData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class PlayerGroupRemoveEvent extends CustomEvent {

    private final Player player;
    private final GroupData group;

}
