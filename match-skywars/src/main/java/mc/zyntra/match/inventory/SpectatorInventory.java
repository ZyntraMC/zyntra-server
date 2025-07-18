package mc.zyntra.match.inventory;

import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.Leading;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


public class SpectatorInventory extends MenuInventory {

    public SpectatorInventory() {
        super("Selecione um jogador para spectar", 4);

        int slot = 8;

        for (Player players : Leading.getInstance().getRoom().getPlayers()) {
            slot++;
            if (slot % 9 == 0 || slot % 9 == 8)
                slot++;
            if (slot % 9 == 0 || slot % 9 == 8)
                slot++;

            ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta im = (SkullMeta) is.getItemMeta();

            im.setDisplayName("§a" + players.getName());
            im.setOwner(players.getName());
            is.setItemMeta(im);

            setItem(slot, new ItemBuilder(is).build(), (p, inv, type, stack, itemSlot) -> {
                p.closeInventory();
                p.teleport(Bukkit.getPlayer(((SkullMeta) is.getItemMeta()).getOwner()));
            });

            slot++;
        }
    }
}