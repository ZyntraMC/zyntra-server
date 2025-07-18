package mc.zyntra.general.controller;

import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.group.Group;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountController {

    private final Map<UUID, ZyntraPlayer> accountMap = new HashMap<>();

    public void create(ZyntraPlayer zyntraPlayer) {
        accountMap.put(zyntraPlayer.getUniqueId(), zyntraPlayer);
    }

    public void remove(UUID uniqueId) {
        accountMap.remove(uniqueId);
    }

    public ZyntraPlayer get(UUID uniqueId) {
        return accountMap.get(uniqueId);
    }

    public ZyntraPlayer get(String name) {
        return list().stream().filter(zyntraPlayer -> zyntraPlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Collection<ZyntraPlayer> list() {
        return accountMap.values();
    }

    public void notify(String message) {
        notify(Group.ADMIN, message);
    }

    public void notify(Group group, String message) {
        list().stream()
                .filter(zyntraPlayer -> zyntraPlayer.hasGroupPermission(group) && zyntraPlayer.getConfiguration().isEnabled(ConfigType.SEEING_LOGS))
                .forEach(zyntraPlayer -> zyntraPlayer.sendMessage("§7§o[" + message + "]"));
    }

    public void broadcast(String message) {
        broadcast(Group.DEFAULT, message);
    }

    public void broadcast(Group group, String message) {
        list().stream()
                .filter(zyntraPlayer -> zyntraPlayer.hasGroupPermission(group))
                .forEach(zyntraPlayer -> zyntraPlayer.sendMessage(message));
    }
}
