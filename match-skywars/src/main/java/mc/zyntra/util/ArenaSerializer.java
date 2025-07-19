package mc.zyntra.util;

import mc.zyntra.Skywars;
import mc.zyntra.arena.Arena;
import mc.zyntra.arena.enums.ArenaType;
import mc.zyntra.arena.structure.Island;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaSerializer {

    public static Arena deserialize(YamlConfiguration cfg) {
        String name = cfg.getString("name");
        if (name == null) return null;

        Arena arena = new Arena(name);

        arena.setType(ArenaType.valueOf(cfg.getString("type", "NORMAL")));
        arena.setMinPlayers(cfg.getInt("minPlayers", 2));
        arena.setMaxPlayers(cfg.getInt("maxPlayers", 16));

        // Lobby Location
        arena.setLobbyLocation(deserializeLocation(cfg.getConfigurationSection("lobbyLocation")));

        // Islands
        List<Island> islands = new ArrayList<>();
        ConfigurationSection islandsSection = cfg.getConfigurationSection("islands");
        if (islandsSection != null) {
            for (String key : islandsSection.getKeys(false)) {
                ConfigurationSection islandSection = islandsSection.getConfigurationSection(key);

                Location spawn = deserializeLocation(islandSection.getConfigurationSection("spawn"));
                Location cage = deserializeLocation(islandSection.getConfigurationSection("cageOrigin"));

                Island island = new Island(spawn, cage);

                // Chest locations
                ConfigurationSection chestsSection = islandSection.getConfigurationSection("chests");
                if (chestsSection != null) {
                    for (String chestKey : chestsSection.getKeys(false)) {
                        Location chestLoc = deserializeLocation(chestsSection.getConfigurationSection(chestKey));
                        if (chestLoc != null) {
                            island.addChestLocation(chestLoc);
                        }
                    }
                }

                islands.add(island);
            }
        }

        arena.getIslands().clear();
        arena.getIslands().addAll(islands);

        // Feasts
        ConfigurationSection feastSection = cfg.getConfigurationSection("feastChests");
        if (feastSection != null) {
            for (String key : feastSection.getKeys(false)) {
                Location loc = deserializeLocation(feastSection.getConfigurationSection(key));
                if (loc != null) arena.addFeastChest(loc);
            }
        }

        // MiniFeasts
        ConfigurationSection miniFeastSection = cfg.getConfigurationSection("miniFeastChests");
        if (miniFeastSection != null) {
            for (String key : miniFeastSection.getKeys(false)) {
                Location loc = deserializeLocation(miniFeastSection.getConfigurationSection(key));
                if (loc != null) arena.addMiniFeastChest(loc);
            }
        }

        return arena;
    }

    public static void serialize(Arena arena, YamlConfiguration cfg) {
        cfg.set("name", arena.getName());
        cfg.set("type", arena.getType().name());
        cfg.set("minPlayers", arena.getMinPlayers());
        cfg.set("maxPlayers", arena.getMaxPlayers());

        serializeLocation(cfg.createSection("lobbyLocation"), arena.getLobbyLocation());

        // Islands
        ConfigurationSection islandsSection = cfg.createSection("islands");
        int i = 0;
        for (Island island : arena.getIslands()) {
            ConfigurationSection islandSection = islandsSection.createSection("island" + i);

            serializeLocation(islandSection.createSection("spawn"), island.getSpawn());
            serializeLocation(islandSection.createSection("cageOrigin"), island.getCageOrigin());

            ConfigurationSection chestsSection = islandSection.createSection("chests");
            int c = 0;
            for (Location chestLoc : island.getChestLocations().keySet()) {
                serializeLocation(chestsSection.createSection("chest" + c++), chestLoc);
            }

            i++;
        }

        // Feasts
        ConfigurationSection feastSection = cfg.createSection("feastChests");
        int f = 0;
        for (Location loc : arena.getFeastChestLocations()) {
            serializeLocation(feastSection.createSection("chest" + f++), loc);
        }

        // MiniFeasts
        ConfigurationSection miniFeastSection = cfg.createSection("miniFeastChests");
        int m = 0;
        for (Location loc : arena.getMiniFeastChestLocations()) {
            serializeLocation(miniFeastSection.createSection("chest" + m++), loc);
        }
    }

    private static Location deserializeLocation(ConfigurationSection section) {
        if (section == null) return null;

        String world = section.getString("world");
        if (world == null) return null;

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");
        float pitch = (float) section.getDouble("pitch");

        return new Location(Skywars.getInstance().getServer().getWorld(world), x, y, z, yaw, pitch);
    }

    private static void serializeLocation(ConfigurationSection section, Location loc) {
        if (loc == null) return;

        section.set("world", loc.getWorld().getName());
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());
        section.set("yaw", loc.getYaw());
        section.set("pitch", loc.getPitch());
    }
}
