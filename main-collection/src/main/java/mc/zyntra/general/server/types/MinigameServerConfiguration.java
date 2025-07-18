package mc.zyntra.general.server.types;

import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.ServerConfiguration;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MinigameServerConfiguration extends ServerConfiguration {

    @Setter
    private int time = 0, alive = 0;

    @Setter
    private MinigameStage stage = MinigameStage.WAITING_FOR_PLAYERS;

    public MinigameServerConfiguration(String name, ServerType serverType) {
        super(name, serverType);
    }
}
