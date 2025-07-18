package mc.zyntra.general.clan.member.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    LEADER("Líder", "§e"),
    MANAGER("Manager", "§c"),
    STAFF("Staff", "§a"),
    DEFAULT("Default", "§7");

    private final String name;
    private final String color;

    public String getColoredName() {
        return color + name;
    }

    public String getFormattedName() {
        return color + "[" + name + "]";
    }

    public static MemberRole fromName(String name) {
        for (MemberRole memberRole : MemberRole.values()) {
            if (memberRole.getName().equalsIgnoreCase(name))
                return memberRole;
        }
        return null;
    }
}