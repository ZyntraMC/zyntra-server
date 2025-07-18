package mc.zyntra.general.account.medal.enums;

import lombok.Getter;

@Getter
public enum Medal {

    NONE("§7", "§7", "Nenhum"),
    PEACE_LOVE("§a", "✌", "Paz e Amor"),
    HEART("§4", "❤", "Coração do Amor"),
    UPSET_HEART("§4", "❥", "Coração Chateado"),
    SMILE("§e", "ツ", "Sorriso"),
    TOXIC("§e", "☣", "Tóxico"),
    RAY("§6", "⚡", "Raio Dourado"),
    MUSIC("§5", "♫", "Músico"),
    COFFEE("§8", "☕", "Café da Manhã"),
    RADIOACTIVE("§6", "☢", "Radiativo"),
    SKELETON("§7", "☠", "Crânio de Esqueleto"),
    UMBRELLA("§d", "☂", "Guarda Chuva"),
    HALLOWEEN_CROSS("§f", "✞", "Cruz de Halloween"),
    CROSS("§7", "✠", "Frequento a Igreja"),
    WRONG("§c", "✘", "Negativado"),
    CORRECT("§a", "✔", "Positivamente"),
    YIN_YANG("§f", "☯", "Yin Yang"),
    STARRY("§b", "❄", "Estrela Cadente"),
    SIGNAL("§2", "♻", "Semáforo"),
    STUDENT("§e", "✍", "Estudante"),
    EMAIL("§e", "✉", "Caixa de Entrada"),
    F("§f", "Ⓕ", "Press F for Respect"),
    NITRO("§d", "✶", "Nitro"),
    DESTAQUE("§3", "✯", "Destaque"),
    GAY("§e", "§6§lG§e§lA§a§lY", "Gay");

    private String color;
    private String symbol;
    private String name;

    Medal(String color, String symbol, String name) {
        this.color = color;
        this.symbol = symbol;
        this.name = "§" + color + name;
    }

    public static Medal fromName(String name) {
        try {
            return Medal.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
