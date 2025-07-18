package mc.zyntra.lobby.commands;

import mc.zyntra.Main;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.lobby.npc.NPCController;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SetCommand implements CommandClass {

    private final NPCController npcController = Main.getInstance().getNpcController();

    @Command(
            name = "var",
            group = Group.ADMIN,
            inGameOnly = true
    )
    public void set(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();

        if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
            Main.getInstance().saveLocation(player.getLocation(), "spawn");
            player.sendMessage("§aVocê definiu a localização do §fspawn §acom sucesso.");
            return;
        }

        if (args.length >= 2) {
            switch (args[0].toLowerCase()) {
                case "npc": {
                    String npcType = args[1].toLowerCase();
                    String configPath = "npcs." + npcType;

                    Main.getInstance().saveLocation(player.getLocation(), configPath);

                    if (args.length >= 3) {
                        String skinName = args[2];
                        Main.getInstance().getConfig().set(configPath + ".skin", skinName);
                        Main.getInstance().saveConfig();
                        player.sendMessage("§aVocê definiu a skin do npc §f" + npcType + " §acom sucesso: §f" + skinName);
                    }

                    player.sendMessage("§aVocê definiu a localização do npc §f" + npcType + " §acom sucesso.");

                    Material handItem;
                    switch (npcType) {
                        case "sw":
                            handItem = Material.BOW;
                            break;
                        case "bw":
                            handItem = Material.BED;
                            break;
                        case "pvp":
                            handItem = Material.STONE_SWORD;
                            break;
                        default:
                            player.sendMessage("§c§lERROR ➜ §r§cTipo de NPC desconhecido para respawn.");
                            return;
                    }

                    npcController.respawnNPC(
                            configPath,
                            handItem,
                            Arrays.asList("§b§l" + npcType.toUpperCase(), "§eClique para jogar!", "§70 jogando agora."),
                            npcController.getClickAction(npcType)
                    );

                    return;
                }

                case "hologram": {
                    Main.getInstance().saveLocation(player.getLocation(), "hologram." + args[1].toLowerCase());
                    player.sendMessage("§aVocê definiu a localização do holograma §f" + args[1].toLowerCase() + " §acom sucesso.");
                    return;
                }

                default:
                    break;
            }
        }

        player.sendMessage("§cUso do /" + context.getCommandLabel() + ":");
        player.sendMessage("§c* /" + context.getCommandLabel() + " spawn");
        player.sendMessage("§c* /" + context.getCommandLabel() + " npc <sw|bw|pvp> [skin]");
        player.sendMessage("§c* /" + context.getCommandLabel() + " hologram <type>");
    }
}
