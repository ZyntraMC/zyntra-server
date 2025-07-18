package mc.zyntra.general.account.statistics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class Statistics {

    private final StatisticsType statisticsType;
    private final UUID uniqueId;

}
