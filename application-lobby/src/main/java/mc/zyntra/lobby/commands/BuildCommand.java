package mc.zyntra.lobby.commands;

import mc.zyntra.Main;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.lobby.generator.user.User;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandClass {

    @Command(
            name = "build",
            group = Group.ADMIN,
            inGameOnly = true
    )
    public void build(CommandContext context) {
        Player player = context.getPlayer();
        User user = Main.getInstance().getUserParser().get(player.getUniqueId());
        user.setBuildEnabled(!user.isBuildEnabled());
        player.sendMessage(user.isBuildEnabled()
                ? "§aVocê habilitou o modo construção."
                : "§cVocê desabilitou o modo construção.");
    }
}
