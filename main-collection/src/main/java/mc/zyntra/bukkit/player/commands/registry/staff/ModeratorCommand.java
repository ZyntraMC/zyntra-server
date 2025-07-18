package mc.zyntra.bukkit.player.commands.registry.staff;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.account.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ModeratorCommand implements CommandClass {

    @Command(
            name = "inventorysee",
            aliases = {"invsee", "inv"},
            group = Group.MODERATOR,
            inGameOnly = true
    )
    public void inventorysee(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Constant.PLAYER_NOT_FOUND);
            return;
        }

        player.openInventory(target.getInventory());
        player.sendMessage("§aVocê abriu o inventário de " + target.getName() + ".");

        notify(zyntraPlayer.getName() + " abriu o inventário de " + target.getName());
    }

    @Command(
            name = "teleport",
            aliases = "tp",
            group = Group.MODERATOR,
            inGameOnly = true
    )
    public void teleport(CommandContext context) {
        Player player = context.getPlayer();
        String[] args = context.getArguments();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            player.teleport(target);
            player.sendMessage("§aVocê teleportou-se até " + target.getName() + ".");

            notify( player.getName() + " teleportou-se até " + target.getName());
            return;
        } else if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            Player playerToTeleport = Bukkit.getPlayer(args[1]);
            if (playerToTeleport == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            target.teleport(playerToTeleport);
            player.sendMessage("§aVocê teleportou " + target.getName() + " até " + playerToTeleport.getName() + ".");

            notify(zyntraPlayer.getName() + " teleportou " + target.getName() + " até " + playerToTeleport.getName());

            return;
        } else if (args.length == 3) {
            if (isInteger(args[0]) && isInteger(args[1]) && isInteger(args[2])) {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                Location location = new Location(player.getWorld(), x, y, z);

                player.teleport(location);
                player.sendMessage("§aVocê teleportou-se até " + x + ", " + y + ", " + z + ".");

                notify(zyntraPlayer.getName() + " se teleportou até " + x + ", " + y + ", " + z);
            } else {
                player.sendMessage("§cFormato de localização inválida.");
            }
            return;
        }

        player.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " [player] <target> e/ou <x> <y> <z>"));
    }

    @Command(
            name = "tpall",
            aliases = "teleportall",
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void teleportall(CommandContext context) {
        Player player = context.getPlayer();

        player.sendMessage("§aTeleportando...");

        new BukkitRunnable() {
            final Location location = player.getLocation();
            final List<Player> players = player.getWorld().getPlayers();
            int count = 0;

            public void run() {
                if (count >= players.size()) {
                    cancel();

                    if (player.isOnline())
                        player.sendMessage("§aVocê teleportou " + count + " jogador(es) até você.");

                    return;
                }

                for (int i = 0; i < 2; i++) {
                    try {
                        Player t = players.get(count + i);
                        if (t != null && t != player) {
                            t.teleport(location);
                        }
                    } catch (IndexOutOfBoundsException | NullPointerException ignored) {
                    }
                }
                count+=2;
            }
        }.runTaskTimer(BukkitMain.getInstance(), 2L, 2L);
    }

    @Command(
            name = "gamemode",
            aliases = { "gm" },
            group = Group.MANAGER,
            inGameOnly = true
    )
    public void gamemode(CommandContext context) {
        Player player = context.getPlayer();
        String[] args = context.getArguments();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage("§a/gamemode <modo> [Jogador]");
            player.sendMessage("§7Modos disponíveis: §f0 [Survival], 1 [Creative], 2 [Adventure], 3 [Spectator]");
            return;
        }

        String sub = args[0].toLowerCase();

        GameMode gamemode = getGameMode(sub);
        if (gamemode == null) {
            player.sendMessage("§c§lERROR ➜ §r§cGamemode inválido. Use 0, 1, 2, 3 ou survival, creative, adventure, spectator.");
            return;
        }

        if (args.length == 1) {
            if (player.getGameMode() == gamemode) {
                player.sendMessage("§c§lERROR ➜ §r§cVocê já está neste gamemode.");
                return;
            }

            player.setGameMode(gamemode);
            player.sendMessage("§aVocê alterou o seu gamemode para §f" + gamemode.name().toLowerCase() + "§a.");
            notify("§7§o[" + zyntraPlayer.getName() + " alterou o seu gamemode para " + gamemode.name() + "]");
            return;
        }

        if (args.length >= 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage("§c§lERROR ➜ §r§cJogador não encontrado.");
                return;
            }

            if (target.getGameMode() == gamemode) {
                player.sendMessage("§c§lERROR ➜ §r§cO jogador já está neste gamemode.");
                return;
            }

            target.setGameMode(gamemode);
            player.sendMessage("§aVocê alterou o gamemode de §f" + target.getName() + " §apara §f" + gamemode.name().toLowerCase() + "§a.");
            target.sendMessage("§aSeu gamemode foi alterado para §f" + gamemode.name().toLowerCase() + " §apor §f" + player.getName() + "§a.");

            notify("§7§o[" + zyntraPlayer.getName() + " alterou o gamemode de " + target.getName() + " para " + gamemode.name() + "]");
        }
    }

    private GameMode getGameMode(String input) {
        switch (input.toLowerCase()) {
            case "0":
            case "survival":
                return GameMode.SURVIVAL;
            case "1":
            case "creative":
                return GameMode.CREATIVE;
            case "2":
            case "adventure":
                return GameMode.ADVENTURE;
            case "3":
            case "spectator":
                return GameMode.SPECTATOR;
            default:
                return null;
        }
    }

    public boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}