package mc.zyntra.match.listener;

import mc.zyntra.Leading;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.util.Platform;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

public class PlayerLoginListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!Leading.isStarted()) {
            event.setKickMessage("§cEsta sala ainda não carregou!");
            event.setResult(PlayerPreLoginEvent.Result.KICK_FULL);
            return;
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Platform.getPlayerLoader().create(new MatchPlayer(event.getPlayer().getUniqueId()));
    }
}
