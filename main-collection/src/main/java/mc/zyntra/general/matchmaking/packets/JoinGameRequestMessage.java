package mc.zyntra.general.matchmaking.packets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class JoinGameRequestMessage {

    public enum PlayerLoginState { PLAYER, SPECTATOR }

    private final UUID uniqueId;
    private final String gameId;
    private final PlayerLoginState state;

}
