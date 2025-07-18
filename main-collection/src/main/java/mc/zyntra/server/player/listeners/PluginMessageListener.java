package mc.zyntra.server.player.listeners;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.ServerConfiguration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static net.md_5.bungee.api.chat.TextComponent.fromLegacyText;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("BungeeCord")) {
            return;
        }

        if (!(event.getReceiver() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String action = in.readUTF();

            if (action.equalsIgnoreCase("SearchServer")) {
                String type = in.readUTF();
                ServerType serverType = ServerType.valueOf(type);

                if (!searchServer(player, Core.getAccountController().get(player.getUniqueId()), serverType)) {
                    player.sendMessage("§c'redirect': Não foi possivel encontrar um servidor do tipo " + serverType.name().toLowerCase().replace("_", " ") + " disponível no momento.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean searchServer(ProxiedPlayer player, ZyntraPlayer zyntraPlayer, ServerType serverType) {
        ServerConfiguration serverConfiguration = Core.getDataServer().getServers(serverType)
                .stream()
                .findFirst()
                .orElse(null);

        if (serverConfiguration == null || ProxyServer.getInstance().getServerInfo(serverConfiguration.getName()) == null)
            return false;

        if (serverConfiguration.isRestricted() && !zyntraPlayer.hasGroupPermission(Group.CREATOR)) {
            zyntraPlayer.sendMessage(fromLegacyText("§cA sala solicitada está disponível apenas para nossa equipe."));
            return true;
        }

        if (serverConfiguration.getOnlineCount() >= serverConfiguration.getMaxPlayers() && !zyntraPlayer.hasGroupPermission(Group.GOLD)) {
            zyntraPlayer.sendMessage(fromLegacyText("§cA sala solicitada está cheia."));
            return true;
        }

        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverConfiguration.getName());
        player.connect(serverInfo);
        return true;
    }

    @EventHandler
    public void onWDL(PluginMessageEvent event) {
        String tag = event.getTag();

        Connection connection = event.getReceiver();

        if (tag.equalsIgnoreCase("WDL|INIT")) {
            connection.disconnect(fromLegacyText("§cVocê naõ pode baixar os mapas do servidor!"));
        }
    }
}
