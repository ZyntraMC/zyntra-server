package mc.zyntra.match.kit;

import mc.zyntra.bukkit.api.cooldown.CooldownAPI;
import mc.zyntra.bukkit.api.cooldown.types.Cooldown;
import mc.zyntra.match.generator.player.MatchPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public abstract class Kit implements Listener {
    private String name;
    private List<String> description;
    private ItemStack icon;
    private Integer cooldown;
    private Integer price;
    private List<ItemStack> items = new ArrayList<>();
    private Type abilityType;

    public Kit(String name, Integer price) {
        setName(name);
        setPrice(price);
    }

    public void setDescription(List<String> description) {
        this.description = description;

        ItemMeta meta = this.icon.getItemMeta();
        List<String> lore = new ArrayList<>();

        for (String D : description) {
            lore.add("§7" + D);
        }

        meta.setDisplayName("§a" + this.name);
        meta.setLore(lore);

        icon.setItemMeta(meta);
    }

    protected boolean hasAbility(MatchPlayer player, String name) {
        boolean using = false;
        if (player.getKit().equalsIgnoreCase(name)) {
            using = true;
        }
        return using;
    }

    protected boolean hasCooldown(Player player, final String cooldownName) {
        return CooldownAPI.getInstance().hasCooldown(player, cooldownName);
    }

    protected boolean hasCooldown(Player player) {
        return CooldownAPI.getInstance().hasCooldown(player, getName());
    }

    protected void sendMessageCooldown(Player player) {
        player.sendMessage("§cAguarde para utilizar esta habilidade novamente!");
    }

    protected void addCooldown(Player player, long time) {
        if (CooldownAPI.getInstance().hasCooldown(player, getName())) {
            CooldownAPI.getInstance().removeCooldown(player, getName());
        }

        CooldownAPI.getInstance().addCooldown(player, new Cooldown(getName(), time));
    }

    protected void addCooldown(Player player, String cooldownName, long time) {
        if (CooldownAPI.getInstance().hasCooldown(player, cooldownName)) {
            CooldownAPI.getInstance().removeCooldown(player, cooldownName);
        }

        CooldownAPI.getInstance().addCooldown(player, new Cooldown(cooldownName, time));
    }

    public void addItem(ItemStack item, boolean undroppable) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;

        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }

        if (tag == null) {
            tag = nmsStack.getTag();
        }

        tag.setString("Kit", this.name);
        tag.setBoolean("Undroppable", undroppable);

        items.add(CraftItemStack.asBukkitCopy(nmsStack));
    }

    public void applyItems(Player player) {
        PlayerInventory inventory = player.getInventory();

        for (ItemStack itemStack : items) {
            String type = itemStack.getType().name();

            if (type.contains("_")) {
                String[] split = type.split("_");

                switch (split[1]) {
                    case "HELMET":
                    case "ITEM": {
                        inventory.setHelmet(itemStack.clone());
                        break;
                    }
                    case "CHESTPLATE": {
                        inventory.setChestplate(itemStack.clone());
                        break;
                    }
                    case "LEGGINGS": {
                        inventory.setLeggings(itemStack.clone());
                        break;
                    }
                    case "BOOTS": {
                        inventory.setBoots(itemStack.clone());
                        break;
                    }
                    default: {
                        inventory.addItem(itemStack.clone());
                        break;
                    }
                }
            } else {
                inventory.addItem(itemStack.clone());
            }
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        INSANE,
        NORMAL;
    }
}