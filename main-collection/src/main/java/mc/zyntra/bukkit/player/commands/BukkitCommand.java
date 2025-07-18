package mc.zyntra.bukkit.player.commands;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.command.CommandClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BukkitCommand extends Command {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

    private final mc.zyntra.general.command.Command command;
    private final CommandClass commandObject;
    private final String inGameOnlyMsg;
    private final CommandExecutor executor;
    protected BukkitCompleter completer;

    protected BukkitCommand(final String name, final mc.zyntra.general.command.Command command, final CommandExecutor executor,
                            final CommandClass commandObject, final String inGameOnlyMsg) {
        super(name);
        this.command = command;
        this.executor = executor;
        this.commandObject = commandObject;
        this.inGameOnlyMsg = inGameOnlyMsg;
    }

    @Override
    public boolean execute(final CommandSender commandSender, final String commandLabel, final String[] args) {
        if (!(commandSender instanceof Player) && command.inGameOnly()) {
            commandSender.sendMessage(inGameOnlyMsg);
            return false;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

            if (!zyntraPlayer.isLogged()) {
                player.sendMessage("§cVocê não está logado.");
                return false;
            }

            if (!zyntraPlayer.hasGroupPermission(command.group())) {
                player.sendMessage(Constant.COMMAND_NO_ACCESS);
                return false;
            }

            if (!command.permission().isEmpty()) {
                if (!player.hasPermission(command.permission())) {
                    player.sendMessage(Constant.COMMAND_NO_ACCESS);
                    return false;
                }
            }
        }

        executor.onCommand(commandSender, this, commandLabel, args);
        return true;
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        List<String> completions = null;
        try {
            if (completer != null) {
                completions = completer.onTabComplete(sender, this, alias, args);
            }
        } catch (final Throwable throwable) {
            sender.sendMessage("§cAn exception occurred while executing this tab completer:");
            sender.sendMessage("§cCompleter: §6" + alias + " (" + commandObject.getClass().getName() + ")");
            sender.sendMessage("§cException: §6" + throwable.getClass().getName());
            sender.sendMessage("§cTimestamp: §6" + DATE_FORMAT.format(new Date()));
            sender.sendMessage("§cPlease report this issue to a staff member.");
            throwable.printStackTrace();
        }

        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }
        return completions;
    }
}