package mc.zyntra.general.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerType {

    UNKNOWN("Unknown Server", ServerCategory.UNKNOWN),
    PROXY("Proxy", ServerCategory.UNKNOWN),

    AUTH("Autenticação", ServerCategory.AUTH),

    LOBBY("Saguão Central", ServerCategory.LOBBY),
    LOBBY_PVP("Saguão de Treinamento", ServerCategory.GAME_LOBBY),
    LOBBY_DUELS("Saguão de Duelos", ServerCategory.GAME_LOBBY),
    LOBBY_SKYWARS("Saguão de Sky Wars", ServerCategory.GAME_LOBBY),
    LOBBY_BEDWARS("Saguão de Bed Wars", ServerCategory.GAME_LOBBY),
    LOBBY_ESCESC("Saguão de Esc-Esc", ServerCategory.GAME_LOBBY),

    PVP_ARENA("Arena PvP", ServerCategory.GAME),
    PVP_FPS("Arena Fps", ServerCategory.GAME),
    PVP_CHALLENGE("Arena Lava", ServerCategory.GAME),

    DUELS_SOUP_1V1("Duelo de Sopa 1v1", ServerCategory.GAME), DUELS_SOUP_2V2("Duelo de Sopa 2v2", ServerCategory.GAME),

    DUELS_UHC_1V1("Duelo de UHC 1v1", ServerCategory.GAME), DUELS_UHC_2V2("Duelo de UHC 2v2", ServerCategory.GAME),

    DUELS_GLADIATOR_1V1("Duelo de Gladiator 1v1", ServerCategory.GAME), DUELS_GLADIATOR_2V2("Duelo de Gladiator 2v2", ServerCategory.GAME),

    DUELS_SIMULATOR_1V1("Duelo de Simulator 1v1", ServerCategory.GAME), DUELS_SIMULATOR_2V2("Duelo de Simulator 2v2", ServerCategory.GAME),

    SW_SOLO("Solo", ServerCategory.GAME),
    SW_DUO("Dupla", ServerCategory.GAME),
    SW_MEGA("Mega", ServerCategory.GAME),

    BW_SOLO("Solo", ServerCategory.GAME),
    BW_DUO("Dupla", ServerCategory.GAME),
    BW_MEGA("Mega", ServerCategory.GAME),

    ESCONDE_ESCONDE("Esconde Esconde", ServerCategory.GAME);

    private final String name;
    private final ServerCategory category;

    public ServerType getLobbyServer() {
        switch (this) {
            case PVP_ARENA:
            case PVP_CHALLENGE:
            case PVP_FPS:
                return LOBBY_PVP;
            case DUELS_UHC_1V1:
            case DUELS_UHC_2V2:
            case DUELS_SOUP_1V1:
            case DUELS_SOUP_2V2:
            case DUELS_GLADIATOR_1V1:
            case DUELS_GLADIATOR_2V2:
            case DUELS_SIMULATOR_1V1:
            case DUELS_SIMULATOR_2V2:
                return LOBBY_DUELS;
            case SW_SOLO:
            case SW_DUO:
            case SW_MEGA:
                return LOBBY;
            case BW_SOLO:
            case BW_DUO:
            case BW_MEGA:
                return LOBBY_BEDWARS;
            case ESCONDE_ESCONDE:
                return LOBBY_ESCESC;
            default:
                return LOBBY;
        }
    }
}
