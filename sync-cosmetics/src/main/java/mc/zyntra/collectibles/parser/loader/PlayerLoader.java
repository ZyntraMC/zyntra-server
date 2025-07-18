package mc.zyntra.collectibles.parser.loader;

import lombok.Getter;
import mc.zyntra.collectibles.parser.CollectiblePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;


public class PlayerLoader {

    @Getter
    private HashMap<UUID, CollectiblePlayer> gamerMap = new HashMap<>();

    public void create(CollectiblePlayer player) {
        gamerMap.put(player.getUniqueId(), player);
    }

    public boolean containsKey(UUID key) {
        return gamerMap.containsKey(key);
    }

    public CollectiblePlayer get(UUID uniqueId) {
        return gamerMap.get(uniqueId);
    }

    public CollectiblePlayer get(Player player) {
        return gamerMap.get(player.getUniqueId());
    }

    public void load(CollectiblePlayer player) {
        gamerMap.put(player.getUniqueId(), player);
    }

    public void unload(UUID uniqueId) {
        gamerMap.remove(uniqueId);
    }

    public Collection<CollectiblePlayer> list() {
        return gamerMap.values();
    }
}


