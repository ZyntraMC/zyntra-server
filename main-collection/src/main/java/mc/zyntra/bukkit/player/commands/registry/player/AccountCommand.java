package mc.zyntra.bukkit.player.commands.registry.player;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.utils.string.DateUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccountCommand implements CommandClass {

    @Command(
            name = "account",
            aliases = "acc"
    )
    public void account(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender sender = context.getSender(CommandSender.class);

        ZyntraPlayer target = null;

        if (sender instanceof Player)
            target = Core.getAccountController().get(((Player) sender).getUniqueId());

        if (args.length >= 1) {
            if (sender instanceof Player) {
                if (!Core.getAccountController().get(((Player) sender).getUniqueId()).hasGroupPermission(Group.MODERATOR)) {
                    sender.sendMessage(Constant.COMMAND_NO_ACCESS);
                    return;
                }
            }

            target = Core.getAccountController().get(args[0]);

            if (target == null)
                target = Core.getDataPlayer().load(args[0]);

            if (target == null) {
                sender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }
        }

        if (target == null) {
            sender.sendMessage("§eComandos disponíveis:");
            sender.sendMessage("");
            sender.sendMessage("§e/conta [Jogador] §8» §7Veja as informações da conta do jogador.");
            sender.sendMessage("");
            sender.sendMessage("§ePágina §f1 §ede §f1§e.");
            return;
        }

        ZyntraPlayer senderCommand = Core.getAccountController().get(((Player) sender).getUniqueId());
        sender.sendMessage("§eInformações de: " + target.getPrimaryGroupData().getGroup().getColor() + target.getName());
        sender.sendMessage("  §fNick: §7" + target.getName());
        sender.sendMessage("  §fPosição: " + target.getPrimaryGroupData().getGroup().getColor() + target.getPrimaryGroupData().getGroup().getName() +
                " §e(" + target.getPrimaryGroupData().getAttribute().getName() + ")");
        sender.sendMessage("  §fDuração do rank: §7" + (target.getPrimaryGroupData().getDuration() == -1 ? "Vitalício" :
                DateUtils.getTime(target.getPrimaryGroupData().getDuration())));
        if (senderCommand.hasGroupPermission(Group.CREATOR)) {
            sender.sendMessage("  §fAdicionado por: §7" + target.getPrimaryGroupData().getAddedBy());
            sender.sendMessage("  §fAdicionado em: §7" + Constant.DATE_FORMAT.format(target.getPrimaryGroupData().getDate()));
            if (target.hasDisguise()) sender.sendMessage("  §fFake: §7" + target.getDisguise());
        }
        sender.sendMessage("  §fTipo de conta: §7" + (target.getAccountType().equals(ZyntraPlayer.AccountType.PREMIUM) ? "§aOriginal" : "§7Pirata"));
        sender.sendMessage("");
        sender.sendMessage("  §fÚltimo acesso: §7" + Constant.DATE_FORMAT.format(target.getLastLogin()));
        sender.sendMessage("  §fPrimeiro acesso: §7" + Constant.DATE_FORMAT.format(target.getFirstLogin()));
        sender.sendMessage("");
    }
}
