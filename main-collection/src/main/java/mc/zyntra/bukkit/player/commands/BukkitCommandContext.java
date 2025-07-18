package mc.zyntra.bukkit.player.commands;

import mc.zyntra.general.command.CommandContext;
import org.bukkit.command.CommandSender;

public final class BukkitCommandContext implements CommandContext {

    private final CommandSender sender;
    private final String[] args;
    private final String commandLabel;

    public BukkitCommandContext(final CommandSender sender, final String[] args, final String commandLabel, final int subCommand) {
        final String[] modArgs = new String[args.length - subCommand];
        for (int i = 0; i < args.length - subCommand; i++) {
            modArgs[i] = args[i + subCommand];
        }
        final StringBuilder buffer = new StringBuilder();
        buffer.append(commandLabel);
        for (int x = 0; x < subCommand; x++) {
            buffer.append(".").append(args[x]);
        }
        final String cmdLabel = buffer.toString();
        this.sender = sender;
        this.args = modArgs;
        this.commandLabel = cmdLabel;
    }

    @Override
    public <T> T getSender(final Class<T> type) {
        if (type.isAssignableFrom(sender.getClass())) {
            return (T) sender;
        }
        return null;
    }

    @Override
    public String[] getArguments() {
        return args;
    }

    @Override
    public String getCommandLabel() {
        return commandLabel;
    }

}