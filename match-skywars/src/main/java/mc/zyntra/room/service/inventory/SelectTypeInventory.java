package mc.zyntra.room.service.inventory;

import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.util.Platform;
import mc.zyntra.room.creator.RoomCreator;
import mc.zyntra.room.service.type.RoomType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;


public class SelectTypeInventory extends MenuInventory {

    public SelectTypeInventory() {
        super("Selecione o modo da sala", 1);
        setItem(2, new ItemBuilder(Material.WOOD_SWORD)
                .setName("§aSkywars Normal")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build(), (p, inv, type, stack, itemSlot) -> {
            RoomCreator builder = Platform.getRoomCreatorController().getRoomCreator(p.getUniqueId());
            p.closeInventory();
            builder.getRoom().setType(RoomType.NORMAL);
            p.sendMessage("");
            p.sendMessage("§b§l > §aVocê definiu o §lmodo de jogo §ada sala!");
            p.sendMessage("§e§l > §c§oAgora a sala é do modo §e§l§oNormal§c§o.");
            p.sendMessage("");
        });

        setItem(3, new ItemBuilder(Material.STONE_SWORD)
                .setName("§aSkywars Duel")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build(), (p, inv, type, stack, itemSlot) -> {
            RoomCreator builder = Platform.getRoomCreatorController().getRoomCreator(p.getUniqueId());
            p.closeInventory();
            builder.getRoom().setType(RoomType.DUEL);
            p.sendMessage("");
            p.sendMessage("§b§l > §aVocê definiu o §lmodo de jogo §ada sala!");
            p.sendMessage("§e§l > §c§oAgora a sala é do modo §e§l§oDuels SW§c§o.");
            p.sendMessage("");
        });

        setItem(4, new ItemBuilder(Material.DIAMOND_SWORD)
                .setName("§aSkywars Insano")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .build(), (p, inv, type, stack, itemSlot) -> {
            RoomCreator builder = Platform.getRoomCreatorController().getRoomCreator(p.getUniqueId());
            p.closeInventory();
            builder.getRoom().setType(RoomType.INSANO);
            p.sendMessage("");
            p.sendMessage("§b§l > §aVocê definiu o §lmodo de jogo §ada sala!");
            p.sendMessage("§e§l > §c§oAgora a sala é do modo §e§l§oInsano§c§o.");
            p.sendMessage("");
        });
    }
}