package mc.zyntra.server.player.listeners;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.server.ServerMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        Core.getDataServer().getLocalServer().setOnlineCount(ProxyServer.getInstance().getOnlineCount());
        Core.getDataServer().updateServer();
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        Core.getDataServer().getLocalServer().setOnlineCount(ProxyServer.getInstance().getOnlineCount() - 1);
        Core.getDataServer().updateServer();
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing response = event.getResponse();

        if (Core.getWhitelistController().isEnabled()) {
            response.setVersion(new ServerPing.Protocol("Manutenção", -1));
        }

        response.getPlayers().setMax(300);
        response.setDescription((Core.getWhitelistController().isEnabled()
                        ? ServerMain.getInstance().getConfig().getString("motd.maintenance").replace("&", "§").replace("{breakLine}", "\n")
                        : ServerMain.getInstance().getConfig().getString("motd.default").replace("&", "§").replace("{breakLine}", "\n")));
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            ServerConfiguration serverConfiguration = Core.getDataServer().getServers(
                    player.getPendingConnection().isOnlineMode() ? ServerType.LOBBY : ServerType.AUTH)
                        .stream()
                        .findFirst()
                        .orElse(null);

            if (serverConfiguration == null || ProxyServer.getInstance().getServerInfo(serverConfiguration.getName()) == null) {
                player.disconnect(Constant.NO_SERVER_FOUND);
                return;
            }

            event.setTarget(ProxyServer.getInstance().getServerInfo(serverConfiguration.getName()));
        }
    }
}
