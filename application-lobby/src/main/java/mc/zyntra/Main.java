package mc.zyntra;

import mc.zyntra.lobby.generator.user.parser.UserParser;
import mc.zyntra.lobby.generator.scheduler.LobbyGenerator;
import lombok.Setter;
import mc.zyntra.bukkit.BukkitMain;
import lombok.Getter;
import mc.zyntra.lobby.npc.NPCController;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Main extends JavaPlugin {

    @Setter
    private General general;
    private final UserParser userParser = new UserParser();

    public static Main getInstance() {
        return Main.getPlugin(Main.class);
    }

    NPCController npcController = new NPCController();

    @Override
    public void onLoad() {
        saveDefaultConfig();
        setGeneral(new LobbyGenerator());
    }

    @Override
    public void onEnable() {

        BukkitMain.getInstance().loadCommandClasses(this, "mc.zyntra.lobby.commands");
        BukkitMain.getInstance().loadListenerClasses(this, "mc.zyntra");

        Location location = Main.getInstance().getLocationFromConfig("spawn") == null ?
                Bukkit.getWorlds().get(0).getSpawnLocation() : Main.getInstance().getLocationFromConfig("spawn");

        World world = getServer().getWorlds().get(0);
        world.getWorldBorder().setCenter(location);
        world.getWorldBorder().setSize(450);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(6000);

        npcController.registerNPCs();
    }

    @Override
    public void onDisable() {

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
