package mc.zyntra.general.matchmaking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {

    SOUP("Soup Duels", 2, 2),
    GLADIATOR("Gladiator Duels", 2, 2),
    SIMULATOR("Simulator Duels", 2, 2),
    SUMO("Sumo Duels", 2, 2),
    SKYWARS("Sky Wars Duels", 2, 2);

    private final String name;
    private final int minPlayers, maxPlayers;

}
