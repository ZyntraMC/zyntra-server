package mc.zyntra.match.generator.player.loader;

import mc.zyntra.match.generator.player.MatchPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;


public class PlayerLoader {

    @Getter
    private HashMap<UUID, MatchPlayer> gamerMap = new HashMap<>();

    public void create(MatchPlayer matchPlayer) {
        gamerMap.put(matchPlayer.getUniqueId(), matchPlayer);
    }

    public boolean containsKey(UUID key) {
        return gamerMap.containsKey(key);
    }

    public MatchPlayer get(UUID uniqueId) {
        return gamerMap.get(uniqueId);
    }

    public MatchPlayer get(Player player) {
        return gamerMap.get(player.getUniqueId());
    }

    public void load(MatchPlayer matchPlayer) {
        gamerMap.put(matchPlayer.getUniqueId(), matchPlayer);
    }

    public void unload(UUID uniqueId) {
        gamerMap.remove(uniqueId);
    }

    public Collection<MatchPlayer> list() {
        return gamerMap.values();
    }
}

