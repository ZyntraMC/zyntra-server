package mc.zyntra.backend.vincule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yWoxys
 */
@Getter
@AllArgsConstructor
public enum BackendConnection {

    DISCORD("****************",
            "DISCORD_CONNECTION", "Game Zyntra."),

    ZYNTRA_STUDIOS("***********",
            "COMPANY_CONNECTION", "Zyntra Studios.");

    String ID_CONNECTION, TYPE, ORGANIZATION;
}
