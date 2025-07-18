package mc.zyntra.general.server.launch.constructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mc.zyntra.general.server.ServerType;

@Getter
@AllArgsConstructor
public enum LaunchConst {
    PVP("PvP", new ServerType[] { ServerType.LOBBY_PVP, ServerType.PVP_ARENA, ServerType.PVP_FPS, ServerType.PVP_CHALLENGE }, true),
    ESC_ESC("Esconde Esconde", new ServerType[] { ServerType.LOBBY_ESCESC, ServerType.ESCONDE_ESCONDE }, false),
    SKYWARS("Sky Wars", new ServerType[] { ServerType.LOBBY_SKYWARS, ServerType.SW_SOLO, ServerType.SW_DUO, ServerType.SW_MEGA }, false),
    BEDWARS("Bed Wars", new ServerType[] { ServerType.LOBBY_BEDWARS, ServerType.BW_SOLO, ServerType.BW_DUO, ServerType.BW_MEGA }, false),
    DUELS("Duels", new ServerType[] { ServerType.LOBBY_DUELS, ServerType.DUELS_GLADIATOR_1V1, ServerType.DUELS_GLADIATOR_2V2, ServerType.DUELS_SIMULATOR_1V1, ServerType.DUELS_SIMULATOR_2V2, ServerType.DUELS_SOUP_1V1, ServerType.DUELS_SOUP_2V2, ServerType.DUELS_UHC_1V1, ServerType.DUELS_UHC_2V2 }, false);

    private String name;
    private ServerType[] serverType;
    private boolean alwaysLaunched;
}
