package mc.zyntra.player.generator.parser;

import mc.zyntra.player.generator.User;

import java.util.*;

public class UserParser {

    private final Map<UUID, User> gamerMap = new HashMap<>();

    public void create(User user) {
        gamerMap.put(user.getUniqueId(), user);
    }

    public void remove(UUID uniqueId) {
        gamerMap.remove(uniqueId);
    }

    public User get(UUID uniqueId) {
        return gamerMap.get(uniqueId);
    }

    public Collection<User> list() {
        return gamerMap.values();
    }
}
