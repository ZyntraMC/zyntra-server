package mc.zyntra.match.commands;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.controller.ArenaController;
import mc.zyntra.arena.enums.ArenaType;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetupCommand implements CommandClass {

    @Command(
            name = "setup",
            group = Group.ADMIN,
            inGameOnly = true
    )
    public void setup(CommandContext context) {
        Player player = context.getPlayer();

        if (context.getArguments().length != 1) {
            player.sendMessage("§cUso correto: /setup <arena>");
            return;
        }

        String arenaName = context.getArguments()[0].toLowerCase();

        ArenaController arenaController = Skywars.getInstance().getArenaController();
        Arena arena = arenaController.getArena(arenaName);

        if (arena == null) {
            arena = new Arena(arenaName);
            arena.setMinPlayers(2);
            arena.setMaxPlayers(16);
            arena.setType(ArenaType.NORMAL);

            arenaController.addArena(arena);

            player.sendMessage("§aArena §b" + arenaName + " §acriada com valores padrão!");
        } else {
            player.sendMessage("§aModo setup ativado para arena §b" + arenaName);
        }

        arenaController.setSetupArena(player, arena);

        giveSetupItems(player);
    }

    private void giveSetupItems(Player player) {
        player.getInventory().clear();

        ItemStack lobbyItem = new ItemBuilder(Material.BLAZE_ROD)
                .setName("§aDefinir LobbySpawn")
                .build();
        ItemStack spawnItem = new ItemBuilder(Material.FEATHER)
                .setName("§aDefinir Spawn")
                .build();
        ItemStack chestItem = new ItemBuilder(Material.CHEST)
                .setName("§aDefinir Baú")
                .build();

        player.getInventory().setItem(0, lobbyItem);
        player.getInventory().setItem(1, spawnItem);
        player.getInventory().setItem(2, chestItem);
    }
}
