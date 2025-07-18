package mc.zyntra.general.account.statistics.types;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.statistics.StatisticsType;
import mc.zyntra.general.account.statistics.Statistics;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class GameStatistics extends Statistics {

    private int matchesPlayed = 0,
            wins = 0,
            winstreak = 0,
            bestWinstreak = 0,
            finalKills = 0,
            kills = 0,
            deaths = 0,
            assists = 0,
            exp = 0,
            coins = 0,
            level = 0;

    public GameStatistics(StatisticsType statisticsType, UUID uniqueId) {
        super(statisticsType, uniqueId);
    }

    public void addMatch() {
        matchesPlayed++;

        Core.getDataStatus().update(this, "matchesPlayed");
    }

    public void addWin() {
        wins++;

        Core.getDataStatus().update(this, "wins");
    }

    public void addWinstreak() {
        winstreak++;

        Core.getDataStatus().update(this, "winstreak");
    }

    public void setBestWinStreak() {
        bestWinstreak++;

        Core.getDataStatus().update(this, "bestWistreak");
    }

    public void addKill() {
        kills++;

        Core.getDataStatus().update(this, "kills");
    }

    public void addFinalKill() {
        finalKills++;

        Core.getDataStatus().update(this, "finalKills");
    }


    public void addDeath() {
        deaths++;

        Core.getDataStatus().update(this, "deaths");
    }

    public void addAssist() {
        assists++;

        Core.getDataStatus().update(this, "assists");
    }

    public void addExp(int exp) {
        this.exp += exp;

        Core.getDataStatus().update(this, "exp");
    }

    public void addCoins(int coins) {
        this.coins += coins;

        Core.getDataStatus().update(this, "coins");
    }

    public void addLevel() {
        level++;

        Core.getDataStatus().update(this, "level");
    }
}
