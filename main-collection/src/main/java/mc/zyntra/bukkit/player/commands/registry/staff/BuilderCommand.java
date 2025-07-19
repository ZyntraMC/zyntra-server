package mc.zyntra.bukkit.player.commands.registry.staff;

import mc.zyntra.general.Constant;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.worldedit.WorldeditController;
import mc.zyntra.bukkit.api.worldedit.arena.ArenaResponse;
import mc.zyntra.bukkit.api.worldedit.arena.ArenaType;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandContext;
import com.google.common.base.Joiner;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class BuilderCommand implements CommandClass {

    @Getter
    private final WorldeditController controller;

    public BuilderCommand() {
        controller = BukkitMain.getInstance().getWorldeditController();
    }

    @Command(
            name = "wand",
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void wand(CommandContext context) {
        controller.giveWand(context.getPlayer());
        context.getPlayer().sendMessage("§aVocê recebeu o machado do worldedit.");
    }

    @Command(
            name = "createarena",
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void createarena(CommandContext context) {
        Player player = context.getPlayer();
        String[] args = context.getArguments();

        if (args.length < 4) {
            player.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " " + (Joiner.on(":")
                    .join(Arrays.stream(ArenaType.values())
                            .map(arenaType -> arenaType.name().toLowerCase()).collect(Collectors.toList())))
                    + "> <material:id> <radius> <height>"));
            return;
        }

        ArenaType arenaType;
        try {
            arenaType = ArenaType.valueOf(args[0].toUpperCase());
        } catch (Exception ex) {
            player.sendMessage("§cTipo de arena inválida.");
            return;
        }

        Material blockMaterial;
        byte blockId = 0;

        if (args[1].contains(":")) {
            blockMaterial = Material.getMaterial(args[1].split(":")[0].toUpperCase());

            if (blockMaterial == null) {
                try {
                    blockMaterial = Material.getMaterial(Integer.parseInt(args[1].split(":")[0]));
                } catch (NumberFormatException ignored) {}
            }

            try {
                blockId = Byte.parseByte(args[1].split(":")[1]);
            } catch (Exception e) {
                player.sendMessage("§cO bloco " + args[1] + " não existe!");
                return;
            }
        } else {
            blockMaterial = Material.getMaterial(args[1]);

            if (blockMaterial == null) {
                try {
                    blockMaterial = Material.getMaterial(Integer.parseInt(args[1]));
                } catch (NumberFormatException ignored) {}
            }
        }

        int radius;

        try {
            radius = Integer.valueOf(args[2]);
        } catch (NumberFormatException ex) {
            player.sendMessage("§cSintaxe de radius inválida.");
            return;
        }

        int height;

        try {
            height = Integer.valueOf(args[3]);
        } catch (NumberFormatException ex) {
            player.sendMessage("§cSintaxe de height inválida.");
            return;
        }

        ArenaResponse arenaResponse = arenaType.place(player.getLocation(), blockMaterial, blockId, radius, height,
                true, false);

        controller.addUndo(player, arenaResponse.getMap());
        player.sendMessage("§dVocê criou uma arena " + arenaType.name() + ", colocando " + arenaResponse.getBlocks() + " blocos!");
    }

    @Command(
            name = "set",
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void set(CommandContext context) {
        Player player = context.getPlayer();
        String[] args = context.getArguments();

        if (args.length == 0) {
            player.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <material:id>"));
            return;
        }

        Material blockMaterial;
        byte blockId = 0;

        if (args[0].contains(":")) {
            blockMaterial = Material.getMaterial(args[0].split(":")[0].toUpperCase());

            if (blockMaterial == null) {
                try {
                    blockMaterial = Material.getMaterial(Integer.parseInt(args[0].split(":")[0]));
                } catch (NumberFormatException e) {
                    player.sendMessage("§cNão foi possível encontrar esse bloco!");
                    return;
                }
            }

            try {
                blockId = Byte.parseByte(args[0].split(":")[1]);
            } catch (Exception e) {
                player.sendMessage("§cO bloco " + args[0] + " não existe!");
                return;
            }
        } else {
            blockMaterial = Material.getMaterial(args[0]);

            if (blockMaterial == null) {
                try {
                    blockMaterial = Material.getMaterial(Integer.parseInt(args[0]));
                } catch (NumberFormatException e) {
                    player.sendMessage("§cNão foi possível encontrar esse bloco!");
                    return;
                }
            }
        }

        if (blockMaterial == null) {
            player.sendMessage("§cNão foi possível encontrar o bloco " + args[0] + "!");
            return;
        }

        if (!controller.hasFirstPosition(player)) {
            player.sendMessage("§cA primeira posição não foi setada!");
            return;
        }

        if (!controller.hasSecondPosition(player)) {
            player.sendMessage("§cA segunda posição não foi setada!");
            return;
        }

        Location first = controller.getFirstPosition(player);
        Location second = controller.getSecondPosition(player);

        Map<Location, BlockState> map = new HashMap<>();
        int amount = 0;

        for (int x = (Math.min(first.getBlockX(), second.getBlockX())); x <= (Math.max(first.getBlockX(), second.getBlockX())); x++) {
            for (int z = (Math.min(first.getBlockZ(), second.getBlockZ())); z <= (Math.max(first.getBlockZ(), second.getBlockZ())); z++) {
                for (int y = (Math.min(first.getBlockY(), second.getBlockY())); y <= (Math.max(first.getBlockY(), second.getBlockY())); y++) {
                    Location location = new Location(first.getWorld(), x, y, z);
                    map.put(location.clone(), location.getBlock().getState());

                    if (location.getBlock().getType() != blockMaterial || location.getBlock().getData() != blockId) {
                        location.getBlock().setType(blockMaterial);
                        location.getBlock().setData(blockId);
                        amount++;
                    }
                }
            }
        }

        controller.addUndo(player, map);
        player.sendMessage("§dVocê colocou " + amount + " blocos!");
    }

    @Command(
            name = "undo",
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void undo(CommandContext context) {
        Player player = context.getPlayer();

        if (!controller.hasUndoList(player)) {
            player.sendMessage("§cVocê não tem nada para desfazer.");
            return;
        }

        Map<Location, BlockState> map = controller.getUndoList(player).get(controller.getUndoList(player).size() - 1);

        int amount = 0;

        for (Map.Entry<Location, BlockState> entry : map.entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue().getType());
            entry.getKey().getBlock().setData(entry.getValue().getData().getData());
            amount++;
        }

        controller.removeUndo(player, map);
        player.sendMessage("§dVocê colocou " + amount + " blocos!");
    }

    @Command(
            name = "worldtp",
            aliases = {"wtp"},
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void worldtp(CommandContext context) {
        Player player = context.getPlayer();
        String[] args = context.getArguments();

        if (args.length == 0) {
            player.sendMessage("§cUse /worldtp <mundo>, /worldtp list, /worldtp unloadeds ou /worldtp load <mundo>.");
            return;
        }

        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(" §eMundos carregados:");

            for (World world : Bukkit.getWorlds()) {
                TextComponent component = new TextComponent("§8• §f" + world.getName());
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new TextComponent[] { new TextComponent("§aClique para teleportar para §f" + world.getName()) }));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldtp " + world.getName()));
                player.spigot().sendMessage(component);
            }
            return;
        }

        if (args[0].equalsIgnoreCase("unloadeds")) {
            File worldContainer = Bukkit.getWorldContainer();
            File[] folders = worldContainer.listFiles(File::isDirectory);

            if (folders == null || folders.length == 0) {
                player.sendMessage("§cNenhum mundo encontrado.");
                return;
            }

            List<String> loadedNames = new ArrayList<>();
            for (World world : Bukkit.getWorlds()) {
                loadedNames.add(world.getName());
            }

            player.sendMessage("§eMundos não carregados:");
            for (File folder : folders) {
                String folderName = folder.getName();
                if (folderName.equalsIgnoreCase("plugins") || folderName.equalsIgnoreCase("logs"))
                    continue;

                if (loadedNames.contains(folderName))
                    continue;

                if (!new File(folder, "level.dat").exists())
                    continue;

                TextComponent component = new TextComponent("§8• §f" + folderName);
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new TextComponent[] { new TextComponent("§aClique para carregar §f" + folderName) }));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/worldtp load " + folderName));
                player.spigot().sendMessage(component);
            }
            return;
        }

        if (args[0].equalsIgnoreCase("load")) {
            if (args.length < 2) {
                player.sendMessage("§cUse /worldtp load <mundo>");
                return;
            }

            String worldName = args[1];

            if (Bukkit.getWorld(worldName) != null) {
                player.sendMessage("§cO mundo §f" + worldName + "§c já está carregado.");
                return;
            }

            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            if (!worldFolder.exists() || !new File(worldFolder, "level.dat").exists()) {
                player.sendMessage("§cO mundo §f" + worldName + "§c não foi encontrado.");
                return;
            }

            World world = Bukkit.createWorld(new WorldCreator(worldName));
            if (world == null) {
                player.sendMessage("§cFalha ao carregar o mundo.");
                return;
            }

            player.sendMessage("§aMundo §f" + worldName + "§a carregado com sucesso.");
            return;
        }

        String targetWorld = args[0];
        World world = Bukkit.getWorld(targetWorld);

        if (world == null) {
            player.sendMessage("§cO mundo §f" + targetWorld + "§c não está carregado.");
            return;
        }

        player.teleport(world.getSpawnLocation());
        player.sendMessage("§aVocê foi teleportado para §f" + world.getName() + "§a.");
    }
}
