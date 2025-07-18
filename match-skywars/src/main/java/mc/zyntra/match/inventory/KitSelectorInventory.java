package mc.zyntra.match.inventory;

import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.MenuItem;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.util.Platform;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.kit.Kit;
import mc.zyntra.match.kit.loader.KitLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class KitSelectorInventory extends MenuInventory {

    public KitSelectorInventory(Player opener, int page) {
        super("Kits", 6);
        List<MenuItem> itens = new ArrayList<>();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(opener.getUniqueId());
        int ITEMS_PER_PAGE;
        int PREVIOUS_PAGE_SLOT;
        int NEXT_PAGE_SLOT;
        int CENTER;

        int ABILITIES_PER_ROW;

        ITEMS_PER_PAGE = 21;
        PREVIOUS_PAGE_SLOT = 40;
        NEXT_PAGE_SLOT = 50;
        CENTER = 22;

        ABILITIES_PER_ROW = 7;

        List<Kit> kitList = new ArrayList<>(KitLoader.getAbilities().values());
        KitLoader a = Platform.getKitLoader();
        for (Kit kit : kitList) {
            if (matchPlayer.getKit().equalsIgnoreCase(kit.getName())) {
                ItemStack item = kit.getIcon();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§c" + kit.getName());
                item.setItemMeta(meta);
                itens.add(new MenuItem(new ItemBuilder(item).build()));
            } else if (Objects.equals(kit.getName(), matchPlayer.getKit())) {
                ItemStack item = kit.getIcon();
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("§c" + kit.getName());
                item.setItemMeta(meta);
                itens.remove(item);
            } else {
                for (int i = 0; i < kit.getDescription().size(); i++) {
                    String elemento = kit.getDescription().get(i);
                    elemento = elemento.replace("%info%", (matchPlayer.getKit().equalsIgnoreCase(kit.getName()) ? "§cVocê já está utilizando este kit!" : "§eClique para selecionar este kit!"));
                    kit.getDescription().set(i, elemento);
                }
                itens.add(new MenuItem(new ItemBuilder(kit.getIcon()).setName("§a" + kit.getName()).setLore(kit.getDescription())
                        .build(), (p, inv, type, stack, itemSlot) -> {
                    p.closeInventory();
                    p.performCommand("abilities " + kit.getName());
                }));
            }
        }

        int PAGE_START = 0;
        int PAGE_END = ITEMS_PER_PAGE;

        if (page > 1) {
            PAGE_START = ((page - 1) * ITEMS_PER_PAGE);
            PAGE_END = (page * ITEMS_PER_PAGE);
        }

        if (PAGE_END > itens.size()) {
            PAGE_END = itens.size();
        }

        if (page == 1) {
            setItem(PREVIOUS_PAGE_SLOT, new ItemBuilder(Material.AIR).build());
        } else {
            setItem(PREVIOUS_PAGE_SLOT, new ItemBuilder(Material.ARROW).setName("§aPágina anterior").build(), (p, inv, type, stack, itemSlot) -> {
                p.closeInventory();
                new KitSelectorInventory(p, page - 1).open(p);
            });
        }

        if ((itens.size() / ITEMS_PER_PAGE) + 1 <= page) {
            setItem(NEXT_PAGE_SLOT, new ItemBuilder(Material.AIR).build());
        } else {
            setItem(NEXT_PAGE_SLOT, new ItemBuilder(Material.ARROW).setName("§aPróxima página").build(), (p, inv, type, stack, itemSlot) -> {
                p.closeInventory();
                new KitSelectorInventory(p, page + 1).open(p);
            });
        }

        int ABILITY_SLOT = 10;

        for (int i = PAGE_START; i < PAGE_END; i++) {
            setItem(itens.get(i), ABILITY_SLOT);

            if (getItem(ABILITY_SLOT).getStack().isSimilar(null) || getItem(ABILITY_SLOT).getStack().isSimilar(new ItemStack(Material.AIR))) {
                setItem(ABILITY_SLOT, new ItemBuilder(Material.INK_SACK).setName("").build());
            }

            if (ABILITY_SLOT % 9 == ABILITIES_PER_ROW) {
                ABILITY_SLOT += 3;
                continue;
            }

            ABILITY_SLOT += 1;
        }

        int startSlot, endSlot;

        startSlot = 45;
        endSlot = 53;

        for (int slot = startSlot; slot <= endSlot; slot++) {
            if (getItem(slot) == null || getItem(slot).getStack().isSimilar(new ItemStack(Material.AIR))) {
                setItem(slot, new ItemBuilder(Material.STAINED_GLASS_PANE).setName("").setDurability(15).build());
            }
        }

        if (itens.isEmpty()) {
            setItem(CENTER, new ItemBuilder(Material.WEB).setName("§cVocê não possui nenhum kit!").build());
        }

        setItem(48, new ItemBuilder(Material.EMERALD).setName("§aLoja").setLore("§eClique para acessar!").build());
        setItem(50, new ItemBuilder(Material.SKULL_ITEM).setName("§aPerks").setLore("§eClique para acessar!").build());
    }
}
