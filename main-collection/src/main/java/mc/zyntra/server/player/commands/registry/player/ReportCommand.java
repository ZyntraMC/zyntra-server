package mc.zyntra.server.player.commands.registry.player;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.punishment.constructor.Punishment;
import mc.zyntra.general.account.punishment.constructor.PunishmentType;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.utils.component.TextComponentBuilder;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

public class ReportCommand implements CommandClass {

    @Command(
            name = "report",
            aliases = "rp",
            inGameOnly = true
    )
    public void report(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length >= 2) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            if (zyntraPlayer.getPunishmentHistoric().getActivePunishment(PunishmentType.BLOCK_REPORT) != null) {
                Punishment punishment = zyntraPlayer.getPunishmentHistoric().getActivePunishment(PunishmentType.BLOCK_REPORT);
                player.sendMessage("§c§lERROR ➜ §r§cSua conta está impossibilitada de denunciar jogadores por " + punishment.getCategory().getName() +
                        (punishment.getExpires() == -1L ? "" : " com duração de " + DateUtils.getTime(punishment.getExpires())) + ".");
                return;
            }

            if (zyntraPlayer.hasCooldown("report")) {
                player.sendMessage("§c§lERROR ➜§r§cAguarde " + DateUtils.getTime(zyntraPlayer.getCooldown("report")) +
                        " para relatar uma nova denúncia novamente.");
                return;
            }

            String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            Core.getAccountController().list().stream()
                    .filter(p -> p.hasGroupPermission(Group.CREATOR) && p.getConfiguration().isEnabled(ConfigType.SEEING_LOGS))
                    .forEach(p -> p.sendMessage(new TextComponentBuilder("§cReport> §8(" + target.getServer().getInfo().getName() + ") §f" + player.getName() +
                            " §cdenunciou §f" + target.getName() + " §cpor §f" + reason + "§c.")
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eIr até ao jogador")))
                            .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/go " + target.getName()))
                            .build()));

            zyntraPlayer.addCooldown("report", 15);

            player.sendMessage("§aSua denúncia foi enviada para nossa equipe. Iremos revisar a sua denúncia o mais rápido possível.");
            return;
        }

        player.sendMessage("§c§lERROR ➜ "+ String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target> <reason>"));
    }
}
