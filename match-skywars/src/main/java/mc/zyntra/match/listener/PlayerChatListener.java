package mc.zyntra.match.listener;

import mc.zyntra.Leading;
import mc.zyntra.room.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Iterator;

public class PlayerChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        Room room = Leading.getInstance().getRoom();

        if (!room.getPlayers().contains(player)) {
            Iterator<Player> iterator = event.getRecipients().iterator();
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode enviar mensagens pois está morto.");

            while (iterator.hasNext()) {
                Player target = iterator.next();
                if (room.getPlayers().contains(target)) {
                    iterator.remove();
                    event.setCancelled(false);
                }
            }
        }
    }
}
