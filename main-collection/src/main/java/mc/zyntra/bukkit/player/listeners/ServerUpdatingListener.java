package mc.zyntra.bukkit.player.listeners;

import mc.zyntra.bukkit.event.update.UpdateEvent;
import mc.zyntra.general.Core;
import mc.zyntra.general.server.ServerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerUpdatingListener implements Listener {

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateEvent.UpdateType.SECOND)
            return;

        ServerConfiguration localServer = Core.getDataServer().getLocalServer();
        localServer.setOnlineCount(Bukkit.getOnlinePlayers().size());
        localServer.setRestricted(Bukkit.hasWhitelist());
        localServer.setMaxPlayers(Bukkit.getMaxPlayers());

        Core.getPlatform().runAsync(() ->
                Core.getDataServer().updateServer()
        );
    }
}
