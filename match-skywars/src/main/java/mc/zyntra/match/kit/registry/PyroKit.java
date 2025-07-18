package mc.zyntra.match.kit.registry;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.Leading;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;


public class PyroKit extends Kit {

    public PyroKit() {
        super("Pyro", 20000);

        setIcon(new ItemBuilder(Material.FLINT_AND_STEEL).setName("§aPyro").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Se torne um piromaniaco", "§7e mate todos queimados", "§7na sua batalha!", "", "§eItens:", " §7Isqueiro", " §7Balde de lava 5x", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.FLINT_AND_STEEL).setAmount(1).build(), false);
        addItem(new ItemBuilder(Material.LAVA_BUCKET).setAmount(5).build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
