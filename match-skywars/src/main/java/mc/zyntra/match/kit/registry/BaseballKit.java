package mc.zyntra.match.kit.registry;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;


public class BaseballKit extends Kit {

    public BaseballKit() {
        super("Baseball", 15000);

        setIcon(new ItemBuilder(Material.IRON_HELMET).setName("§aBaseball").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Jogue seus oponentes para longe", "§7como se estivesse batendo", "§7uma bola com um taco de baseball!", "", "§eItens:", " §7Capacete de Ferro", "  §8Proteção I", " §7Espada de Madeira", "  §8Repulsão I", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.IRON_HELMET).setAmount(1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(), false);
        addItem(new ItemBuilder(Material.WOOD_SWORD).setAmount(1).addEnchant(Enchantment.DAMAGE_ALL, 1).build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
