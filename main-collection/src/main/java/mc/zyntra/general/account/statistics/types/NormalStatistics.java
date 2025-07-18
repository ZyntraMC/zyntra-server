package mc.zyntra.general.account.statistics.types;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.statistics.Statistics;
import mc.zyntra.general.account.statistics.StatisticsType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class NormalStatistics extends Statistics {

    private int kills = 0,
            deaths = 0,
            killstreak = 0,
            easy = 0,
            medium = 0,
            difficult = 0,
            extreme = 0,
            xp = 0,
            coins = 0;

    public NormalStatistics(StatisticsType statisticsType, UUID uniqueId) {
        super(statisticsType, uniqueId);
    }

    public void setKills(int value) {
        kills += value;

        Core.getDataStatus().update(this, "kills");
    }

    public void setXp(int exp) {
        this.xp += exp;

        Core.getDataStatus().update(this, "xp");
    }

    public void setCoins(int coins) {
        this.coins += coins;

        Core.getDataStatus().update(this, "coins");
    }

    public void setDeaths(int value) {
        deaths += value;

        Core.getDataStatus().update(this, "deaths");
    }

    public void setKillstreak(int value) {
        killstreak += value;

        Core.getDataStatus().update(this, "killstreak");
    }

    public void setLavaEasy(int value) {
        easy += value;

        Core.getDataStatus().update(this, "easy");
    }

    public void setLavaMedium(int value) {
        medium += value;

        Core.getDataStatus().update(this, "medium");
    }

    public void setLavaDifficult(int value) {
        difficult += value;

        Core.getDataStatus().update(this, "difficult");
    }

    public void setLavaExtreme(int value) {
        extreme += value;

        Core.getDataStatus().update(this, "extreme");
    }
}
