package mc.zyntra.general.data;

import mc.zyntra.general.report.Report;

import java.util.Collection;
import java.util.UUID;

public interface DataReport {

    void create(Report report);

    void update(Report report);

    void delete(UUID uniqueId);

    boolean exists(UUID uniqueId);

    Report getReport(UUID uniqueId);

    Collection<Report> getReports();
    
}
