package mc.zyntra.general.data;

import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.types.MinigameServerConfiguration;

import java.util.Set;

public interface DataServer {

    ServerConfiguration getLocalServer();

    void updateServer();

    void startServer();

    void stopServer();

    ServerConfiguration getServer(String name);

    MinigameServerConfiguration getMinigameServer(String name);

    int getOnlineCount(String name);

    int getOnlineCount(ServerType... serverTypes);

    Set<ServerConfiguration> getServers(ServerType serverType);

    Set<MinigameServerConfiguration> getMinigameServers(ServerType serverType);

    Set<ServerConfiguration> getServers();
}