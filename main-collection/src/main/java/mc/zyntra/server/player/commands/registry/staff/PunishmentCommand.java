package mc.zyntra.server.player.commands.registry.staff;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.punishment.constructor.Punishment;
import mc.zyntra.general.account.punishment.constructor.PunishmentCategory;
import mc.zyntra.general.account.punishment.constructor.PunishmentType;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.server.ServerMain;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

public class PunishmentCommand implements CommandClass {

    @Command(
            name = "punish",
            aliases = "punir"
    )
    public void punish(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender commandSender = context.getSender(CommandSender.class);

        ZyntraPlayer zyntraPlayerS = Core.getAccountController().get(commandSender.getName());

        if (!zyntraPlayerS.hasGroup(Group.MODERATOR)) {
            commandSender.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length >= 4) {
            PunishmentType type;
            try {
                type = PunishmentType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                commandSender.sendMessage("§cTipo inválido.");
                return;
            }

            PunishmentCategory category;
            try {
                category = PunishmentCategory.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                commandSender.sendMessage("§cCategoria inválida.");
                return;
            }

            ZyntraPlayer target = Core.getAccountController().get(args[2]);

            if (target == null)
                target = Core.getDataPlayer().load(args[2]);

            if (target == null) {
                commandSender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            if (commandSender instanceof ProxiedPlayer) {
                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(context.getProxiedPlayer().getUniqueId());

                if (!zyntraPlayer.hasGroupPermission(type.getRequiredGroup())) {
                    commandSender.sendMessage("§cVocê não possuí permissão para aplicar uma punição deste tipo.");
                    return;
                }

                if (!zyntraPlayer.hasGroupPermission(target.getPrimaryGroupData().getGroup())) {
                    commandSender.sendMessage("§cEste jogador possuí um grupo superior ou igual ao seu.");
                    return;
                }
            }

            long duration;

            if (args[3].equalsIgnoreCase("n") || args[3].equalsIgnoreCase("never"))
                duration = -1L;
            else {
                try {
                    duration = DateUtils.parseDateDiff(args[3], true);
                } catch (Exception e) {
                    commandSender.sendMessage("§cSintaxe de tempo inválido.");
                    return;
                }
            }

            String reason = args.length == 4
                    ? "Motivo não relatado"
                    : String.join(" ", Arrays.copyOfRange(args, 4, args.length));

            try {
                Punishment punishment = new Punishment(type, category, commandSender.getName(), reason, duration);
                ServerMain.getInstance().getPunishController().punish(target, punishment);

                commandSender.sendMessage("§aVocê aplicou uma punição do tipo " + type.name().toLowerCase() + " na categoria " +
                        category.name().toLowerCase() + " com duração " + (punishment.getExpires() == -1L ? "eterna" : "de " + DateUtils.getTime(punishment.getExpires())) +
                        " ao jogador " + target.getName() + ".");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return;
        }

        commandSender.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <type> <category> <target> <duration> or <never:n> <reason>"));
    }

    @Command(
            name = "cban",
            group = Group.MODERATOR
    )
    public void cban(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender commandSender = context.getSender(CommandSender.class);

        ZyntraPlayer zyntraPlayerS = Core.getAccountController().get(commandSender.getName());

        if (!zyntraPlayerS.hasGroup(Group.MODERATOR)) {
            commandSender.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length >= 1) {
            ZyntraPlayer target = Core.getAccountController().get(args[0]);

            if (target == null)
                target = Core.getDataPlayer().load(args[0]);

            if (target == null) {
                commandSender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            if (commandSender instanceof ProxiedPlayer) {
                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(context.getProxiedPlayer().getUniqueId());

                if (target.hasGroupPermission(zyntraPlayer.getPrimaryGroupData().getGroup())) {
                    commandSender.sendMessage("§cEste jogador possuí um grupo superior ou igual ao seu.");
                    return;
                }
            }

            String reason = args.length == 1
                    ? "Motivo não relatado"
                    : String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            try {
                Punishment punishment = commandSender instanceof ProxiedPlayer
                        ? new Punishment(PunishmentType.BAN, PunishmentCategory.CHEATING, commandSender.getName(), ((ProxiedPlayer) commandSender).getUniqueId(), reason, -1L)
                        : new Punishment(PunishmentType.BAN, PunishmentCategory.CHEATING, commandSender.getName(), reason, -1L);

                ServerMain.getInstance().getPunishController().punish(target, punishment);

                commandSender.sendMessage("§aVocê aplicou uma punição do tipo " + punishment.getType().name().toLowerCase() + " na categoria " +
                        punishment.getCategory().name().toLowerCase() + " com duração " + (punishment.getExpires() == -1L ? "eterna" : "de "
                        + DateUtils.getTime(punishment.getExpires())) + " ao jogador " + target.getName() + ".");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }

        commandSender.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target> <reason>"));
    }

    @Command(
            name = "pardon"
    )
    public void pardon(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender commandSender = context.getSender(CommandSender.class);

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(commandSender.getName());

        if (!zyntraPlayer.hasGroup(Group.MANAGER)) {
            commandSender.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length == 2) {
            ZyntraPlayer target = Core.getAccountController().get(args[0]);

            if (target == null)
                target = Core.getDataPlayer().load(args[0]);

            if (target == null) {
                commandSender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            Punishment punishment = target.getPunishmentHistoric().getPunishment(args[1]);

            if (punishment == null || !punishment.isActive()) {
                commandSender.sendMessage("§cEste jogador não possuí nenhuma punição #" + args[1] + " ativa.");
                return;
            }

            ServerMain.getInstance().getPunishController().revoke(target, args[1], commandSender.getName());
            return;
        }

        commandSender.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target> <type>"));
    }

    @Command(
            name = "kick",
            aliases = "expulsar"
    )
    public void kick(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender commandSender = context.getSender(CommandSender.class);

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(commandSender.getName());

        if (!zyntraPlayer.hasGroup(Group.CREATOR)) {
            commandSender.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        if (args.length >= 1) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

            if (target == null) {
                commandSender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            String reason = args.length == 1 ? "Motivo não relatado" : String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            target.disconnect("§cVocê foi expulso por " + reason);
            commandSender.sendMessage("§aVocê expulsou " + target.getName() + " por " + reason);
            return;
        }

        commandSender.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target> <reason>"));
    }
}
