package mc.zyntra.general.account.group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mc.zyntra.general.account.tag.enums.Tag;

@Getter
@RequiredArgsConstructor
public enum Group {

    ADMIN("Admin", "§4", Tag.ADMIN, GroupCategory.STAFF),
    MANAGER("Manager", "§5", Tag.MANAGER, GroupCategory.STAFF),
    MODERATOR("Moderator", "§2", Tag.MODERATOR, GroupCategory.STAFF),
    HELPER("Helper", "§9", Tag.HELPER, GroupCategory.STAFF),
    CREATOR("Creator", "§3", Tag.CREATOR, GroupCategory.STAFF),
    PARTNER("Partner", "§b", Tag.PARTNER, GroupCategory.MEDIA),
    STREAMER("Streamer", "§5", Tag.STREAMER, GroupCategory.MEDIA),
    BETA("Beta", "§1", Tag.BETA, GroupCategory.VIP),
    EMERALD("Emerald", "§a", Tag.EMERALD, GroupCategory.VIP),
    DIAMOND("Diamond", "§b", Tag.DIAMOND, GroupCategory.VIP),
    GOLD("Gold", "§e", Tag.GOLD, GroupCategory.VIP),
    DEFAULT("Membro", "§7", Tag.MEMBRO, GroupCategory.DEFAULT);

    private final String name;
    private final String color;
    private final Tag tag;
    private final GroupCategory category;

    public String getColoredName() {
        return color + name;
    }

    public static Group fromName(String name) {
        for (Group group : Group.values()) {
            if (group.getName().equalsIgnoreCase(name))
                return group;

            if (group.name().replace("_", "").replace("PLUS", "+").equalsIgnoreCase(name))
                return group;
        }
        return null;
    }
}
