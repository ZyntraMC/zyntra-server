package mc.zyntra.bukkit.player.commands.registry.player;

import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandClass {

    @Command(
            name = "tag",
            aliases = {"tags", "cargos"},
            inGameOnly = true
    )
    public void execute(CommandContext context) {
        String[] args = context.getArguments();
        CommandSender sender = context.getSender(CommandSender.class);

        // ADMIN MODE
        if (args.length >= 4 && args[0].equalsIgnoreCase("-admin")) {

            ZyntraPlayer admin = Core.getAccountController().get(sender.getName());
            if (!admin.hasGroupPermission(Group.MODERATOR)) {
                sender.sendMessage(Constant.COMMAND_NO_ACCESS);
                return;
            }

            String targetName = args[1];
            String action = args[2].toLowerCase();
            String tagName = args[3];

            ZyntraPlayer target = Core.getAccountController().get(targetName);
            if (target == null)
                target = Core.getDataPlayer().load(targetName);

            if (target == null) {
                sender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            Tag tag = Tag.fromName(tagName);
            if (tag == null) {
                sender.sendMessage("§cA tag especificada não foi encontrada.");
                return;
            }

            if (!tag.isExclusive()) {
                sender.sendMessage("§cApenas tags exclusivas podem ser gerenciadas.");
                return;
            }

            if (action.equals("give")) {
                if (target.getTagHandler().hasTag(tag)) {
                    sender.sendMessage("§cO jogador já possui essa tag.");
                    return;
                }

                target.getTagHandler().addTag(tag);
                target.getTagHandler().setSelectedTag(tag);
                target.update("tagHandler");

                NametagController.getInstance().setNametag(target.toPlayer().getPlayer(), tag);
                target.toPlayer().getPlayer().playSound(target.toPlayer().getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                sender.sendMessage("§aTag " + tag.getColoredName() + " §aadicionada ao jogador " + target.getName() + ".");
                target.sendMessage("§aVocê recebeu a tag " + tag.getColoredName() + "§a na sua conta.");

            } else if (action.equals("remove")) {
                if (!target.getTagHandler().hasTag(tag)) {
                    sender.sendMessage("§cO jogador não possui essa tag.");
                    return;
                }

                target.getTagHandler().removeTag(tag);
                if (target.getTagHandler().getSelectedTag() == tag) {
                    target.getTagHandler().setSelectedTag(Tag.MEMBRO);
                }

                target.update("tagHandler");
                NametagController.getInstance().setNametag(target.toPlayer().getPlayer(), Tag.MEMBRO);

                target.toPlayer().getPlayer().playSound(target.toPlayer().getPlayer().getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                sender.sendMessage("§cTag " + tag.getColoredName() + "§c removida do jogador " + target.getName() + ".");
                target.sendMessage("§cVocê perdeu a tag " + tag.getColoredName() + "§c da sua conta.");

            } else {
                sender.sendMessage("§cAção inválida. Use: give ou remove");
            }

            return;
        }

        // NORMAL PLAYER MODE
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 1) {
            Tag tag = Tag.fromName(args[0]);
            if (tag == null) {
                player.sendMessage("§cTag não encontrada.");
                return;
            }

            Group groupWithTag = null;
            for (Group group : Group.values()) {
                if (group.getTag() == tag) {
                    groupWithTag = group;
                    break;
                }
            }

            if (groupWithTag != null) {
                if (!zyntraPlayer.hasGroupPermission(groupWithTag)) {
                    player.sendMessage("§cVocê não possui esta tag.");
                    return;
                }
            } else {
                if (tag.isExclusive() && !zyntraPlayer.getTagHandler().hasTag(tag)) {
                    player.sendMessage("§cVocê não possui esta tag.");
                    return;
                }
            }

            if (zyntraPlayer.getTagHandler().getSelectedTag().equals(tag)) {
                player.sendMessage("§cVocê já está utilizando esta tag.");
                return;
            }

            zyntraPlayer.getTagHandler().setSelectedTag(tag);
            zyntraPlayer.update("tagHandler");
            NametagController.getInstance().setNametag(player, tag);
            player.sendMessage("§aVocê selecionou a tag " + tag.getColor() + tag.getName() + "§a.");
            return;
        }

        TextComponent message = new TextComponent(" ");
        boolean hasOtherTags = false;

        for (Tag tag : Tag.values()) {
            if (tag.equals(Tag.MEMBRO)) continue;

            boolean canUse = false;
            if (tag.isExclusive()) {
                canUse = zyntraPlayer.getTagHandler().hasTag(tag);
            } else {
                for (Group group : Group.values()) {
                    if (group.getTag() == tag && zyntraPlayer.hasGroupPermission(group)) {
                        canUse = true;
                        break;
                    }
                }
            }

            if (tag.name().equalsIgnoreCase("MEMBRO")) {
                canUse = true;
            }

            if (!canUse) continue;

            if (!tag.name().equalsIgnoreCase("MEMBRO")) {
                hasOtherTags = true;
            }

            TextComponent component = new TextComponent(tag.getColor() + tag.getName());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                    new TextComponent("§eComo ficará: " +
                            (tag == Tag.MEMBRO ? tag.getColor() : tag.getColor() + "[" + tag.getName() + "] §7")
                            + zyntraPlayer.getName() + "\n\n§aClique para selecionar.")
            }));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag.getName().toLowerCase()));
            message.addExtra(component);
            message.addExtra("§f, ");
        }

        if (!hasOtherTags) {
            TextComponent error = new TextComponent("§c§lERROR ➜ §r§cVocê não possuí tags, adquira alguma em nossa ");
            TextComponent loja = new TextComponent("§b[Loja]");
            loja.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                    new TextComponent("§eClique para abrir a loja.")
            }));
            loja.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://zyntramc.com/loja"));
            error.addExtra(loja);

            player.sendMessage("");
            player.spigot().sendMessage(error);
            player.sendMessage("");
            return;
        }

        player.sendMessage("");
        player.sendMessage(" §eTags disponíveis:");
        player.spigot().sendMessage(message);
        player.sendMessage("");
    }
}
