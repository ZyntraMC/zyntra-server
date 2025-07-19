package mc.zyntra.arena.structure.loot;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootFactory {

    private static final Random RANDOM = new Random();

    public static List<ItemStack> generate(LootChest.Type type) {
        List<ItemStack> items = new ArrayList<>();

        switch (type) {
            case NORMAL:
                add(items, Material.STONE_SWORD, 80);
                add(items, Material.COOKED_BEEF, 60, 4, 8);
                add(items, Material.SNOW_BALL, 30, 4, 12);
                add(items, Material.EGG, 25, 4, 12);
                add(items, Material.COBBLESTONE, 40, 16, 32);
                add(items, Material.EXP_BOTTLE, 20, 4, 8);
                add(items, Material.WOOD_PICKAXE, 20);
                break;

            case MINIFEAST:
                add(items, enchant(new ItemStack(Material.IRON_SWORD), Enchantment.DAMAGE_ALL, 1), 80);
                add(items, Material.COOKED_BEEF, 80, 6, 10);
                add(items, Material.GOLDEN_APPLE, 50, 1, 2);
                add(items, Material.SNOW_BALL, 50, 6, 16);
                add(items, Material.EGG, 50, 6, 16);
                add(items, Material.EXP_BOTTLE, 60, 8, 12);
                add(items, Material.ENDER_PEARL, 20, 1, 2);
                break;

            case FEAST:
                add(items, enchant(new ItemStack(Material.DIAMOND_SWORD), Enchantment.DAMAGE_ALL, 2), 100);
                add(items, enchant(new ItemStack(Material.BOW), Enchantment.ARROW_DAMAGE, 2), 70);
                add(items, Material.ARROW, 80, 16, 32);
                add(items, Material.ENDER_PEARL, 80, 2, 4);
                add(items, Material.GOLDEN_APPLE, 100, 2, 4);
                add(items, Material.EXP_BOTTLE, 100, 16, 32);
                add(items, enchant(new ItemStack(Material.DIAMOND_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 2), 60);
                add(items, Material.SNOW_BALL, 60, 8, 16);
                add(items, Material.EGG, 60, 8, 16);
                break;
        }

        return items;
    }

    private static void add(List<ItemStack> list, Material mat, int chance) {
        if (RANDOM.nextInt(100) < chance) {
            list.add(new ItemStack(mat));
        }
    }

    private static void add(List<ItemStack> list, Material mat, int chance, int min, int max) {
        if (RANDOM.nextInt(100) < chance) {
            list.add(new ItemStack(mat, getAmount(min, max)));
        }
    }

    private static void add(List<ItemStack> list, ItemStack item, int chance) {
        if (RANDOM.nextInt(100) < chance) {
            list.add(item);
        }
    }

    private static int getAmount(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    private static ItemStack enchant(ItemStack item, Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return item;
    }
}
