package mc.zyntra.bukkit.api.inventory.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material type, int amount, byte durability) {
        this(new ItemStack(type, amount, durability));
    }

    public ItemBuilder(Material type, int amount) {
        this(new ItemStack(type, amount));
    }

    public ItemBuilder(Material type, String name) {
        this(new ItemStack(type));
        setName(name);
    }

    public ItemBuilder(Material type) {
        this(new ItemStack(type));
    }

    public static ItemBuilder fromItemStack(ItemStack item) {
        return new ItemBuilder(item);
    }

    protected ItemBuilder changeItem(Consumer<ItemStack> consumer) {
        consumer.accept(this.item);
        return this;
    }

    protected ItemBuilder changeMeta(Consumer<ItemMeta> consumer) {
        final ItemMeta meta = this.item.getItemMeta();
        consumer.accept(meta);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setType(Material type) {
        return changeItem(item -> item.setType(type));
    }

    public ItemBuilder setName(String name) {
        return changeMeta(meta -> meta.setDisplayName(name));
    }

    public ItemBuilder setAmount(int amount) {
        return changeItem(item -> item.setAmount(amount));
    }

    public ItemBuilder setDurability(int durability) {
        return changeItem(item -> item.setDurability((byte) durability));
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        return changeItem(item -> item.addUnsafeEnchantment(ench, level));
    }

    public ItemBuilder removeEnchant(Enchantment ench, int level) {
        return changeItem(item -> item.removeEnchantment(ench));
    }

    public ItemBuilder setLore(List<String> lore) {
        return changeMeta(meta -> meta.setLore(lore));
    }

    public ItemBuilder setLore(String... loreArray) {
        return changeMeta(meta -> meta.setLore(Arrays.asList(loreArray)));
    }

    public ItemBuilder setLore(String loreText) {
        return changeMeta(meta -> {
            List<String> lore = new java.util.ArrayList<>();
            String text = ChatColor.translateAlternateColorCodes('&', loreText);
            String[] split = text.split(" ");
            String color = "";
            text = "";
            for (int i = 0; i < split.length; i++) {
                if (ChatColor.stripColor(text).length() >= 30 || ChatColor.stripColor(text).endsWith(".")
                        || ChatColor.stripColor(text).endsWith("!")) {
                    lore.add(text);
                    if (text.endsWith(".") || text.endsWith("!"))
                        lore.add("");
                    text = color;
                }
                String toAdd = split[i];
                if (toAdd.contains("§"))
                    color = ChatColor.getLastColors(toAdd.toLowerCase());
                if (toAdd.contains("\n")) {
                    toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
                    split[i] = split[i].substring(toAdd.length() + 1);
                    lore.add(text + (text.length() == 0 ? "" : " ") + toAdd);
                    text = color;
                    i--;
                } else {
                    text += (ChatColor.stripColor(text).length() == 0 ? "" : " ") + toAdd;
                }
            }
            lore.add(text);
            meta.setLore(lore);
        });
    }

    public ItemBuilder addItemFlag(ItemFlag... flags) {
        return changeMeta(meta -> meta.addItemFlags(flags));
    }

    public ItemBuilder removeItemFlag(ItemFlag... flags) {
        return changeMeta(meta -> meta.removeItemFlags(flags));
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        return changeMeta(meta -> meta.spigot().setUnbreakable(unbreakable));
    }

    public ItemBuilder setPlayerHead(String name) {
        return changeMeta(meta -> {
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                skullMeta.setOwner(name);
            }
        });
    }

    public ItemBuilder setPlayerHeadBase64(String value) {
        return changeMeta(meta -> {
            SkullMeta itemMeta = (SkullMeta) meta;

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", value));

            Field field;
            try {
                field = itemMeta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(itemMeta, profile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public ItemStack build(InteractHandler handler) {
        return InteractHandlerAPI.setTag(this.item, InteractHandlerAPI.register(handler));
    }

    public ItemStack build() {
        return this.item;
    }
}
