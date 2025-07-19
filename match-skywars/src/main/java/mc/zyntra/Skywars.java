package mc.zyntra;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.bukkit.BukkitMain;
import org.bukkit.plugin.java.JavaPlugin;

public class Skywars extends JavaPlugin {

    @Getter
    private static Skywars instance;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        BukkitMain.getInstance().loadCommandClasses(this, "mc.zyntra.match.command");
        BukkitMain.getInstance().loadListenerClasses(this, "mc.zyntra.match");
    }

    @Override
    public void onDisable() {
    }
}
