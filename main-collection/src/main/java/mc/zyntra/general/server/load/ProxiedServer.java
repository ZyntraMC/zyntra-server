package mc.zyntra.general.server.load;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.load.balancer.element.LoadBalancerObject;
import mc.zyntra.general.server.load.balancer.element.NumberConnection;

import java.util.Set;
import java.util.UUID;

public class ProxiedServer implements LoadBalancerObject, NumberConnection {
    private String serverId;
    private ServerType serverType;
    private Set<UUID> players;
    private int maxPlayers;
    private int playersRecord;
    private boolean joinEnabled;
    private long startTime;

    public ProxiedServer(String serverId, ServerType serverType, Set<UUID> players, int maxPlayers, boolean joinEnabled) {
        this.serverId = serverId.toLowerCase();
        this.serverType = serverType;
        this.players = players;
        this.maxPlayers = maxPlayers;
        this.joinEnabled = joinEnabled;
    }

    public void setOnlinePlayers(Set<UUID> onlinePlayers) {
        this.players = onlinePlayers;
    }

    public void joinPlayer(UUID uuid) {
        this.players.add(uuid);
        this.playersRecord = Math.max(this.playersRecord, this.players.size());
    }

    public void leavePlayer(UUID uuid) {
        this.players.remove(uuid);
    }

    public int getOnlinePlayers() {
        return this.players.size();
    }

    public boolean isFull() {
        return this.players.size() >= this.maxPlayers;
    }

    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(this.serverId);
    }

    @Override
    public boolean canBeSelected() {
        return !this.isFull();
    }

    @Override
    public int getActualNumber() {
        return this.getOnlinePlayers();
    }

    @Override
    public int getMaxNumber() {
        return this.getMaxPlayers();
    }

    @Override
    public String getServerId() {
        return this.serverId;
    }

    public ServerType getServerType() {
        return this.serverType;
    }

    public Set<UUID> getPlayers() {
        return this.players;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public int getPlayersRecord() {
        return this.playersRecord;
    }

    public boolean isJoinEnabled() {
        return this.joinEnabled;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setPlayersRecord(int playersRecord) {
        this.playersRecord = playersRecord;
    }

    public void setJoinEnabled(boolean joinEnabled) {
        this.joinEnabled = joinEnabled;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}

