package mc.zyntra.bukkit.api.npc;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NPCListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdate(UpdateEvent event) {
        Bukkit.getOnlinePlayers().forEach(this::updatePlayer);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        updatePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoinedPlayer(PlayerJoinEvent event) {
        updatePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuitedPlayer(PlayerQuitEvent event) {
        for (NPC npc : BukkitMain.getInstance().getNpcManager().getNPCs()) {
            if (npc.isSpawned() && npc instanceof NPCHuman)
                ((NPCHuman) npc).getViewers().remove(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteractedPlayer(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity) {
            LivingEntity clicked = (LivingEntity) event.getRightClicked();

            NPC npc = BukkitMain.getInstance().getNpcManager().getNPC(clicked.getEntityId());
            if (npc != null) {
                event.setCancelled(true);

                NPC.onClickListener clickListener = npc.getOnClickListener();

                if (clickListener != null)
                    clickListener.onClick(event.getPlayer());
            }
        }
    }

    private void updatePlayer(Player viewer) {
        for (NPC npc : BukkitMain.getInstance().getNpcManager().getNPCs()) {
            if (npc.isSpawned() && npc instanceof NPCHuman) {
                NPCHuman npcHuman = (NPCHuman) npc;
                if (!viewer.getWorld().equals(npc.getWorld()))
                    npcHuman.getViewers().remove(viewer);
                else if (!BukkitMain.getInstance().getNpcManager().canSpawn(viewer.getLocation(), npc.getLocation()))
                    npcHuman.despawnTo(viewer);
                else
                    npcHuman.spawnTo(viewer);
            }
        }
    }
}