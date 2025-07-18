package mc.zyntra.match.kit.registry;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.Leading;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;


public class WarrerKit extends Kit {

    public WarrerKit() {
        super("Guerreiro", 15000);

        setIcon(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName("§aGuerreiro").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Com sua espada e seu colete trajado", "§7avance o mais rapido e elimine", "§7todos seus oponentes em velocidade maxima!", "", "§eItens:", "§7 Espada de diamante", "  §8Afiação I", "§7 Peitoral de diamante", "  §8Proteção I", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).setName("§cEspada feroz!").build(), false);
        addItem(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).setName("§cEspada feroz!").build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
