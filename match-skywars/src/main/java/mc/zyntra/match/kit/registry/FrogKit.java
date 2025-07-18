package mc.zyntra.match.kit.registry;

import mc.zyntra.Leading;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.match.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;


public class FrogKit extends Kit {

    public FrogKit() {
        super("Frog", 15000);

        setIcon(new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).setPlayerHeadBase64("ewogICJ0aW1lc3RhbXAiIDogMTYyMTU2OTgxODg3NywKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMTc4MTg5ZWY4ZmE3YTViNzI0ZmI5MWRmOWE0NDc4NGZkODU2NDhlZDNlM2FjZjZkMGRlZDdiOGNhYTMwZjA3IgogICAgfQogIH0KfQ==").setName("§aFrog").setLore(getDescription()).build());
        setDescription(Arrays.asList("§7Vire um sapo e saia pulando", "§7rapidamente para seus inimigos!", "", "§eItens:", " §7Poção (Arremesável) 1x", "  §8Super pulo IV (0:40)", "  §8Velocidade II (0:40)", "", "§7Preço: §6" + String.format("%,d", getPrice()), "", "%info%"));

        ItemStack pocaoSuperPulo = new ItemStack(Material.POTION, 1, (short) 16428); // Tipo de poção de Super Pulo IV
        PotionMeta metaSuperPulo = (PotionMeta) pocaoSuperPulo.getItemMeta();
        metaSuperPulo.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 60 * 5, 3), true); // 5 minutos de Super Pulo IV
        pocaoSuperPulo.setItemMeta(metaSuperPulo);

        ItemStack pocaoVelocidade = new ItemStack(Material.POTION, 1, (short) 8226); // Tipo de poção de Velocidade II
        PotionMeta metaVelocidade = (PotionMeta) pocaoVelocidade.getItemMeta();
        metaVelocidade.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 3, 1), true); // 3 minutos de Velocidade II
        pocaoVelocidade.setItemMeta(metaVelocidade);

        ItemStack peitoral = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack calca = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack botas = new ItemStack(Material.LEATHER_BOOTS);

        LeatherArmorMeta metaPeitoral = (LeatherArmorMeta) peitoral.getItemMeta();
        metaPeitoral.setColor(Color.GREEN);
        peitoral.setItemMeta(metaPeitoral);

        LeatherArmorMeta metaCalca = (LeatherArmorMeta) calca.getItemMeta();
        metaCalca.setColor(Color.GREEN);
        calca.setItemMeta(metaCalca);

        LeatherArmorMeta metaBotas = (LeatherArmorMeta) botas.getItemMeta();
        metaBotas.setColor(Color.GREEN);
        botas.setItemMeta(metaBotas);
        addItem(new ItemBuilder(pocaoVelocidade).build(), false);
        addItem(new ItemBuilder(pocaoSuperPulo).build(), false);
        addItem(new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).setDurability(3).setPlayerHeadBase64("ewogICJ0aW1lc3RhbXAiIDogMTYyMTU2OTgxODg3NywKICAicHJvZmlsZUlkIiA6ICJiNjM2OWQ0MzMwNTU0NGIzOWE5OTBhODYyNWY5MmEwNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJCb2JpbmhvXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMTc4MTg5ZWY4ZmE3YTViNzI0ZmI5MWRmOWE0NDc4NGZkODU2NDhlZDNlM2FjZjZkMGRlZDdiOGNhYTMwZjA3IgogICAgfQogIH0KfQ==").build(), false);
        addItem(new ItemBuilder(peitoral).build(), false);
        addItem(new ItemBuilder(calca).build(), false);
        addItem(new ItemBuilder(botas).build(), false);
        Bukkit.getPluginManager().registerEvents(this, Leading.getInstance());
    }
}
