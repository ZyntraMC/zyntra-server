package mc.zyntra.server.player.commands.registry.staff;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.controller.WhitelistController;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class StaffCommand implements CommandClass {

    @Command(
            name = "whitelist",
            aliases = "wl",
            group = Group.ADMIN
    )
    public void whitelist(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender commandSender = context.getSender(CommandSender.class);

        WhitelistController controller = Core.getWhitelistController();

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(commandSender.getName());

        if (!zyntraPlayer.hasGroup(Group.MODERATOR)) {
            commandSender.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "add": {
                    ZyntraPlayer target = Core.getAccountController().get(args[1]);

                    if (target == null)
                        target = Core.getDataPlayer().load(args[1]);

                    if (target == null) {
                        commandSender.sendMessage(Constant.PLAYER_NOT_FOUND);
                        return;
                    }

                    if (controller.containsWhitelist(target.getUniqueId())) {
                        commandSender.sendMessage("§c§lERROR ➜ §r§cEste jogador já está na whitelist.");
                        return;
                    }

                    controller.addWhitelist(target.getUniqueId());
                    commandSender.sendMessage("§aVocê adicionou " + target.getName() + " na whitelist.");
                    break;
                }
                case "remove": {
                    ZyntraPlayer target = Core.getAccountController().get(args[1]);

                    if (target == null)
                        target = Core.getDataPlayer().load(args[1]);

                    if (target == null) {
                        commandSender.sendMessage(Constant.PLAYER_NOT_FOUND);
                        return;
                    }

                    if (!controller.containsWhitelist(target.getUniqueId())) {
                        commandSender.sendMessage("§cEste jogador não está na whitelist.");
                        return;
                    }

                    controller.removeWhitelist(target.getUniqueId());
                    commandSender.sendMessage("§cVocê removeu " + target.getName() + " da whitelist.");
                    break;
                }
                default: {
                    commandSender.sendMessage("§c§lERROR ➜ §r§e" + String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <add, remove, role> <target:role>"));
                    break;
                }
            }
            return;
        }

        controller.setEnabled(!controller.isEnabled());
        commandSender.sendMessage(controller.isEnabled()
                ? "§aVocê habilitou a whitelist."
                : "§cVocê desabilitou a whitelist.");
    }

    @Command(
            name = "broadcast",
            aliases = "bc",
            group = Group.MANAGER
    )
    public void broadcast(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender commandSender = context.getSender(CommandSender.class);

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(commandSender.getName());

        if (!zyntraPlayer.hasGroup(Group.MODERATOR)) {
            commandSender.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length >= 1) {
            ProxyServer.getInstance().broadcast("");
            ProxyServer.getInstance().broadcast("§a Nosso querido" + zyntraPlayer.getPrimaryGroupData().getGroup().getColor() + "[" + zyntraPlayer.getPrimaryGroupData().getGroup().getName() + "] §7" + commandSender.getName() + "§a fez um anúncio! ");
            ProxyServer.getInstance().broadcast("§7➜ " + String.join(" ", Arrays.copyOfRange(args, 0, args.length)));
            ProxyServer.getInstance().broadcast("");
            return;
        }

        commandSender.sendMessage("§c§lERROR ➜ §r§e" + String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <msg>"));
    }

    @Command(
            name = "go",
            inGameOnly = true
    )
    public void go(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (!zyntraPlayer.hasGroup(Group.STREAMER)) {
            player.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length == 1) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
                jedis.setex("bungee-teleport:" + player.getName(), 10, target.getUniqueId().toString());
            }

            player.connect(target.getServer().getInfo());

            return;
        }

        player.sendMessage("§7➜" + String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target>"));
    }

    @Command(
            name = "staffchat",
            aliases = "sc",
            group = Group.MODERATOR,
            inGameOnly = true
    )
    public void staffchat(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (!zyntraPlayer.hasGroup(Group.MODERATOR)) {
            player.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            zyntraPlayer.getConfiguration().setEnabled(ConfigType.SEEING_STAFFCHAT, !zyntraPlayer.getConfiguration().isEnabled(ConfigType.SEEING_STAFFCHAT));
            zyntraPlayer.update("configuration");

            player.sendMessage(zyntraPlayer.getConfiguration().isEnabled(ConfigType.SEEING_STAFFCHAT) ?
                    "§aVocê habilitou a visibilidade do bate-papo da equipe." : "§cVocê desabilitou a visibilidade do bate-papo da equipe.");
            return;
        }

        zyntraPlayer.getConfiguration().setEnabled(ConfigType.STAFFCHAT, !zyntraPlayer.getConfiguration().isEnabled(ConfigType.STAFFCHAT));
        zyntraPlayer.update("configuration");

        player.sendMessage(zyntraPlayer.getConfiguration().isEnabled(ConfigType.STAFFCHAT) ?
                "§aVocê entrou no bate-papo da equipe." : "§cVocê saiu do bate-papo da equipe.");
    }
}