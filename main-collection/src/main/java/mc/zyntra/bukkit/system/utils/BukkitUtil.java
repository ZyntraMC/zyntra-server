package mc.zyntra.bukkit.system.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BukkitUtil {

    public static void unregisterBukkitCommand(JavaPlugin plugin, String... commands) {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer());
            Field f2 = commandMap.getClass().getDeclaredField("knownCommands");

            f2.setAccessible(true);
            Map<String, Command> knownCommands = (HashMap<String, Command>) f2.get(commandMap);

            for (String command : commands) {
                if (knownCommands.containsKey(command)) {
                    knownCommands.remove(command);

                    List<String> aliases = new ArrayList<>();

                    for (String key : knownCommands.keySet()) {
                        if (!key.contains(":"))
                            continue;

                        String substr = key.substring(key.indexOf(":") + 1);

                        if (substr.equalsIgnoreCase(command)) {
                            aliases.add(key);
                        }
                    }

                    for (String alias : aliases) {
                        knownCommands.remove(alias);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}