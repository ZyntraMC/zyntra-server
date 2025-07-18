package mc.zyntra.bukkit.player.inventories.preferences;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.click.MenuClickHandler;
import mc.zyntra.bukkit.player.inventories.AccountInventory;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.AccountConfiguration;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.utils.string.StringLoreUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class PreferencesInventory extends MenuInventory {

    public PreferencesInventory(ZyntraPlayer opener, boolean cameByCommand) {
        super("Suas preferências", 4);

        build(10, Material.SIGN, "Mensagens privadas",
                "Permita o recebimento de mensagens privadas.", opener, ConfigType.TELL);
        build(11, Material.BOOK_AND_QUILL, "Bate-papo",
                "Permita a visualização do bate-papo.", opener, ConfigType.CHAT);
        build(12, Material.HOPPER, "Visibilidade das estatísticas",
                "Permita que outros jogadores visualizem suas estatísticas.", opener, ConfigType.STATISTICS_VISIBILITY);

        if (opener.hasGroupPermission(Group.CREATOR)) {
            build(13, Material.IRON_FENCE, "Entrar no vanish",
                    "Entre automaticamente no modo vanish ao entrar.", opener, ConfigType.ADMIN_ON_JOIN);
            build(14, Material.PAPER, "Visiblidade do bate-papo da equipe",
                    "Permita a visualização do bate-papo da equipe.", opener, ConfigType.SEEING_STAFFCHAT);
            build(15, Material.EMPTY_MAP, "Logs",
                    "Permita o recebimento de logs.", opener, ConfigType.SEEING_LOGS);
            build(16, Material.ANVIL, "Alertas do anticheat",
                    "Permita o recebimento de alertas do anticheat.", opener, ConfigType.SEEING_ANTICHEAT);
        }

        if (!cameByCommand) {
            setItem(31, new ItemBuilder(Material.ARROW)
                    .setName("§aVoltar").build(), (p, inv, type, stack, slot) ->
                    new AccountInventory(opener).open(p));
        }
    }

    private void build(int slot, Material material, String name, String description, ZyntraPlayer zyntraPlayer, ConfigType configType) {
        AccountConfiguration configuration = zyntraPlayer.getConfiguration();

        MenuClickHandler menuClickHandler = (player, inv, type, stack, var1) -> {
            configuration.setEnabled(configType, !configuration.isEnabled(configType));
            zyntraPlayer.update("configuration");

            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 5.5F, 5.5F);
            build(slot, material, name, description, zyntraPlayer, configType);
        };

        setItem(slot, new ItemBuilder(material)
                .setName((configuration.isEnabled(configType) ? "§a" : "§c") + name)
                .setLore(StringLoreUtils.formatForLore("§7" + description))
                .build(), menuClickHandler);
        setItem(slot + 9, new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, configuration.isEnabled(configType) ? (byte) 5 : (byte) 7))
                .setName((configuration.isEnabled(configType) ? "§a" : "§c") + name)
                .setLore(StringLoreUtils.formatForLore("§7" + (configuration.isEnabled(configType) ? "Clique para desativar." : "Clique para ativar.")))
                .build(), menuClickHandler);
    }
}
