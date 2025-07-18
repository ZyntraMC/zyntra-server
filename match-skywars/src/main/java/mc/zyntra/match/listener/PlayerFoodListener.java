package mc.zyntra.match.listener;

import mc.zyntra.stage.MatchStage;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.util.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodListener implements Listener {
    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get((Player) e.getEntity());
        if (matchPlayer.getRoom().getStage() != MatchStage.EM_JOGO) e.setCancelled(true);
    }
}
