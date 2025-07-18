package mc.zyntra.general.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

@Getter
@Setter
@RequiredArgsConstructor
public class ServerConfiguration {

    private final String name;
    private final ServerType serverType;
    private int maxPlayers;
    private String map;

    private int onlineCount = 0;
    private boolean restricted = false;

    public boolean isFull() {
        return onlineCount >= maxPlayers;
    }

    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }
}
