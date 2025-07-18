package mc.zyntra.bukkit.player.commands.registry.player;

import mc.zyntra.bukkit.BukkitGeneral;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.fake.Fake;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import net.md_5.bungee.api.chat.*;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class FakeCommand implements CommandClass {

    @Command(
            name = "fake",
            group = Group.STREAMER,
            aliases = {"nick", "disfarce"},
            inGameOnly = true
    )
    public void execute(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 0) {
            openRankBook(player);
            return;
        }

        String arg = args[0];

        if (arg.equalsIgnoreCase("random")) {
            if (!zyntraPlayer.getFake().getName().equalsIgnoreCase(zyntraPlayer.getName())) {
                player.sendMessage("§cVocê já está com um nick fake.");
                return;
            }

            if (args.length > 1) {
                String rankArg = args[1];

                Tag rank = Tag.fromName(rankArg);
                if (zyntraPlayer.getPrimaryGroupData().getGroup().ordinal() <= rank.ordinal()) {
                    Fake fake = BukkitGeneral.getFakeAPI().randomFake(rank);
                    zyntraPlayer.setFake(fake);
                    Core.getDataPlayer().save(zyntraPlayer);
                    BukkitGeneral.getFakeAPI().changeFake(player, fake);
                    player.sendMessage("§aVocê agora está usando o nick fake: §f" + fake.getName());
                } else {
                    player.sendMessage("§cVocê não pode usar esse rank.");
                }
                return;
            }

            openRankBook(player);
            return;
        }

        if (arg.equalsIgnoreCase("reset")) {
            if (zyntraPlayer.getFake().getName().equalsIgnoreCase(zyntraPlayer.getName())) {
                player.sendMessage("§cVocê não está usando nenhum nick fake.");
                return;
            }

            Fake fake = new Fake(zyntraPlayer.getName(), zyntraPlayer.getTagHandler().getSelectedTag());
            zyntraPlayer.setFake(fake);
            Core.getDataPlayer().save(zyntraPlayer);
            BukkitGeneral.getFakeAPI().changeFake(player, fake);
            player.sendMessage("§aNick fake removido.");
            return;
        }

        if (arg.equalsIgnoreCase("nick")) {
            openNickInput(player, zyntraPlayer);
            return;
        }

        if (!zyntraPlayer.getFake().getName().equalsIgnoreCase(zyntraPlayer.getName())) {
            player.sendMessage("§cVocê já está com um nick fake.");
            return;
        }

        ZyntraPlayer target = Core.getAccountController().get(arg);
        if (target != null || BukkitGeneral.getFakeAPI().isOriginal(arg)) {
            player.sendMessage("§cEste nick não pode ser usado.");
            return;
        }

        Fake fake = new Fake(arg, Tag.MEMBRO);
        BukkitGeneral.getFakeAPI().changeFake(player, fake);
        zyntraPlayer.setFake(fake);
        Core.getDataPlayer().save(zyntraPlayer);
        player.sendMessage("§aVocê agora está usando o nick fake: §f" + fake.getName());
    }

    private void openRankBook(Player player) {
        TextComponent book = new TextComponent("§aSelecione um rank para seu nick fake:\n");

        for (Tag rank : Tag.values()) {
            if (rank.ordinal() < Tag.EMERALD.ordinal()) continue;

            TextComponent rankComponent = new TextComponent("\n §8§l▪ " + rank.getColor() + "§l" + rank.getName().toUpperCase());
            rankComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    TextComponent.fromLegacyText("§eClique para selecionar este rank")));
            rankComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fake random " + rank.name()));
            book.addExtra(rankComponent);
        }

        TextComponent customNick = new TextComponent("\n\n §7» §bUsar nick personalizado");
        customNick.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                TextComponent.fromLegacyText("§aClique para digitar o nick desejado")));
        customNick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fake nick"));
        book.addExtra(customNick);

        BukkitGeneral.getFakeAPI().openBook(player, book);
    }

    private void openNickInput(Player player, ZyntraPlayer zyntraPlayer) {
        new AnvilGUI.Builder()
                .title("Digite o nick fake")
                .text("")
                .itemLeft(new ItemStack(Material.NAME_TAG))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    if (stateSnapshot.getText().equalsIgnoreCase(zyntraPlayer.getName())) {
                        player.sendMessage("§cVocê não pode usar o seu próprio nick como fake.");
                        return AnvilGUI.Response.close();
                    }

                    if (Core.getAccountController().get(stateSnapshot.getText()) != null || BukkitGeneral.getFakeAPI().isOriginal(stateSnapshot.getText())) {
                        player.sendMessage("§cEste nick não pode ser usado.");
                        return AnvilGUI.Response.close();
                    }

                    if (stateSnapshot.getText().length() < 5 || stateSnapshot.getText().length() > 14) {
                        player.sendMessage("§cO nick deve ter entre 5 e 14 caracteres.");
                        return AnvilGUI.Response.close();
                    }

                    Fake fake = new Fake(stateSnapshot.getText(), Tag.MEMBRO);
                    zyntraPlayer.setFake(fake);
                    Core.getDataPlayer().save(zyntraPlayer);
                    BukkitGeneral.getFakeAPI().changeFake(player, fake);
                    player.sendMessage("§aVocê agora está usando o nick fake: §f" + fake.getName());
                    return AnvilGUI.Response.close();
                })
                .plugin(BukkitMain.getInstance())
                .open(player);
    }
}
