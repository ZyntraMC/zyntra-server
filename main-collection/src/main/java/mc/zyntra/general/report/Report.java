package mc.zyntra.general.report;

import mc.zyntra.general.Core;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class Report {

    private final String playerName;
    private final UUID playerUniqueId;
    private final Map<UUID, ReportInfo> reports;

    @Setter
    private ReportInfo lastReport;

    @Setter
    private long lastReportTime;

    @Setter
    private long reportExpire;

    @Setter
    private boolean expire;

    public Report(String playerName, UUID playerUniqueId) {
        this.playerName = playerName;
        this.playerUniqueId = playerUniqueId;

        reports = new HashMap<>();
        lastReport = null;
        lastReportTime = System.currentTimeMillis();
        reportExpire = System.currentTimeMillis() / 1000 * 5;
        expire = false;
    }

    public boolean isOnline() {
        return Core.getPlatform().isOnline(playerUniqueId);
    }

    public void addReport(String reporterName, UUID reporterUniqueId, String reason) {
        ReportInfo info = new ReportInfo(reporterName, reporterUniqueId, reason);
        getReports().put(reporterUniqueId, info);

        setLastReport(info);
        setLastReportTime(System.currentTimeMillis());
        setReportExpire(System.currentTimeMillis() / 1000 * 5);
    }

    public void remove() {
        Core.getReportController().remove(playerUniqueId);
    }

    @Getter
    @AllArgsConstructor
    public static class ReportInfo {

        private String reporterName;
        private UUID reporterUniqueId;
        private String reason;

    }
}