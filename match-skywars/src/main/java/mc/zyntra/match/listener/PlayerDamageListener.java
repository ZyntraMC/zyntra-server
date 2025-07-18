package mc.zyntra.match.listener;

import mc.zyntra.stage.MatchStage;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.generator.player.fight.FightLoader;
import mc.zyntra.util.Platform;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {
    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player)) {
            Player player = (Player) e.getDamager();
            MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player);
            if (matchPlayer.getRoom().getStage() != MatchStage.EM_JOGO)
                return;
            if (matchPlayer.getRoom().getSpectators().contains(player)) {
                e.setCancelled(true);
                return;
            }
            if (matchPlayer.getRoom().getPlayers().contains(player)) {
                if ((e.getEntity() instanceof Player)) {
                    Player target = (Player) e.getEntity();
                    if (target == player)
                        return;
                    makeCombatLog(player, target);
                    return;
                }
            }
        } else if ((e.getDamager() instanceof Projectile)) {
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player player = (Player) projectile.getShooter();
                if (e.getEntity() instanceof Player) {
                    Player target = (Player) e.getEntity();
                    if (target == player)
                        return;
                    makeCombatLog(player, target);
                    return;
                }
            }
        }
    }

    public void makeCombatLog(Player damage, Player damaged) {
        if (FightLoader.inCombat(damaged)) {
            FightLoader.setCombat(damage, damaged);
            System.out.println(
                    "[Debug-CombatLog] " + damage.getName() + " entrou novamente em combate contra " + damaged.getName());
        } else {
            FightLoader.setCombat(damage, damaged);
            System.out
                    .println("[Debug-CombatLog] " + damage.getName() + " entrou em combate contra " + damaged.getName());
        }
    }
}
