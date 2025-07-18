package mc.zyntra.match.kit.registry;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.Leading;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;


public class EcologistKit extends Kit {

    public EcologistKit() {
        super("Ecologista", 15000);

        setIcon(new ItemBuilder(Material.DIAMOND_AXE).setName("§aEcologista").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Com seu machado feroz elimine", "§7os oponentes com sua eficiencia", "§7em combate e vença a partida!", "", "§eItens:", "§7 Machado de diamante", "  §8Afiação I", "  §8Eficiência I", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.DIAMOND_AXE).addEnchant(Enchantment.DAMAGE_ALL, 1).setName("§cMachado Ecologista!").build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
