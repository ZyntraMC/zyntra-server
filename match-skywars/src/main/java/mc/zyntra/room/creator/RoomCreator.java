package mc.zyntra.room.creator;

import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.util.Platform;
import mc.zyntra.room.Room;
import mc.zyntra.room.service.inventory.SelectSubTypeInventory;
import mc.zyntra.room.service.inventory.SelectTypeInventory;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;


@Getter @Setter
public class RoomCreator {
    private Player player;
    private Room room;
    private int miniFeastIsland, feastIsland;

    public RoomCreator(Player player, Room room) {
        this.player = player;
        this.room = room;
        this.miniFeastIsland = 0;
        this.feastIsland = 0;
    }

    public void sendItems() {
        player.getInventory().setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).setName("§aSelecione o modo")
                .build((p, item, action, clicked) -> {
                    new SelectTypeInventory().open(p);
                    return false;
                }));

        player.getInventory().setItem(1, new ItemBuilder(Material.WOOD_AXE).setName("§aSelecione o tipo")
                .build((p, item, action, clicked) -> {
                    new SelectSubTypeInventory().open(p);
                    return false;
                }));

        player.getInventory().setItem(2, new ItemBuilder(Material.BED).setName("§6Definir local de espera")
                .build(
                        (p, item, action, clicked) -> {
                            room.setLobby(p.getLocation());
                            p.sendMessage("");
                            p.sendMessage("§b§l > §aVocê definiu o §llocal de espera §ada sala!");
                            p.sendMessage("");
                            return false;
                        }
                ));

        player.getInventory().setItem(7, new ItemBuilder(Material.BEACON).setName("§aAdicionar spawn")
                .build(
                        (p, item, action, clicked) -> {
                            room.getSpawns().add(player.getLocation());
                            p.sendMessage("");
                            p.sendMessage("§b§l > §aVocê adicionou um novo §lspawn §aa lista!");
                            p.sendMessage("§e§l > §c§o Agora existem §o§f§l" + room.getSpawns().size() + " §c§ospawns.");
                            p.sendMessage("");
                            return false;
                        }
                ));

        player.getInventory().setItem(8, new ItemBuilder(Material.INK_SACK).setDurability(10).setName("§aSalvar configuração")
                .build(
                        (p, item, action, clicked) -> {
                            if (room.getLobby() == null) {
                                p.sendMessage("");
                                p.sendMessage("§e§l > §cVocê deve definir o local de espera primeiro!");
                                p.sendMessage("");
                            }

                            if (room.getSpawns().isEmpty()) {
                                p.sendMessage("");
                                p.sendMessage("§e§l > §cVocê deve definir os spawns primeiro!");
                                p.sendMessage("");
                            }

                            if (room.getType() == null) {
                                p.sendMessage("");
                                p.sendMessage("§e§l > §cVocê deve definir o modo da sala primeiro!");
                                p.sendMessage("");
                            }

                            if (room.getSubType() == null) {
                                p.sendMessage("");
                                p.sendMessage("§e§l > §cVocê deve definir o tipo da sala primeiro!");
                                p.sendMessage("");
                            }
                            Platform.getMatch().create(room);
                            Platform.getRoomCreatorController().unload(player.getUniqueId());
                            player.getInventory().clear();
                            return false;
                        }
                ));
    }
}
