package mc.zyntra.room.service.inventory;

import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.util.Platform;
import mc.zyntra.room.creator.RoomCreator;
import mc.zyntra.room.service.type.RoomSubType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;


public class SelectSubTypeInventory extends MenuInventory {

    public SelectSubTypeInventory() {
        super("Selecione a quantia da sala", 1);
        setItem(2, new ItemBuilder(Material.WOOD_SWORD).setAmount(1)
                .setName("§aSolo")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build(), (p, inv, type, stack, itemSlot) -> {
            RoomCreator builder = Platform.getRoomCreatorController().getRoomCreator(p.getUniqueId());
            p.closeInventory();
            builder.getRoom().setSubType(RoomSubType.SOLO);
            builder.getRoom().setMinPlayers(2);
            builder.getRoom().setMaxPlayers(12);
            p.sendMessage("");
            p.sendMessage("§b§l > §aVocê definiu que a sala seja §lSolo§a!");
            p.sendMessage("");
        });

        setItem(3, new ItemBuilder(Material.IRON_SWORD).setAmount(2)
                .setName("§aDupla")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build(), (p, inv, type, stack, itemSlot) -> {
            RoomCreator builder = Platform.getRoomCreatorController().getRoomCreator(p.getUniqueId());
            p.closeInventory();
            builder.getRoom().setSubType(RoomSubType.DUPLA);
            builder.getRoom().setMinPlayers(6);
            builder.getRoom().setMaxPlayers(16);
            p.sendMessage("");
            p.sendMessage("§b§l > §aVocê definiu que a sala seja §lDupla§a!");
            p.sendMessage("");
        });
    }
}