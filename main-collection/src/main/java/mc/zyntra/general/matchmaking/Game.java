package mc.zyntra.general.matchmaking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public abstract class Game {

    private final String id = "m" + RandomStringUtils.randomAlphabetic(5);
    private final String hostedAt;
    private final GameType gameType;

    private final Set<GamePlayer> players = new HashSet<>();

    @Setter
    private GameState state = GameState.WAITING_FOR_PLAYERS;

    public abstract void join(GamePlayer gamePlayer);

    public void addPlayer(GamePlayer gamePlayer) {
        players.add(gamePlayer);

        join(gamePlayer);
    }
}

