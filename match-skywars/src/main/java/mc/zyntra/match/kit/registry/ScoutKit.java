package mc.zyntra.match.kit.registry;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.Leading;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;


public class ScoutKit extends Kit {

    public ScoutKit() {
        super("Velocista", 5000);

        setIcon(new ItemBuilder(Material.POTION).setDurability((short) 8226).setName("§aVelocista").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Adquira o poder do flash e corra", "§7o mais rapido possivel para cima", "§7dos seus oponentes em batalha!", "", "§eItens:", "§7 Poção (Arremesável) 3x", "  §8Velocidade II (0:20)", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        addItem(new ItemBuilder(Material.POTION).setDurability((short) 8226).setAmount(3).build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
