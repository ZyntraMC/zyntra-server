package mc.zyntra.server;

import mc.zyntra.general.Platform;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class ServerGeneral implements Platform {

    @Override
    public UUID getUUID(String playerName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        return player != null ? player.getUniqueId() : null;
    }

    @Override
    public <T> T getPlayerByName(String playerName, Class<T> clazz) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        return player != null ? clazz.cast(player) : null;
    }

    @Override
    public <T> T getExactPlayerByName(String playerName, Class<T> clazz) {
        ProxiedPlayer p = null;

        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.getName().equals(playerName)) {
                p = player;
                break;
            }
        }

        return p != null ? clazz.cast(p) : null;
    }

    @Override
    public <T> T getPlayerByUniqueId(UUID uniqueId, Class<T> clazz) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uniqueId);
        return player != null ? clazz.cast(player) : null;
    }

    @Override
    public boolean isOnline(UUID uniqueId) {
        ProxiedPlayer player = getPlayerByUniqueId(uniqueId, ProxiedPlayer.class);
        return player != null;
    }

    @Override
    public boolean isOnline(String name) {
        ProxiedPlayer player = getPlayerByName(name, ProxiedPlayer.class);
        return player != null;
    }

    @Override
    public void sendMessage(UUID uniqueId, String message) {
        ProxiedPlayer player = getPlayerByUniqueId(uniqueId, ProxiedPlayer.class);
        if (player != null)
            player.sendMessage(message);
    }

    @Override
    public void sendMessage(UUID uniqueId, BaseComponent message) {
        ProxiedPlayer player = getPlayerByUniqueId(uniqueId, ProxiedPlayer.class);
        if (player != null)
            player.sendMessage(message);
    }

    @Override
    public void sendMessage(UUID uniqueId, BaseComponent[] message) {
        ProxiedPlayer player = getPlayerByUniqueId(uniqueId, ProxiedPlayer.class);
        if (player != null)
            player.sendMessage(message);
    }

    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(ServerMain.getInstance(), runnable);
    }
}