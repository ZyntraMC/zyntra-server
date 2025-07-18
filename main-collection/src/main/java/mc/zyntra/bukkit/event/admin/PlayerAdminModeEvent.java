package mc.zyntra.bukkit.event.admin;

import mc.zyntra.bukkit.event.CustomEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Setter
@Getter
@RequiredArgsConstructor
public class PlayerAdminModeEvent extends CustomEvent implements Cancellable {

    private final Player player;
    private final AdminMode adminMode;
    private final GameMode gameMode;

    private boolean cancelled = false;

    public enum AdminMode {
        PLAYER, ADMIN
    }
}
