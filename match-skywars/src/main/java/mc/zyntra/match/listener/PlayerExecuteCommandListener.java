package mc.zyntra.match.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerExecuteCommandListener implements Listener {
    @EventHandler
    public void onUseComands(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/fake") || e.getMessage().equalsIgnoreCase("/fly") || e.getMessage().equalsIgnoreCase("/voar")) {
            Player p = e.getPlayer();
            p.sendMessage("§cComando bloqueado no estágio jogo nesse modo.");
            e.setCancelled(true);
        }
    }
}
