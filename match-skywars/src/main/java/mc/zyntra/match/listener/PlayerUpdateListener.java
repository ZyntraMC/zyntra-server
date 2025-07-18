package mc.zyntra.match.listener;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.room.Room;
import mc.zyntra.util.Platform;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerUpdateListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateEvent.UpdateType.MINUTE)
            return;
        Room room = Leading.getInstance().getRoom();
        for (MatchPlayer user : Platform.getPlayerLoader().list()) {
            switch (room.getStage()) {
                case ESPERANDO:
                case INICIANDO:
                case EM_JOGO:
                case ENCERRANDO:
                case REINICIANDO:
                    user.getScoreboard().destroy();
                    Leading.getInstance().getScheduleScoreboard().handleScoreboard(Bukkit.getPlayer(user.getUniqueId()));
                    break;
            }
        }
    }
}
