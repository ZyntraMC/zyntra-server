package mc.zyntra.bukkit;

import mc.zyntra.bukkit.player.listeners.SoundMessageListener;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.bukkit.api.hologram.HologramManager;
import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.bukkit.api.npc.NPCManager;
import mc.zyntra.bukkit.api.worldedit.WorldeditController;
import mc.zyntra.bukkit.player.commands.BukkitCommandFramework;
import mc.zyntra.bukkit.system.pubsub.BukkitPubSub;
import mc.zyntra.bukkit.system.scheduler.UpdateScheduler;
import mc.zyntra.bukkit.system.utils.BukkitUtil;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.controller.ClanController;
import mc.zyntra.general.data.impl.*;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.utils.ClassGetter;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BukkitMain extends JavaPlugin {

    private NPCManager npcManager;
    private HologramManager hologramManager;
    private NametagController nametagController;
    private WorldeditController worldeditController;
    private ClanController clanController;

    public static BukkitMain getInstance() {
        return BukkitMain.getPlugin(BukkitMain.class);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();

        Core.getMongoStorage().connect(this.getConfig().getString("database.mongodb.uri"), Constant.MONGO_DATABASE_MAIN);
        Core.getRedisBackend().connect(this.getConfig().getString("database.packet.address"), this.getConfig().getString("database.packet.password"), this.getConfig().getInt("database.packet.port"));

        Core.setLogger(getLogger());
        Core.setPlatform(new BukkitGeneral());

        BukkitGeneral.getFakeAPI().loadFakes();

        String serverName = getConfig().getString("server.name", "unknown-server");
        ServerType serverType;

        try {
            serverType = ServerType.valueOf(getConfig().getString("server.type", "UNKNOWN").toUpperCase());
        } catch (Exception e) {
            getLogger().info("> Invalid server type.");
            Bukkit.shutdown();
            return;
        }

        int maxPlayers = Bukkit.getMaxPlayers();

        Core.setServerName(serverName);
        Core.setServerType(serverType);

        Core.setDataServer(new DataServerImpl(serverName, serverType, maxPlayers));
        Core.setDataPlayer(new DataPlayerImpl());
        Core.setDataStatus(new DataStatusImpl());
        Core.setDataClan(new DataClanImpl());
        Core.setDataReport(new DataReportImpl());

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                Core.getDataServer().stopServer()
        ));
    }

    @Override
    public void onEnable() {
        clanController = new ClanController();
        npcManager = new NPCManager();
        hologramManager = new HologramManager();
        nametagController = new NametagController();
        worldeditController = new WorldeditController();

        Core.getRedisBackend().registerPubSub(new BukkitPubSub(), Payload.toChannelsArray());

        // Unregister commands
        getServer().getScheduler().runTaskLater(this, () -> BukkitUtil.unregisterBukkitCommand(this,
                "icanhasbukkit", "?", "about", "help", "ban", "ban-ip", "banlist", "clear", "deop",
                "op", "difficulty", "effect", "enchant", "give", "kick", "list", "me", "pl", "plugins",
                "scoreboard", "seed", "spawnpoint", "spreadplayers", "summon", "tell", "tellraw", "testfor",
                "testforblocks", "weather", "xp", "reload", "rl", "worldborder", "achievement",
                "blockdata", "clone", "debug", "defaultgamemode", "entitydata", "execute", "fill",
                "minecraft:gamemode", "minecraft:teleport", "minecraft:tp",
                "pardon", "pardon-ip", "replaceitem", "setidletimeout", "testforblock", "title",
                "trigger", "viaver", "ps", "holograms", "hd", "holo", "restart", "filter",
                "packetlog", "whitelist", "?", "minecraft:gamerule", "minecraft:gm", "minecraft:gr",
                "minecraft:kill", "minecraft:pl", "minecraft:plugin", "minecraft:plugins", "minecraft:save-all",
                "minecraft:save-off", "minecraft:save-on", "minecraft:setblock", "minecraft:setworldspawn",
                "minecraft:time", "minecraft:timings", "minecraft:stop", "minecraft:toggledownfall",
                "minecraft:ver", "minecraft:version", "minecraft:playsound", "playsound", "particle", "minecraft:particle",
                "packet", "packet_filter", "list", "minecraft:list", "protocol", "protocollib:packet", "protocollib:packet_filter",
                "protocollib:protocol", "viaversion:viaversion", "viaversion", "viaversion:vvbukkit", "vvbukkit",
                "timings", "save-on", "save-off", "setblock", "bukkit:timings"), 3L);

        loadListenerClasses(this, "mc.zyntra.bukkit");
        loadCommandClasses(this, "mc.zyntra.bukkit.player.commands.registry");

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getMessenger().registerIncomingPluginChannel(this, "bungee:sound", new SoundMessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "bungee:sound");

        getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1, 1);

        getServer().getScheduler().runTaskTimer(this, System::gc, 0, 2400);

        Core.getLogger().info("> The server has been loaded (" + Core.getServerName() + " - " + Core.getServerType().name() + ")");

        Core.getPlatform().runAsync(() ->
                Core.getDataServer().startServer()
        );
    }

    @Override
    public void onDisable() {
        Core.getDataServer().stopServer();

        Core.getMongoStorage().close();
        Core.getRedisBackend().close();
    }

    public void loadListenerClasses(JavaPlugin plugin, String pkg) {
        for (Class<?> listenerClass : ClassGetter.getClassesForPackage(plugin.getClass(),
                pkg)) {
            if (Listener.class.isAssignableFrom(listenerClass)) {
                try {
                    Listener listener = (Listener) listenerClass.newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, plugin);
                    getLogger()
                            .warning("> The '" + listenerClass.getSimpleName() + "' listener was successfully registered.");
                } catch (Exception e) {
                    getLogger().warning("Couldn't load " + listenerClass.getSimpleName() + " listener!");
                }
            }
        }
    }

    public void loadCommandClasses(JavaPlugin plugin, String pkg) {
        BukkitCommandFramework framework = new BukkitCommandFramework(plugin);
        for (Class<?> commandClass : ClassGetter.getClassesForPackage(plugin.getClass(), pkg)) {
            if (CommandClass.class.isAssignableFrom(commandClass)) {
                try {
                    CommandClass commands = (CommandClass) commandClass.newInstance();
                    framework.registerCommands(commands);
                    getLogger()
                            .warning("> The '" + commandClass.getSimpleName() + "' command was successfully registered.");
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger()
                            .warning("> Error loading commands from class '" + commandClass.getSimpleName() + "'.");
                }
            }
        }
    }

    public void searchServer(Player player, ServerType serverType) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("SearchServer");
        out.writeUTF(serverType.name());
        player.sendPluginMessage(BukkitMain.getInstance(), "BungeeCord", out.toByteArray());
    }
}