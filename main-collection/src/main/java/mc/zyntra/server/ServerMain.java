package mc.zyntra.server;

import mc.zyntra.general.controller.ClanController;
import mc.zyntra.general.data.impl.*;
import mc.zyntra.general.data.impl.DataPlayerImpl;
import mc.zyntra.general.data.impl.DataReportImpl;
import mc.zyntra.general.data.impl.DataServerImpl;
import mc.zyntra.general.data.impl.DataStatusImpl;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.controller.PunishController;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.server.player.commands.ServerCommandFramework;
import mc.zyntra.server.controller.ServerPunishController;
import mc.zyntra.server.system.pubsub.ProxyPubSub;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.utils.ClassGetter;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@Getter
public class ServerMain extends Plugin {

    @Getter
    private static ServerMain instance;
    private PunishController punishController;
    private Configuration config;
    private ClanController clanController;

    @Override
    public void onLoad() {
        instance = this;
        createConfig();
        Core.getMongoStorage().connect(this.getConfig().getString("database.mongodb.uri"), Constant.MONGO_DATABASE_MAIN);
        Core.getRedisBackend().connect(this.getConfig().getString("database.packet.address"), this.getConfig().getString("database.packet.password"), this.getConfig().getInt("database.packet.port"));

        Core.setServerName("proxy");
        Core.setServerType(ServerType.PROXY);

        Core.setLogger(getLogger());
        Core.setPlatform(new ServerGeneral());

        Core.setDataServer(new DataServerImpl("proxy", ServerType.PROXY, getProxy().getConfig().getPlayerLimit()));
        Core.setDataPlayer(new DataPlayerImpl());
        Core.setDataClan(new DataClanImpl());
        Core.setDataStatus(new DataStatusImpl());
        Core.setDataReport(new DataReportImpl());
    }

    @Override
    public void onEnable() {
        clanController = new ClanController();
        punishController = new ServerPunishController();

        loadListenerClasses(this, "mc.zyntra.server");
        loadCommandClasses(this, "mc.zyntra.server.player.commands.registry");

        Core.getRedisBackend().registerPubSub(new ProxyPubSub(), Payload.toChannelsArray());

        getProxy().registerChannel("WDL|INIT");

        Core.getLogger().info("\n BACKEND - [SERVER INTERNAL CONFIGURATION] \n\nHas loaded a server!\n\nInformations:\n > Name Room: " + Core.getServerName() + "\n> Type: " + Core.getServerType().name() + "\n");

        Core.getPlatform().runAsync(() ->
                Core.getDataServer().startServer()
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public void loadListenerClasses(Plugin plugin, String pkg) {
        for (Class<?> listenerClass : ClassGetter.getClassesForPackage(plugin.getClass(),
                pkg)) {
            if (Listener.class.isAssignableFrom(listenerClass)) {
                try {
                    Listener listener = (Listener) listenerClass.newInstance();
                    getProxy().getPluginManager().registerListener(plugin, listener);
                    getLogger()
                            .warning("> The '" + listenerClass.getSimpleName() + "' listener was successfully registered.");
                } catch (Exception e) {
                    getLogger().warning("Couldn't load " + listenerClass.getSimpleName() + " listener!");
                }
            }
        }
    }

    public void loadCommandClasses(Plugin plugin, String pkg) {
        ServerCommandFramework framework = new ServerCommandFramework(plugin);
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
    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(this.getDataFolder(), "config.yml");
            if (!file.exists()) {
                file.createNewFile();
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), "config.yml"));
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
