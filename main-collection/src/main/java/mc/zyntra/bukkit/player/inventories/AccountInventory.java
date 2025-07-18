package mc.zyntra.bukkit.player.inventories;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.player.inventories.base.BaseLevel;
import mc.zyntra.bukkit.player.inventories.statistics.StatisticsInventory;
import mc.zyntra.bukkit.player.inventories.preferences.PreferencesInventory;
import mc.zyntra.general.Constant;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.utils.string.StringLoreUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AccountInventory extends MenuInventory {

    public AccountInventory(ZyntraPlayer opener) {
        super("Perfil de " + opener.getName(), 5);

        setItem(22, new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3))
                .setPlayerHead(opener.getName())
                .setName("§aInformações principais")
                .setLore(StringLoreUtils.formatForLore(
                        "§7Nickname: " + opener.getName() + "\n" +
                                "§7Modalidade: " + (opener.getAccountType() != ZyntraPlayer.AccountType.CRACKED ? "§6Original" : "§6Pirata") + "\n\n" +
                                "§7Primeiro login: §f" + Constant.DATE_FORMAT.format(opener.getFirstLogin()) + "\n" +
                                "§7Último login: §f" + Constant.DATE_FORMAT.format(opener.getLastLogin()) + "\n\n" +
                                "§7Tag atual: " + opener.getTagHandler().getSelectedTag().getColoredName() + "\n" +
                                "§7Cargo atual: " + opener.getPrimaryGroupData().getGroup().getColoredName() + "\n\n" +
                                "§7Tipo de cargo: " + (opener.getPrimaryGroupData().getDuration() != -1L ? "§6Temporário" : "§6Permanente") + "\n" +
                                "§7Atribuido em: §f" + Constant.DATE_FORMAT.format(opener.getPrimaryGroupData().getDate()) + "\n" +
                                "§7Acaba em: §f" + Constant.DATE_FORMAT.format(opener.getPrimaryGroupData().getDuration()) + "\n\n" +
                                "§7Para conferir o perfil de outro jogador," + "\n" +
                                "§7utilize o comando §f/perfil <nickname>"
                        ))
                .build());

        setItem(4, new ItemBuilder(Material.EXP_BOTTLE)
                .setName("§aNível")
                .setLore("§7Seu nível atual é §f[" + opener.getLevel() + "§f]",
                        "§7 " + BaseLevel.getProgressBar(opener.getLevel(), opener.getExp()) + " §7- " + opener.getExp() + " EXP a " + BaseLevel.getLevelByNumber(opener.getLevel()).getXpToUp() + " EXP",
                        "",
                        "§7Tenha ainda mais EXP's concluindo",
                        "§7missões dentro do servidor e ganhe",
                        "§7Habilidades, Cosmeticos e muito mais!",
                        "",
                        "§eClique para acessar as missões!")
                .build());

        setItem(29, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .setName("§aConfigurações")
                .setLore("§7Defina suas preferências para ter",
                        "§7a melhor experiência possivel no",
                        "§7servidor!.",
                        "",
                        "§eClique para acessar!")
                .build(), (p, inv, type, stack, slot) -> new PreferencesInventory(opener, false).open(p));

        setItem(11, new ItemBuilder(Material.NETHER_STAR)
                .setPlayerHead(opener.getName())
                .setName("§aPasse de batalha")
                .setLore("§7Confira o seu desempenho na",
                        "§fTemporada 1 §8- §fPasse §2ALPHA",
                        "",
                        "§7Nível atual: §f[" + opener.getBattlePass().getLevelPass() + "§f]",
                        "",
                        "§eClique para acessar o passe!")
                .build());

        setItem(15, new ItemBuilder(Material.PAPER)
                .setName("§aSuas estatísticas")
                .setLore(
                        "§7Explore todo o seu desempenho em",
                        "§7todas as modalidades de jogos da Zyntra.",
                        "",
                        "§eClique para acessar o status!"
                )
                .build(), (p, inv, type, stack, slot) -> new StatisticsInventory(opener, opener, StatisticsInventory.StatisticsMenuType.MAIN, false).open(p));

        setItem(33, new ItemBuilder(Material.ITEM_FRAME)
                .setName("§aPersonalizar aparência")
                .setLore(
                        "§7Cansado de sua skin? modifique",
                        "§7sua aparência conforme goste",
                        "§7dentre todas as skins que possuimos.",
                        "",
                        "§eClique aqui para acessar!")
                .build());

        setItem(40, new ItemBuilder(Material.COOKIE)
                .setName("§aAmigos")
                .setLore(
                        "§7Confira e gerencie suas amizades",
                        "§7sempre que desejar!",
                        "",
                        "§eClique aqui para acessar!")
                .build());
    }
}
