package mc.zyntra.match.listener;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.structure.Island;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.match.invetory.ChestTypeInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SetupListener implements Listener {

    private static final ItemStack LOBBY_ITEM = new ItemBuilder(Material.BLAZE_ROD).setName("§aDefinir LobbySpawn").build();
    private static final ItemStack SPAWN_ITEM = new ItemBuilder(Material.FEATHER).setName("§aDefinir Spawn").build();
    private static final ItemStack CHEST_ITEM = new ItemBuilder(Material.CHEST).setName("§aDefinir Baú").build();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Arena arena = Skywars.getInstance().getArenaController().getSetupArena(player);
        if (arena == null) return;

        ItemStack item = player.getInventory().getItemInHand();
        if (item == null) return;

        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;

        String name = item.getItemMeta().getDisplayName();

        if (name.equals(LOBBY_ITEM.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
            arena.setLobbyLocation(player.getLocation());
            player.sendMessage("§aLobbySpawn definido!");
            Skywars.getInstance().getArenaController().saveArena(arena);
            return;
        }

        if (name.equals(SPAWN_ITEM.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
            Location loc = player.getLocation();
            Island island = new Island(loc, loc.clone().add(0, 5, 0)); // Cage acima do spawn
            arena.addIsland(island);
            player.sendMessage("§aSpawn da ilha definido!");
            Skywars.getInstance().getArenaController().saveArena(arena);
            return;
        }

        if (name.equals(CHEST_ITEM.getItemMeta().getDisplayName())) {
            event.setCancelled(true);

            Block block = event.getClickedBlock();
            if (block == null || block.getType() != Material.CHEST) {
                player.sendMessage("§cClique em um baú para definir a localização.");
                return;
            }

            Chest chest = (Chest) block.getState();

            // Abre menu para escolher tipo do baú
            new ChestTypeInventory(player, arena, chest.getLocation()).open(player);
        }
    }
}
