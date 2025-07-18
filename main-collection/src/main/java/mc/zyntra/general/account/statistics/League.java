package mc.zyntra.general.account.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum League {

    UNRANKED("Unranked", "-", "§8", 500),
    BRONZE("Bronze", "★", "§e", 4500),
    SILVER("Silver", "✶", "§7", 17000),
    GOLD("Gold", "✸", "§6", 33000),
    PLATINUM("Platinum", "✯", "§b", 53000),
    DIAMOND("Diamond", "✦", "§9", 67000),
    CRYSTAL("Crystal", "✫", "§5", 80000),
    CHAMPION("Champion", "✬", "§c", 93000),
    LEGENDARY("Legendary", "❂", "§4", 400000);

    private final String name, symbol, color;
    private final int experience;

    public int min() {
        int min = 0;

        if (this.ordinal() > 0) min = League.values()[this.ordinal() - 1].getExperience();

        return min;
    }

    public League getPreviousLeague() {
        return this == UNRANKED ? UNRANKED : League.values()[ordinal() - 1];
    }

    public League getNextLeague() {
        return this == LEGENDARY ? LEGENDARY : League.values()[ordinal() + 1];
    }
}