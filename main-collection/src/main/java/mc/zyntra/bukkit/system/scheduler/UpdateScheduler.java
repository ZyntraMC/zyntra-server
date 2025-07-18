package mc.zyntra.bukkit.system.scheduler;

import mc.zyntra.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;

public class UpdateScheduler implements Runnable {

    private long currentTick;

    @Override
    public void run() {
        currentTick++;

        Bukkit.getPluginManager().callEvent(new UpdateEvent(UpdateEvent.UpdateType.TICK, currentTick));

        if (currentTick % 20 == 0)
            Bukkit.getPluginManager().callEvent(new UpdateEvent(UpdateEvent.UpdateType.SECOND, currentTick));

        if (currentTick % 1200 == 0)
            Bukkit.getPluginManager().callEvent(new UpdateEvent(UpdateEvent.UpdateType.MINUTE, currentTick));
    }
}
