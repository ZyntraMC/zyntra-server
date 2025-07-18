package mc.zyntra.lobby.npc;

import mc.zyntra.bukkit.event.update.UpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class NPC implements Listener {

    private final NPCController controller;

    public NPC() {
        controller = new NPCController();
    }

    public NPCController getController() {
        return controller;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdate(UpdateEvent event) {
        if (event.getCurrentTick() % 200 == 0) {
            controller.updateHolograms();
        }
    }
}
