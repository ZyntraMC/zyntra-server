package mc.zyntra.arena.controller;

import mc.zyntra.arena.Arena;
import mc.zyntra.util.ArenaSerializer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;

public class ArenaController {

    private final Map<String, Arena> arenas = new HashMap<>();
    private final Map<UUID, Arena> setupArenaMap = new HashMap<>();

    private final File roomsFolder;

    public ArenaController() {
        roomsFolder = new File("plugins/Skywars/rooms");
        if (!roomsFolder.exists()) roomsFolder.mkdirs();

        loadAllArenas();
    }

    public Arena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

    public void addArena(Arena arena) {
        arenas.put(arena.getName().toLowerCase(), arena);
    }

    public void removeArena(String name) {
        arenas.remove(name.toLowerCase());
    }

    public Arena getSetupArena(Player player) {
        return setupArenaMap.get(player.getUniqueId());
    }

    public void setSetupArena(Player player, Arena arena) {
        setupArenaMap.put(player.getUniqueId(), arena);
    }

    public void removeSetupArena(Player player) {
        setupArenaMap.remove(player.getUniqueId());
    }

    public void loadAllArenas() {
        arenas.clear();

        File[] files = roomsFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            try {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                Arena arena = ArenaSerializer.deserialize(cfg);
                if (arena != null) {
                    addArena(arena);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveArena(Arena arena) {
        File file = new File(roomsFolder, arena.getName().toLowerCase() + ".yml");
        YamlConfiguration cfg = new YamlConfiguration();

        ArenaSerializer.serialize(arena, cfg);

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Arena getArenaByPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        for (Arena arena : arenas.values()) {
            if (arena.getPlayers().stream().anyMatch(p -> p.getUniqueId().equals(uuid))) {
                return arena;
            }
            if (arena.getSpectators().stream().anyMatch(p -> p.getUniqueId().equals(uuid))) {
                return arena;
            }
        }
        return null;
    }

}
