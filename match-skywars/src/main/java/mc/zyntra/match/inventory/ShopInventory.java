package mc.zyntra.match.inventory;

import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.MenuItem;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.util.Platform;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.kit.Kit;
import mc.zyntra.match.kit.loader.KitLoader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ShopInventory extends MenuInventory {

    public ShopInventory(Player opener, MenuType menuType, int page) {
        super("Loja", (menuType == MenuType.MAIN ? 4 : 6));

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(opener.getUniqueId());
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(opener.getUniqueId());

        switch (menuType) {
            case MAIN: {
                setTitle("Loja");
                setItem(12, new ItemBuilder(Material.IRON_SWORD).setName("§aKits").build(),
                        (p, inv, type, stack, slot) -> new ShopInventory(p, MenuType.KITS, 0).open(p));

                setItem(14, new ItemBuilder(Material.SKULL_ITEM).setDurability(2).setName("§aPerks").build());
                setItem(31, new ItemBuilder(Material.EMERALD).setName("§7Suas coins: §6" + matchPlayer.getStatistics().getCoins()).build());
                break;
            }
            case KITS: {
                setTitle("Kits");
                List<MenuItem> itens = new ArrayList<>();
                int ITEMS_PER_PAGE;
                int PREVIOUS_PAGE_SLOT;
                int NEXT_PAGE_SLOT;
                int CENTER;

                int ABILITIES_PER_ROW;

                ITEMS_PER_PAGE = 21;
                PREVIOUS_PAGE_SLOT = 40;
                NEXT_PAGE_SLOT = 41;
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
                        new ShopInventory(p, MenuType.KITS, page - 1).open(p);
                    });
                }

                if ((itens.size() / ITEMS_PER_PAGE) + 1 <= page) {
                    setItem(NEXT_PAGE_SLOT, new ItemBuilder(Material.AIR).build());
                } else {
                    setItem(NEXT_PAGE_SLOT, new ItemBuilder(Material.ARROW).setName("§aPróxima página").build(), (p, inv, type, stack, itemSlot) -> {
                        p.closeInventory();
                        new ShopInventory(p, MenuType.KITS, page + 1).open(p);
                    });
                }

                int ABILITY_SLOT = 10;

                for (int i = PAGE_START; i < PAGE_END; i++) {
                    setItem(itens.get(i), ABILITY_SLOT);

                    if (ABILITY_SLOT % 9 == ABILITIES_PER_ROW) {
                        ABILITY_SLOT += 3;
                        continue;
                    }

                    ABILITY_SLOT += 1;
                }

                if (itens.isEmpty()) {
                    setItem(CENTER, new ItemBuilder(Material.WEB).setName("§cVocê não possui nenhum kit!").build());
                }

                setItem(43, new ItemBuilder(Material.ARROW).setName("§aVoltar").setLore("§7Para Loja").build(), (p, inv, type, stack, itemSlot) -> {
                    p.closeInventory();
                    new ShopInventory(p, MenuType.MAIN, 0).open(p);
                });
                setItem(44, new ItemBuilder(Material.EMERALD).setName("§7Suas coins: §6" + matchPlayer.getStatistics().getCoins()).build());
                break;
            }
            case PERKS: {
                setTitle("Perks");
                setItem(22, new ItemBuilder(Material.WEB).setName("§cVocê não possui nenhuma perks!").build());
                setItem(43, new ItemBuilder(Material.ARROW).setName("§aVoltar").setLore("§7Para Loja").build(), (p, inv, type, stack, itemSlot) -> {
                    p.closeInventory();
                    new ShopInventory(p, MenuType.MAIN, 0).open(p);
                });
                setItem(44, new ItemBuilder(Material.EMERALD).setName("§7Suas coins: §6" + matchPlayer.getStatistics().getCoins()).build());
                break;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public enum MenuType {
        MAIN, KITS, PERKS;
    }
}
