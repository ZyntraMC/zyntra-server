package mc.zyntra.server.player.commands;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCommand extends Command {

    private final mc.zyntra.general.command.Command command;
    private final String inGameOnlyMsg;
    private final ServerCommandFramework cmd;

    public ServerCommand(final String name, final ServerCommandFramework cmd, final mc.zyntra.general.command.Command command, final String inGameOnlyMsg) {
        super(name);
        this.cmd = cmd;
        this.command = command;
        this.inGameOnlyMsg = inGameOnlyMsg;
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer) && command.inGameOnly()) {
            commandSender.sendMessage(inGameOnlyMsg);
            return;
        }

        if (commandSender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

            if (!zyntraPlayer.isLogged()) {
                player.sendMessage("§cVocê não está logado.");
                return;
            }

            if (!zyntraPlayer.hasGroupPermission(command.group())) {
                player.sendMessage(Constant.COMMAND_NO_ACCESS);
                return;
            }

            if (!command.permission().isEmpty()) {
                if (!player.hasPermission(command.permission())) {
                    player.sendMessage(Constant.COMMAND_NO_ACCESS);
                    return;
                }
            }
        }

        cmd.execute(commandSender, getName(), strings);
    }
}
