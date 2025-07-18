package mc.zyntra.general.controller;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.statistics.Statistics;
import mc.zyntra.general.account.statistics.StatisticsType;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatisticsController {

    private final Map<UUID, Map<StatisticsType, Statistics>> statisticsMap = new HashMap<>();

    public Statistics loadStatistics(UUID uniqueId, StatisticsType statisticsType) {
        Map<StatisticsType, Statistics> map = statisticsMap.containsKey(uniqueId) ? statisticsMap.get(uniqueId)
                : statisticsMap.computeIfAbsent(uniqueId, v -> new HashMap<>());

        if (map.containsKey(statisticsType))
            return map.get(statisticsType);

        Statistics statistics = Core.getDataStatus().load(uniqueId, statisticsType);

        if (statistics == null) {
            try {
                statistics = statisticsType.getStatusClass().getConstructor(StatisticsType.class, UUID.class).newInstance(statisticsType, uniqueId);
                Core.getDataStatus().save(statistics);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        map.put(statisticsType, statistics);
        return statistics;
    }

    public <T> T loadStatistics(UUID uniqueId, StatisticsType statisticsType, Class<? extends T> clazz) {
        Map<StatisticsType, Statistics> map = statisticsMap.containsKey(uniqueId) ? statisticsMap.get(uniqueId)
                : statisticsMap.computeIfAbsent(uniqueId, v -> new HashMap<>());

        if (map.containsKey(statisticsType))
            return clazz.cast(map.get(statisticsType));

        Statistics statistics = Core.getDataStatus().load(uniqueId, statisticsType);

        if (statistics == null) {
            try {
                statistics = statisticsType.getStatusClass().getConstructor(StatisticsType.class, UUID.class).newInstance(statisticsType, uniqueId);
                Core.getDataStatus().save(statistics);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                     | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }

        map.put(statisticsType, statistics);
        return clazz.cast(map.get(statisticsType));
    }

    public void unloadStatistics(UUID uniqueId, StatisticsType statisticsType) {
        if (statisticsMap.containsKey(uniqueId))
            statisticsMap.get(uniqueId).remove(statisticsType);
    }

    public void unloadStatistics(UUID uniqueId) {
        statisticsMap.remove(uniqueId);
    }
}
