package mc.zyntra.general.account.tag.enums;

import lombok.Getter;

@Getter
public enum Tag {

    ADMIN("Admin", "§4", "A", false, "administrator", "admin", "sênior"),
    MANAGER("Manager", "§5", "B", false, "manager", "gerente", "gerencia"),
    MODERATOR("Moderador", "§2", "D", false, "mod", "moderate"),
    HELPER("Helper", "§9", "G", false, "ajudante", "estagiario", "startstaffer"),
    CREATOR("Creator", "§3", "H", false, "youtuberplus", "creator+", "creatorplus"),
    PARTNER("Partner", "§b", "I", false, "youtuber", "creator"),
    STREAMER("Streamer", "§5", "J", false, "stream", "live"),

    PINTO("Pinto", "§d", "K", true, "pau", "piroca"),
    BETA("Beta", "§1", "L", true),

    EMERALD("Esmeralda", "§a", "O", false),
    DIAMOND("Diamante", "§b", "P", false),
    GOLD("Ouro", "§e", "Q", false),
    MEMBRO("Membro", "§7", "Z", false);

    private final String name;
    private final String color;
    private final String team;
    private final boolean exclusive;
    private final String[] aliases;

    Tag(String name, String color, String team, boolean exclusive, String... aliases) {
        this.name = name;
        this.color = color;
        this.team = team;
        this.aliases = aliases;
        this.exclusive = exclusive;
    }

    public String getColoredName() {
        return color + name;
    }

    public static Tag fromName(String name) {
        for (Tag tag : Tag.values()) {
            if (tag.name().equalsIgnoreCase(name)) return tag;
            if (tag.getName().equalsIgnoreCase(name)) return tag;
            for (String alias : tag.getAliases())
                if (alias.equalsIgnoreCase(name)) return tag;
        }
        return null;
    }
}
