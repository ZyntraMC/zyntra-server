package mc.zyntra.general.controller;

import mc.zyntra.general.report.Report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportController {

    private final Map<UUID, Report> reportMap = new HashMap<>();

    public void create(UUID uniqueId, Report report) {
        reportMap.put(uniqueId, report);
    }

    public void remove(UUID uniqueId) {
        reportMap.remove(uniqueId);
    }

    public Report get(UUID uniqueId) {
        return reportMap.get(uniqueId);
    }

    public boolean contains(UUID uniqueId) {
        return reportMap.containsKey(uniqueId);
    }

    public Collection<Report> list() {
        return reportMap.values();
    }
}
