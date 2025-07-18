package mc.zyntra;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.util.Platform;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import mc.zyntra.util.SerializerUtil;
import mc.zyntra.match.scoreboard.ScheduleScoreboard;
import mc.zyntra.match.scoreboard.constructor.ScoreboardConstructor;
import mc.zyntra.room.Room;

import java.io.File;
import java.io.IOException;


public class Leading extends JavaPlugin {

    @Getter @Setter
    private static Leading instance;
    @Getter @Setter
    private SerializerUtil location;
    @Setter @Getter
    private ScheduleScoreboard scheduleScoreboard;
    @Setter @Getter
    private String roomName;
    @Setter @Getter
    private static boolean started;

    @Getter @Setter
    private Room room;

    public File chesting;
    @Getter static YamlConfiguration chest;

    @Override
    public void onLoad() {
        super.onLoad();
        setInstance(this);
        setLocation(new SerializerUtil());
        saveDefaultConfig();
        setScheduleScoreboard(new ScoreboardConstructor());
        saveConfig();
        room = null;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Platform.getKitLoader().load();
        Platform.getMatch().load();
        Platform.getPerkLoader().load();
        BukkitMain.getInstance().loadCommandClasses(this, "mc.zyntra.match.command");
        BukkitMain.getInstance().loadListenerClasses(this, "mc.zyntra.match");
        World world = Bukkit.getWorld("world");
        world.setAutoSave(false);

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(6000);
        getServer().getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                setStarted(true);
            }
        }, 40);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        restoreMap();
    }

    void restoreMap() {
        try {
            File worldFolder = new File(getServer().getWorldContainer(), "world");
            File backupFolder = new File(getDataFolder(), "maps/" + room.getMap());

            FileUtils.cleanDirectory(worldFolder);
            FileUtils.copyDirectory(backupFolder, worldFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}