package mc.zyntra.general.matchmaking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GamePlayer {

    public enum GamePlayerState { PLAYER, SPECTATOR }

    private final UUID uniqueId;

    @Setter
    private GamePlayerState state = GamePlayerState.PLAYER;

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

}
