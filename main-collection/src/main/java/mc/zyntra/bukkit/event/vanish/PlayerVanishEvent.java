package mc.zyntra.bukkit.event.vanish;

import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerVanishEvent extends CustomEvent {

    private final ZyntraPlayer zyntraPlayer;

}
