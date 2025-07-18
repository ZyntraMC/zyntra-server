package mc.zyntra.bukkit.player.commands.registry.player;

import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.medal.enums.Medal;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MedalCommand implements CommandClass {

    @Command(
            name = "medal",
            aliases = {"medalhas"},
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
            String medalName = args[3];

            ZyntraPlayer target = Core.getAccountController().get(targetName);
            if (target == null)
                target = Core.getDataPlayer().load(targetName);

            if (target == null) {
                sender.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            Medal medal = Medal.fromName(medalName);
            if (medal == null) {
                sender.sendMessage("§cA medalha especificada não foi encontrada.");
                return;
            }

            if (action.equals("give")) {
                if (target.getMedalHandler().hasMedal(medal)) {
                    sender.sendMessage("§cO jogador já possui essa medalha.");
                    return;
                }

                target.getMedalHandler().addMedal(medal);
                target.getMedalHandler().setSelectedMedal(medal);
                target.update("medalHandler");

                Player targetPlayer = target.toPlayer().getPlayer();
                if (targetPlayer != null) {
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    NametagController.getInstance().setNametag(targetPlayer, target.getTagHandler().getSelectedTag());
                }

                sender.sendMessage("§aMedalha " + medal.getColor() + medal.getSymbol() + " §aadicionada ao jogador " + target.getName() + ".");
                target.sendMessage("§aVocê recebeu a medalha " + medal.getColor() + medal.getSymbol() + "§a na sua conta.");

            } else if (action.equals("remove")) {
                if (!target.getMedalHandler().hasMedal(medal)) {
                    sender.sendMessage("§cO jogador não possui essa medalha.");
                    return;
                }

                target.getMedalHandler().removeMedal(medal);
                if (target.getMedalHandler().getSelectedMedal() == medal) {
                    target.getMedalHandler().setSelectedMedal(null);
                }

                target.update("medalHandler");

                Player targetPlayer = target.toPlayer().getPlayer();
                if (targetPlayer != null) {
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                    NametagController.getInstance().setNametag(targetPlayer, target.getTagHandler().getSelectedTag());
                }

                sender.sendMessage("§cMedalha " + medal.getColor() + medal.getSymbol() + "§c removida do jogador " + target.getName() + ".");
                target.sendMessage("§cVocê perdeu a medalha " + medal.getColor() + medal.getSymbol() + "§c da sua conta.");

            } else {
                sender.sendMessage("§cAção inválida. Use: give ou remove");
            }

            return;
        }

        // JOGADOR
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 1) {
            Medal medal = Medal.fromName(args[0]);
            if (medal == null) {
                player.sendMessage("§cMedalha não encontrada.");
                return;
            }

            if (!zyntraPlayer.getMedalHandler().hasMedal(medal)) {
                player.sendMessage("§cVocê não possui essa medalha.");
                return;
            }

            if (medal.equals(zyntraPlayer.getMedalHandler().getSelectedMedal())) {
                player.sendMessage("§cVocê já está utilizando essa medalha.");
                return;
            }

            zyntraPlayer.getMedalHandler().setSelectedMedal(medal);
            zyntraPlayer.update("medalHandler");
            NametagController.getInstance().setNametag(player, zyntraPlayer.getTagHandler().getSelectedTag());
            player.sendMessage("§aVocê selecionou a medalha " + medal.getColor() + medal.getSymbol() + "§a.");
            return;
        }

        TextComponent message = new TextComponent(" ");
        boolean hasMedals = !zyntraPlayer.getMedalHandler().getMedals().isEmpty();

        if (!hasMedals) {
            TextComponent error = new TextComponent("§c§lERROR ➜ §r§cVocê não possuí medalhas, adquira alguma em nossa ");
            TextComponent loja = new TextComponent("§b[Loja]");
            loja.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent("§eClique para abrir a loja.")
            }));
            loja.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://zyntramc.com/loja"));
            error.addExtra(loja);

            player.sendMessage("");
            player.spigot().sendMessage(error);
            player.sendMessage("");
            return;
        }

        for (Medal medal : zyntraPlayer.getMedalHandler().getMedals()) {
            TextComponent component = new TextComponent(medal.getColor() + medal.getSymbol());
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent("§eClique para selecionar a medalha " + medal.getColor() + medal.getSymbol())
            }));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/medal " + medal.name().toLowerCase()));
            message.addExtra(component);
            message.addExtra("§f, ");
        }

        player.sendMessage("");
        player.sendMessage(" §eMedalhas disponíveis:");
        player.spigot().sendMessage(message);
        player.sendMessage("");

    }
}
