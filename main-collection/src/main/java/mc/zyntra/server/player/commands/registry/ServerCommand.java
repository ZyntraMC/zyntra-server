package mc.zyntra.server.player.commands.registry;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.command.Completer;
import mc.zyntra.general.server.ServerCategory;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.server.types.MinigameServerConfiguration;
import mc.zyntra.general.server.types.MinigameStage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class ServerCommand implements CommandClass {

    @Command(
            name = "lobby",
            aliases = {"l", "hub"},
            inGameOnly = true
    )
    public void lobby(CommandContext context) {
        ProxiedPlayer player = context.getProxiedPlayer();

        ServerType serverType = ServerType.LOBBY;
        ServerConfiguration currentServer = Core.getDataServer().getServer(player.getServer().getInfo().getName());

        if (currentServer != null && currentServer.getServerType().getLobbyServer() != null)
            serverType = currentServer.getServerType().getLobbyServer();

        ServerConfiguration server = Core.getDataServer().getServers(serverType)
                .stream()
                .filter(serverConfiguration -> serverConfiguration.getOnlineCount() < serverConfiguration.getMaxPlayers()
                        && !serverConfiguration.isRestricted())
                .findFirst()
                .orElse(null);

        if (server == null || ProxyServer.getInstance().getServerInfo(server.getName()) == null) {
            player.sendMessage(String.format(Constant.SERVER_NOT_FOUND, serverType.getName().toLowerCase()));
            return;
        }

        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getName());
        player.connect(serverInfo);
    }

    @Command(
            name = "play",
            aliases = {"jogar"},
            inGameOnly = true
    )
    public void play(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();

        List<ServerType> games = new ArrayList<>();
        for (ServerType serverType : ServerType.values()) {
            if (serverType.getCategory() == ServerCategory.GAME)
                games.add(serverType);
        }

        if (args.length == 1) {
            ServerType serverType = null;
            for (ServerType type : games) {
                if (type.name().equalsIgnoreCase(args[0]))
                    serverType = type;
            }

            if (serverType == null) {
                player.sendMessage("§cJogo não encontrado.");
                return;
            }

            MinigameServerConfiguration server = Core.getDataServer().getMinigameServers(serverType)
                    .stream()
                    .filter(serverConfiguration -> !serverConfiguration.isRestricted() &&
                            serverConfiguration.getStage().equals(MinigameStage.WAITING_FOR_PLAYERS) ||
                            serverConfiguration.getStage().equals(MinigameStage.STARTING) &&
                                    serverConfiguration.getOnlineCount() < serverConfiguration.getMaxPlayers())
                    .findFirst()
                    .orElse(null);

            if (server == null || ProxyServer.getInstance().getServerInfo(server.getName()) == null) {
                player.sendMessage(String.format(Constant.SERVER_NOT_FOUND, serverType.getName().toLowerCase()));
                return;
            }

            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server.getName());
            player.connect(serverInfo);
            return;
        }

        TextComponent message = new TextComponent("§eJogos disponíveis: ");

        for (ServerType serverType : games) {
            TextComponent component = new TextComponent("§6" + serverType.getName().toLowerCase());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent("/play " + serverType.name().toLowerCase())
            }));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play " + serverType.name().toLowerCase()));
            message.addExtra(component);
            message.addExtra(games.get(games.size() - 1).equals(serverType) ? "§f." : "§f, ");
        }

        player.sendMessage(message);
    }

    @Completer(
            name = "play",
            aliases = "jogar"
    )
    public List<String> playCompleter(CommandContext context) {
        List<String> list = new ArrayList<>();
        for (ServerType serverType : ServerType.values()) {
            if (serverType.getCategory() == ServerCategory.GAME)
                list.add(serverType.name().toLowerCase());
        }
        return list;
    }
}
