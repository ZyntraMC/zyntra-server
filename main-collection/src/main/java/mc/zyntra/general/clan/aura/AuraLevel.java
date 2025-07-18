package mc.zyntra.general.clan.aura;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuraLevel {

    STARTER("Iniciante", 0),
    BRONZE("Bronze", 200),
    SILVER("Prata", 500),
    GOLD("Ouro", 900),
    PLATINUM("Platina", 1400),
    DIAMOND("Diamante", 2000),
    MASTER("Master", 2800),
    LEGENDARY("Lendário", 3800),
    MYTHIC("Mítico", 5000),
    DIVINE("Divíno", 10000);

    private final String displayName;
    private final int requiredAura;

    public static AuraLevel fromAura(int aura) {
        AuraLevel result = STARTER;
        for (AuraLevel level : values()) {
            if (aura >= level.requiredAura) {
                result = level;
            } else {
                break;
            }
        }
        return result;
    }

    public AuraLevel getNextLevel() {
        boolean foundCurrent = false;
        for (AuraLevel level : values()) {
            if (foundCurrent) {
                return level;
            }
            if (level == this) {
                foundCurrent = true;
            }
        }
        return null;
    }
}