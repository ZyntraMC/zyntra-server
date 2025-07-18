package mc.zyntra.bukkit.api.bossbar;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.bossbar.entity.WitherBoss;
import mc.zyntra.bukkit.system.utils.entity.EntityUtils;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class BossBarEntity {

    @NonNull
    @Getter
    private Player player;

    @Getter
    protected String title;

    @Getter
    protected float health;

    @Getter
    protected int id;

    public boolean setTitle(String title) {
        if (!Objects.equals(this.title, title)) {
            this.title = title;
            return true;
        }

        return false;
    }

    public boolean setHealth(float percent) {
        float minHealth = (this instanceof WitherBoss ? 151F : 1F);
        float maxHealth = (this instanceof WitherBoss ? 300F : 200F);
        float newHealth = Math.max(minHealth, (percent / 100F) * maxHealth);

        if (!Objects.equals(this.health, newHealth)) {
            this.health = newHealth;
            return true;
        }

        return false;
    }

    protected void sendPacket(Player player, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAlive() {
        return id > 0;
    }

    public void setAlive(boolean alive) {
        this.id = (alive ? EntityUtils.next() : -1);
    }

    public abstract void spawn();

    public abstract void remove();

    public abstract void update();

    public abstract void move(PlayerMoveEvent event);

    private BukkitTask task;

    public void startTask(BukkitRunnable runnable) {
        if (task == null)
            task = runnable.runTaskTimer(BukkitMain.getInstance(), 20L, 20L);
    }

    public void cancelTask() {
        if (task != null)
            task.cancel();
    }

    public boolean hasTask() {
        return task != null;
    }

}
