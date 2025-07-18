package mc.zyntra.bukkit.player.commands;

import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandFramework;
import mc.zyntra.general.command.Completer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;

public class BukkitCommandFramework implements CommandFramework, CommandExecutor {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");

    private final JavaPlugin plugin;
    private final Map<String, Entry<Method, Object>> stringEntryMap = new HashMap<>();
    private CommandMap commandMap;
    private String inGameOnlyMessage = "This command can only be executed ingame.";

    public BukkitCommandFramework(final JavaPlugin plugin) {
        this.plugin = plugin;
        if (plugin.getServer().getPluginManager() instanceof SimplePluginManager) {
            final SimplePluginManager manager = (SimplePluginManager) plugin.getServer().getPluginManager();
            try {
                final Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap) field.get(manager);
            } catch (final IllegalArgumentException | SecurityException | NoSuchFieldException |
                           IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getCommandLabels() {
        return stringEntryMap.keySet().toArray(new String[0]);
    }

    @Override
    public void registerCommand(final Command command, final Method method, final CommandClass commandObject) {
        final List<String> labels = new ArrayList<>();
        labels.add(command.name());
        labels.addAll(Arrays.asList(command.aliases()));
        for (final String label : labels) {
            stringEntryMap.put(label.toLowerCase(), new SimpleEntry<>(method, commandObject));
            final String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
            if (commandMap.getCommand(cmdLabel) == null) {
                final org.bukkit.command.Command cmd = new BukkitCommand(cmdLabel, method.getDeclaredAnnotation(Command.class), this, commandObject, inGameOnlyMessage);
                commandMap.register(plugin.getName(), cmd);
            }
            if (!command.description().equalsIgnoreCase("") && cmdLabel.equals(label)) {
                commandMap.getCommand(cmdLabel).setDescription(command.description());
            }
            if (!command.usage().equalsIgnoreCase("") && cmdLabel.equals(label)) {
                commandMap.getCommand(cmdLabel).setUsage(command.usage());
            }
        }
    }

    @Override
    public void registerCompleter(final Completer completer, final Method method, final CommandClass commandObject) {
        final List<String> labels = new ArrayList<>();
        labels.add(completer.name());
        labels.addAll(Arrays.asList(completer.aliases()));
        for (final String label : labels) {
            final String cmdLabel = label.replace(".", ",").split(",")[0].toLowerCase();
            if (commandMap.getCommand(cmdLabel) instanceof BukkitCommand) {
                final BukkitCommand command = (BukkitCommand) commandMap.getCommand(cmdLabel);
                if (command.completer == null) {
                    command.completer = new BukkitCompleter();
                }
                command.completer.addCompleter(label, method, commandObject);
            } else if (commandMap.getCommand(cmdLabel) instanceof PluginCommand) {
                try {
                    final Object command = commandMap.getCommand(cmdLabel);
                    final Field field = command.getClass().getDeclaredField("completer");
                    field.setAccessible(true);
                    if (field.get(command) == null) {
                        final BukkitCompleter completerIksde = new BukkitCompleter();
                        completerIksde.addCompleter(label, method, commandObject);
                        field.set(command, completer);
                    } else if (field.get(command) instanceof BukkitCompleter) {
                        final BukkitCompleter completerIksde = (BukkitCompleter) field.get(command);
                        completerIksde.addCompleter(label, method, commandObject);
                    } else {
                        plugin.getLogger().warning("Unable to register tab completer " + method.getName() + ". A tab completer is already registered for that command!");
                    }
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setInGameOnlyMessage(final String msg) {
        inGameOnlyMessage = msg;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
        for (int i = args.length; i >= 0; i--) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append(label.toLowerCase());
            for (int x = 0; x < i; x++) {
                buffer.append(".").append(args[x].toLowerCase());
            }
            final String cmdLabel = buffer.toString();
            if (stringEntryMap.containsKey(cmdLabel)) {
                final Method method = stringEntryMap.get(cmdLabel).getKey();
                final Object methodObject = stringEntryMap.get(cmdLabel).getValue();
                try {
                    method.invoke(methodObject, new BukkitCommandContext(sender, args, label, cmdLabel.split("\\.").length - 1));
                } catch (final Exception e) {
                    sender.sendMessage("§cAn exception occurred while executing this command:");
                    sender.sendMessage("§cCommand: §6" + cmdLabel + " (" + methodObject.getClass().getName() + ")");
                    sender.sendMessage("§cException: §6" + e.getClass().getName());
                    sender.sendMessage("§cTimestamp: §6" + DATE_FORMAT.format(new Date()));
                    sender.sendMessage("§cPlease report this issue to a staff member.");
                    e.printStackTrace();
                }
                return true;
            }
        }
        sender.sendMessage("§cThis command is not handled. What a shame...");
        return true;
    }
}