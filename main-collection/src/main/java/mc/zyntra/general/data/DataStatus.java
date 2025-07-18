package mc.zyntra.general.data;

import mc.zyntra.general.account.statistics.Statistics;
import mc.zyntra.general.account.statistics.StatisticsType;

import java.util.Collection;
import java.util.UUID;

public interface DataStatus {

    Statistics load(UUID uniqueId, StatisticsType statisticsType);

    void save(Statistics statistics);

    void update(Statistics statistics, String fieldName);

    Collection<Statistics> ranking(StatisticsType statisticsType, String fieldName);

}
