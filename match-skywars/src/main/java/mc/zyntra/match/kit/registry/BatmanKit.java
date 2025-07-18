package mc.zyntra.match.kit.registry;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;


public class BatmanKit extends Kit {

    public BatmanKit() {
        super("Batman", 5000);

        setIcon(new ItemBuilder(Material.MONSTER_EGG).setDurability(EntityType.BAT.getTypeId()).setName("§aBatman").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Seja o batman em sua partida.", "§7Cause cegueira em seus oponentes", "§7e jogue seus morcegos para afeta-los!", "", "§eItens:", "§7 Poção (Arremesável) 2x", "  §8Cegueira I (0:10)", "§7 Ovo de morcego 5x", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));
        ItemStack potion = new ItemStack(Material.POTION, 2, (short) 8266);
        addItem(new ItemBuilder(potion).build(), false);
        addItem(new ItemBuilder(Material.MONSTER_EGG).setDurability(EntityType.BAT.getTypeId()).setAmount(5).build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
