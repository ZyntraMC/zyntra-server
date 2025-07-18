package mc.zyntra.general.server.load;

import mc.zyntra.general.server.ServerType;

import java.util.Set;
import java.util.UUID;

public abstract class MinigameServer extends ProxiedServer {
    private int time;
    private String map = "Unknown";
    private MinigameState state = MinigameState.WAITING;

    public MinigameServer(String serverId, ServerType type, Set<UUID> players, int maxPlayers, boolean joinEnabled) {
        super(serverId, type, players, maxPlayers, joinEnabled);
    }

    @Override
    public int getActualNumber() {
        return super.getActualNumber();
    }

    public abstract boolean isInProgress();

    public int getTime() {
        return this.time;
    }

    public String getMap() {
        return this.map;
    }

    public MinigameState getState() {
        return this.state;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void setState(MinigameState state) {
        this.state = state;
    }
}
