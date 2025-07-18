package mc.zyntra.general.data.impl;

import mc.zyntra.general.Core;
import mc.zyntra.general.data.DataReport;
import mc.zyntra.general.report.Report;
import mc.zyntra.general.utils.json.JsonUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

public class DataReportImpl implements DataReport {

    @Override
    public void create(Report report) {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            jedis.sadd("reports", report.getPlayerUniqueId().toString());
            jedis.hmset("report:" + report.getPlayerUniqueId().toString(), JsonUtils.objectToMap(report));
        }
    }

    @Override
    public void update(Report report) {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            jedis.hmset("report:" + report.getPlayerUniqueId().toString(), JsonUtils.objectToMap(report));
        }
    }

    @Override
    public void delete(UUID uniqueId) {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            jedis.srem("reports", uniqueId.toString());
            jedis.del("report:" + uniqueId.toString());
        }
    }

    @Override
    public boolean exists(UUID uniqueId) {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            for (String uuid : jedis.sunion("reports")) {
                if (uniqueId.toString().equals(uuid))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Report getReport(UUID uniqueId) {
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            return JsonUtils.mapToObject(jedis.hgetAll("report:" + uniqueId.toString()), Report.class);
        }
    }

    @Override
    public Set<Report> getReports() {
        Set<Report> reports = new HashSet<>();
        try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
            for (String uniqueId : jedis.sunion("reports")) {
                Map<String, String> map = jedis.hgetAll("report:" + uniqueId);
                Report report = JsonUtils.mapToObject(map, Report.class);
                reports.add(report);
            }
        }
        return reports;
    }
}
