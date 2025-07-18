package mc.zyntra.player.commands.configuration;

import mc.zyntra.Secure;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import org.bukkit.entity.Player;

public class ServerCommands implements CommandClass {

    @Command(
            name = "setspawn",
            group = Group.ADMIN,
            inGameOnly = true
    )
    public void setspawn(CommandContext context) {
        Player player = context.getPlayer();
        Secure.getInstance().saveLocation(player.getLocation(), "spawn");
        player.sendMessage("§aVocê definiu a localização do spawn.");
    }
}
