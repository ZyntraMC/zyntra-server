package mc.zyntra.general.controller;

import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WhitelistController {

    private final Set<UUID> whitelist = new HashSet<>();

    @Setter
    @Getter
    private boolean enabled = false;

    @Setter
    @Getter
    private Group whitelistGroup = Group.ADMIN;

    public boolean canJoin(ZyntraPlayer zyntraPlayer) {
        return !isEnabled() || (zyntraPlayer.hasGroupPermission(whitelistGroup) || whitelist.contains(zyntraPlayer.getUniqueId()));
    }

    public void addWhitelist(UUID uniqueId) {
        whitelist.add(uniqueId);
    }

    public void removeWhitelist(UUID uniqueId) {
        whitelist.remove(uniqueId);
    }

    public boolean containsWhitelist(UUID uniqueId) {
        return whitelist.contains(uniqueId);
    }
}
