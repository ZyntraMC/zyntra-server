package mc.zyntra.match.command;

import mc.zyntra.bukkit.api.scoreboard.Scoreboard;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.match.perks.Perks;
import mc.zyntra.match.perks.loader.PerkLoader;
import mc.zyntra.stage.MatchStage;
import mc.zyntra.util.Platform;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.kit.Kit;
import mc.zyntra.match.kit.loader.KitLoader;
import mc.zyntra.room.Room;
import mc.zyntra.room.creator.RoomCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class PlayerCommand implements CommandClass {

    @Command(
            name = "room",
            group = Group.ADMIN,
            inGameOnly = true
    )
    public void room(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage("§c/room create <name> <map>");
            return;
        }

        if (args.length > 0) {
            String s = args[0];
            if (s.equalsIgnoreCase("create")) {
                if (args.length > 2) {
                    RoomCreator roomBuilder = new RoomCreator(player, new Room(args[1], args[2], null));
                    Platform.getRoomCreatorController().load(roomBuilder);
                    roomBuilder.sendItems();
                    player.sendMessage("");
                    player.sendMessage("§eCriação da sala '§f" + args[1] + "§e' iniciada.");
                    player.sendMessage("");
                    return;
                }
            }

            if (s.equalsIgnoreCase("start")) {
                    matchPlayer.getRoom().setMinPlayers(Bukkit.getOnlinePlayers().size());
                    matchPlayer.getRoom().setTime(5);
                    matchPlayer.getRoom().setStage(MatchStage.INICIANDO);
                    player.sendMessage("§aO temporizador desta sala foi alterado para §f5 segundos §f(§7Estágio: INICIANDO§f)§a.");
            }
        }
    }

    @Command(name = "abilities", aliases = {"kit", "kitselect"}, inGameOnly = true)
    public void kitCommand(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        Scoreboard scoreboard = matchPlayer.getScoreboard();
        if (args.length == 0) {
            player.sendMessage("§cUso do /" + context.getCommandLabel() + ":");
            player.sendMessage("§c* /" + context.getCommandLabel() + " <abilities>");
            return;
        }

        for (String name : matchPlayer.getKits()) {
            if (Platform.getKitLoader().getAbility(name) == null) {
                continue;
            }
            Kit kit = Platform.getKitLoader().getAbility(name);
            player.closeInventory();
            KitLoader kitLoader = Platform.getKitLoader();
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase(kit.getName())) {
                    if (matchPlayer.getKit().equalsIgnoreCase(kit.getName())) {
                        player.sendMessage("§cO kit '§e'" + kit.getName() + "§c' já está selecionado!");
                        return;
                    }
                    matchPlayer.setKit(kit.getName());
                    player.sendMessage("§eO kit '§b" + kit.getName() + "§e' foi selecionado!");
                    return;
                }
            }
        }
        player.sendMessage("§cUso do /" + context.getCommandLabel() + ":");
        player.sendMessage("§c* /" + context.getCommandLabel() + " <abilities>");
    }

    @Command(name = "perk", aliases = {"perks", "perkselector"}, inGameOnly = true)
    public void perkCommand(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        Scoreboard scoreboard = matchPlayer.getScoreboard();
        if (args.length == 0) {
            player.sendMessage("§cUso do /" + context.getCommandLabel() + ":");
            player.sendMessage("§c* /" + context.getCommandLabel() + " <abilities>");
            return;
        }

        for (String name : matchPlayer.getPerks()) {
            if (Platform.getPerkLoader().getAbility(name) == null) {
                continue;
            }
            Perks kit = Platform.getPerkLoader().getAbility(name);
            player.closeInventory();
            PerkLoader kitLoader = Platform.getPerkLoader();
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase(kit.getName())) {
                    if (matchPlayer.getPerk().equalsIgnoreCase(kit.getName())) {
                        player.sendMessage("§cA perk '§e'" + kit.getName() + "§c' já está selecionada!");
                        return;
                    }
                    matchPlayer.setPerk(kit.getName());
                    player.sendMessage("§eA perk '§b" + kit.getName() + "§e' foi selecionada!");
                    return;
                }
            }
        }
        player.sendMessage("§cUso do /" + context.getCommandLabel() + ":");
        player.sendMessage("§c* /" + context.getCommandLabel() + " <abilities>");
    }
}