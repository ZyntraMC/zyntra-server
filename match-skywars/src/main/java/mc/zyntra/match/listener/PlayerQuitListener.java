package mc.zyntra.match.listener;

import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.util.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());

        matchPlayer.getRoom().remove(player);

        Platform.getPlayerLoader().unload(player.getUniqueId());
    }
}
