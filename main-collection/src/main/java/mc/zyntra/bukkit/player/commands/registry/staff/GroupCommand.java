package mc.zyntra.bukkit.player.commands.registry.staff;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.group.GroupAttribute;
import mc.zyntra.general.account.group.GroupCategory;
import mc.zyntra.general.account.group.GroupData;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.networking.PacketOutUpdateAccountGroup;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.general.utils.string.TimeUtils;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCommand implements CommandClass {

    @Command(
            name = "cargo",
            aliases = {"position", "group"},
            group = Group.MANAGER
    )
    public void cargo(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender sender = context.getSender(CommandSender.class);

        ZyntraPlayer target = null;

        if (args.length >= 1) {
            if (sender instanceof Player) {
                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(((Player) sender).getUniqueId());
                if (!zyntraPlayer.hasGroupPermission(Group.MODERATOR)) {
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

            if (args.length > 1) {
                AccountCommandUsage usage = AccountCommandUsage.fromName(args[1]);

                if (usage != null) {
                    if (sender instanceof Player) {
                        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(((Player) sender).getUniqueId());
                        if (!zyntraPlayer.hasGroupPermission(usage.getGroup())) {
                            sender.sendMessage(Constant.COMMAND_NO_ACCESS);
                            return;
                        }
                    }

                    try {
                        usage.getCommandInterface().execute(target, context);
                    } catch (Exception e) {
                        sendHelp(sender);
                    }
                    return;
                }
            }
        }

        sendHelp(sender);
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§eComandos disponíveis:");
        sender.sendMessage("");
        sender.sendMessage("§8• §f/cargo [nickname] give [pos] §8» §7Modifica o cargo do jogador.");
        sender.sendMessage("§8• §f/cargo [nickname] remove [pos] §8» §7Remove o cargo do jogador.");
        sender.sendMessage("");
    }

    @Getter
    @RequiredArgsConstructor
    public enum AccountCommandUsage {

        RANK("give", Group.ADMIN, (target, context) -> {
            CommandSender sender = context.getSender(CommandSender.class);
            String[] args = context.getArguments();

            Group group = Group.fromName(args[2]);
            if (group == null) {
                sender.sendMessage("§cO cargo solicitado não foi encontrado.");
                return;
            }

            if (sender instanceof Player) {
                ZyntraPlayer playerSender = Core.getAccountController().get(((Player) sender).getUniqueId());
                if (!playerSender.hasGroupPermission(group)) {
                    sender.sendMessage("§cVocê não pode dar uma posição maior ou igual a sua.");
                    return;
                }
            }

            boolean isPayment = args[args.length - 1].equalsIgnoreCase("-payment");
            long duration = -1L;

            if (args.length >= 4 && !args[3].equalsIgnoreCase("-payment")) {
                try {
                    duration = TimeUtils.parseDateDiff(args[3], true);
                } catch (Exception ignored) {
                    sender.sendMessage("§cA sintaxe do formato de tempo está incorreta.");
                    return;
                }
            }

            if (isPayment && !group.getCategory().equals(GroupCategory.VIP)) {
                sender.sendMessage("§cVocê não pode utilizar o atributo de pagamento no rank \"" + group.getName().toLowerCase() + "\".");
                return;
            }

            if (target.hasGroup(group)) {
                sender.sendMessage("§cA conta " + target.getName() + " já possui esse rank.");
                return;
            }

            if (group.getTag().isExclusive()) {
                target.getTagHandler().addTag(group.getTag());
            }


            GroupAttribute attribute = isPayment ? GroupAttribute.PAYMENT : (sender instanceof Player ? GroupAttribute.STAFFER : GroupAttribute.SYSTEM);
            String addedBy = sender instanceof Player ? sender.getName() : "API";
            target.addGroup(group, attribute, duration, addedBy);

            target.getTagHandler().setSelectedTag(target.getPrimaryGroupData().getGroup().getTag());
            target.update("selectedTag");
            target.update("groupDataList");

            target.toPlayer().getPlayer().playSound(target.toPlayer().getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            sender.sendMessage("§aVocê adicionou o cargo " + group.getName() + "§a na conta de " + target.getName() + ".");

            GroupData modified = new GroupData(group, attribute, System.currentTimeMillis(), duration, addedBy);
            PacketOutUpdateAccountGroup packet = new PacketOutUpdateAccountGroup(target.getUniqueId(), modified);
            Core.getRedisBackend().publish(Payload.UPDATE_ACCOUNT_GROUP_ADD.name(), Core.getGson().toJson(packet));
        }),

        REMOVE("remove", Group.ADMIN, (target, context) -> {
            CommandSender sender = context.getSender(CommandSender.class);
            String[] args = context.getArguments();

            Group group = Group.fromName(args[2]);
            if (group == null) {
                sender.sendMessage("§cO cargo solicitado não foi encontrado.");
                return;
            }

            if (sender instanceof Player) {
                ZyntraPlayer playerSender = Core.getAccountController().get(((Player) sender).getUniqueId());
                if (!playerSender.hasGroupPermission(group)) {
                    sender.sendMessage("§cVocê não pode remover um cargo maior ou igual ao seu.");
                    return;
                }
            }

            if (!target.hasGroup(group)) {
                sender.sendMessage("§cA conta " + target.getName() + " não possui esse rank.");
                return;
            }

            if (group.getTag().isExclusive()) {
                target.getTagHandler().removeTag(group.getTag());
            }

            GroupData removedData = target.getGroupData(group);

            target.removeGroup(group);

            target.getTagHandler().setSelectedTag(target.getPrimaryGroupData().getGroup().getTag());
            target.update("selectedTag");
            target.update("groupDataList");

            target.toPlayer().getPlayer().playSound(target.toPlayer().getPlayer().getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            sender.sendMessage("§aVocê removeu o cargo " + group.getName() + "§c da conta de " + target.getName() + ".");

            PacketOutUpdateAccountGroup packet = new PacketOutUpdateAccountGroup(target.getUniqueId(), removedData);
            Core.getRedisBackend().publish(Payload.UPDATE_ACCOUNT_GROUP_REMOVE.name(), Core.getGson().toJson(packet));
        });


        private final String name;
        private final Group group;
        private final AccountCommandInterface commandInterface;

        public static AccountCommandUsage fromName(String name) {
            for (AccountCommandUsage usage : values()) {
                if (usage.name().equalsIgnoreCase(name) || usage.getName().equalsIgnoreCase(name))
                    return usage;
            }
            return null;
        }
    }

    public interface AccountCommandInterface {
        void execute(ZyntraPlayer target, CommandContext context);
    }
}
