package mc.zyntra;

import lombok.Getter;
import mc.zyntra.arena.controller.ArenaController;
import mc.zyntra.bukkit.BukkitMain;
import org.bukkit.plugin.java.JavaPlugin;

public class Skywars extends JavaPlugin {

    @Getter
    private static Skywars instance;

    @Getter
    private ArenaController arenaController;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        this.arenaController = new ArenaController();
        getArenaController().loadAllArenas();

        BukkitMain.getInstance().loadCommandClasses(this, "mc.zyntra.match.command");
        BukkitMain.getInstance().loadListenerClasses(this, "mc.zyntra.match");
    }

    @Override
    public void onDisable() {
        getLogger().info("[Skywars] Plugin desligado.");
    }
}
