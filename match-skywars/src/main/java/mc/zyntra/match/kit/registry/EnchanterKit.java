package mc.zyntra.match.kit.registry;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;


public class EnchanterKit extends Kit {

    public EnchanterKit() {
        super("Enchanter", 20000);

        setIcon(new ItemBuilder(Material.ENCHANTMENT_TABLE).setName("§aEnchanter").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Encante seus itens", "§7imediatamente ao iniciar", "§7sua partida e derrote seus oponentes!", "", "§eItens:", " §7Bigorna"," §7Frascos de experiência 64x" ," §7Livro encantado", "  §8Proteção I", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.ANVIL).setAmount(1).build(), false);
        addItem(new ItemBuilder(Material.EXP_BOTTLE).setAmount(64).build(), false);

        ItemStack ENCHANTED = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) ENCHANTED.getItemMeta();
        switch ((int) (Math.random() * 4)) {
            case 0:
                meta.addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false);
                break;
            case 1:
                meta.addStoredEnchant(Enchantment.DAMAGE_ALL, 1, false);
                break;
            case 2:
                meta.addStoredEnchant(Enchantment.DIG_SPEED, 4, false);
                break;
            case 3:
                meta.addStoredEnchant(Enchantment.DURABILITY, 1, false);
                break;
        }
        ENCHANTED.setItemMeta(meta);
        addItem(new ItemBuilder(ENCHANTED).setAmount(1).build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
