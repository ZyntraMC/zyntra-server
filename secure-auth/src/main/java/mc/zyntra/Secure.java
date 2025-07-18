package mc.zyntra;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.player.generator.parser.UserParser;
import mc.zyntra.player.commands.endorse.LoginCommand;
import mc.zyntra.player.commands.endorse.RegisterCommand;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Secure extends JavaPlugin {

    @Getter
    private static Secure instance;
    private ProtocolManager protocolManager;
    private final UserParser userParser = new UserParser();

    @Override
    public void onLoad() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        BukkitMain.getInstance().loadCommandClasses(this, "mc.zyntra.player.commands");
        BukkitMain.getInstance().loadListenerClasses(this, "mc.zyntra.player.listeners");

        getServer().getPluginCommand("register").setExecutor(new RegisterCommand());
        getServer().getPluginCommand("login").setExecutor(new LoginCommand());

        World world = getServer().getWorlds().get(0);
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(300);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void saveLocation(Location location, String key) {
        getConfig().set(key + ".x", location.getX());
        getConfig().set(key + ".y", location.getY());
        getConfig().set(key + ".z", location.getZ());
        getConfig().set(key + ".yaw", location.getYaw());
        getConfig().set(key + ".pitch", location.getPitch());
        saveConfig();
    }

    public Location getLocationFromConfig(String key) {
        double x = getConfig().getDouble(key + ".x");
        double y = getConfig().getDouble(key + ".y");
        double z = getConfig().getDouble(key + ".z");
        float yaw = (float) getConfig().getDouble(key + ".yaw");
        float pitch = (float) getConfig().getDouble(key + ".pitch");
        return new Location(getServer().getWorlds().get(0), x, y, z, yaw, pitch);
    }
}
