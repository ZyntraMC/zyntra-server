package mc.zyntra.match.invetory;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.structure.Island;
import mc.zyntra.arena.structure.loot.LootChest;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ChestTypeInventory extends MenuInventory {

    private final Player player;
    private final Arena arena;
    private final Location chestLocation;

    public ChestTypeInventory(Player player, Arena arena, Location chestLocation) {
        super("Escolha o tipo do baú", 3);
        this.player = player;
        this.arena = arena;
        this.chestLocation = chestLocation;

        setItem(11, new ItemBuilder(Material.CHEST)
                .setName("§aBaú Normal")
                .setLore("§7Contém itens básicos")
                .build(), (p, inv, type, stack, slot) -> {
                    addChest(LootChest.NORMAL.getType());
                });

        setItem(13, new ItemBuilder(Material.GOLD_CHESTPLATE)
                .setName("§6Baú MiniFeast")
                .setLore("§7Contém itens especiais")
                .build(), (p, inv, type, stack, slot) -> {
                    addChest(LootChest.MINIFEAST.getType());
                });

        setItem(15, new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .setName("§bBaú Feast")
                .setLore("§7Contém itens muito poderosos")
                .build(), (p, inv, type, stack, slot) -> {
                    addChest(LootChest.FEAST.getType());
                });
    }

    private void addChest(LootChest.Type type) {
        if (type == LootChest.Type.FEAST || type == LootChest.Type.MINIFEAST) {
            if (type == LootChest.Type.FEAST) {
                arena.addFeastChest(chestLocation);
            } else {
                arena.addMiniFeastChest(chestLocation);
            }
            player.sendMessage("§aBaú especial do tipo §f" + type.name() + " §aadicionado!");
        } else {
            Island targetIsland = arena.getIslands().stream()
                    .filter(island -> island.getSpawn().getWorld().equals(chestLocation.getWorld()) &&
                            island.getSpawn().distance(chestLocation) < 10)
                    .findFirst()
                    .orElse(null);

            if (targetIsland == null) {
                player.sendMessage("§cEsse baú não está próximo de nenhuma ilha.");
                return;
            }

            targetIsland.addChestLocation(chestLocation);
            player.sendMessage("§aBaú do tipo §fNORMAL §aadicionado à ilha.");
        }

        Skywars.getInstance().getArenaController().saveArena(arena);
        player.closeInventory();
    }

    // Método helper para formatar localização (opcional)
    private String formatLocation(Location loc) {
        return "X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ();
    }
}
