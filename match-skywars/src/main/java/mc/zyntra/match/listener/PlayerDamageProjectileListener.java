package mc.zyntra.match.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Locale;

public class PlayerDamageProjectileListener implements Listener {
    @EventHandler
    public void HP(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
            return;

        Projectile proj = (Projectile) e.getDamager();
        if (!(proj.getShooter() instanceof Player))
            return;
        if (!(e.getEntity() instanceof Player))
            return;
        Player shooter = (Player) proj.getShooter();
        Player shot = (Player) e.getEntity();

        String hp = String.format(Locale.US, "%.1f", shot.getHealth());
        shooter.sendMessage("§c" + shot.getName() + " §eestá com §c" + hp + " HP§e.");
    }
}
