package mc.zyntra.arena.controller.sub;

import lombok.var;
import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.enums.GameState;
import mc.zyntra.arena.structure.loot.LootChest;
import mc.zyntra.arena.structure.loot.LootFactory;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaGameController {

    private final Arena arena;

    private BukkitRunnable gameTask;

    public ArenaGameController(Arena arena) {
        this.arena = arena;
    }

    public void startGame() {
        arena.setState(GameState.INGAME);
        arena.setTime(0);

        int index = 0;
        for (Player player : arena.getPlayers()) {
            player.teleport(arena.getIslands().get(index).getSpawn());
            player.sendMessage("§aA partida começou! Você foi teleportado para sua ilha.");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 5.0f, 5.0f);
            index++;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                int time = arena.getTime() + 1;
                arena.setTime(time);

                if (arena.getPlayers().size() == 1) {
                    new ArenaEndController(arena).endGame();
                    cancel();
                }

                if (time == 30 || time == 60) {
                    refillChests();
                }
            }
        }.runTaskTimer(Skywars.getInstance(), 20L, 20L);
    }

    private void refillChests() {
        for (var island : arena.getIslands()) {
            for (var chestLoc : island.getChestLocations().keySet()) {
                LootChest.Type type = island.getChestLocations().get(chestLoc);
                island.addChestLocation(chestLoc);
            }
        }

        for (Location loc : arena.getFeastChestLocations()) {
            fillChest(loc, LootChest.Type.FEAST);
        }

        for (Location loc : arena.getMiniFeastChestLocations()) {
            fillChest(loc, LootChest.Type.MINIFEAST);
        }
    }

    public void stopGame() {
        if (gameTask != null) {
            gameTask.cancel();
        }
    }

    private void fillChest(Location location, LootChest.Type type) {
        if (location == null) return;

        var block = location.getBlock();
        if (!(block.getState() instanceof Chest)) return;

        Chest chest = (Chest) block.getState();

        ItemStack[] loot = LootFactory.generate(type).toArray(new ItemStack[0]);
        chest.getInventory().clear();
        chest.getInventory().setContents(loot);
    }
}
