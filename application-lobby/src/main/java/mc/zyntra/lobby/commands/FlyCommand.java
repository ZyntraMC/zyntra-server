package mc.zyntra.lobby.commands;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.account.group.Group;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandClass {

    @Command(
            name = "fly",
            group = Group.GOLD,
            inGameOnly = true
    )
    public void fly(CommandContext context) {
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        zyntraPlayer.getConfiguration().setEnabled(ConfigType.FLY, !zyntraPlayer.getConfiguration().isEnabled(ConfigType.FLY));
        zyntraPlayer.update("configuration");

        player.setAllowFlight(zyntraPlayer.getConfiguration().isEnabled(ConfigType.FLY));
        player.setFlying(zyntraPlayer.getConfiguration().isEnabled(ConfigType.FLY));
        player.sendMessage(zyntraPlayer.getConfiguration().isEnabled(ConfigType.FLY)
                ? "§aVocê habilitou o modo voar."
                : "§cVocê desabilitou o modo voar.");
    }
}
