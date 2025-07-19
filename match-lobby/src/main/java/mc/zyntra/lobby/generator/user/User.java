package mc.zyntra.lobby.generator.user;

import mc.zyntra.bukkit.api.scoreboard.Scoreboard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class User {

    private final UUID uniqueId;

    private transient boolean buildEnabled;
    private transient Scoreboard scoreboard;
}
