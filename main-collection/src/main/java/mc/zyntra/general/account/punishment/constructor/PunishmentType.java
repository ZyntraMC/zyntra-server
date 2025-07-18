package mc.zyntra.general.account.punishment.constructor;

import mc.zyntra.general.account.group.Group;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PunishmentType {

    BAN,
    MUTE,
    BLOCK_REPORT,

    BLACKLIST_EVENT,
    BLACKLIST;

    public Group getRequiredGroup() {
        switch (this) {
            case BLACKLIST_EVENT:
                return Group.MANAGER;
            case BLACKLIST:
                return Group.ADMIN;
            default:
                return Group.MODERATOR;
        }
    }
}
