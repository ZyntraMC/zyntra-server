package mc.zyntra.bukkit.event.account;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mc.zyntra.general.account.tag.enums.Tag;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public class PlayerTagChangeEvent extends CustomEvent {

    private final Player player;
    private final Tag tag;

}
