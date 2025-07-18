package mc.zyntra.general.account.statistics;

import mc.zyntra.general.account.statistics.types.GameStatistics;
import mc.zyntra.general.account.statistics.types.NormalStatistics;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatisticsType {

    PVP_ARENA("pvp_arena", NormalStatistics.class),
    PVP_FPS("pvp_fps", NormalStatistics.class),
    PVP_LAVA("pvp_lava", NormalStatistics.class),

    UHC("uhc", GameStatistics.class),
    SIMULATOR("simulator", GameStatistics.class),
    SOUP("soup", GameStatistics.class),
    GLADIATOR("gladiator", GameStatistics.class),
    BOXING("boxing", GameStatistics.class),

    SW_SOLO("sw_solo", GameStatistics.class), SW_DOUBLES("sw_doubles", GameStatistics.class),

    BW_SOLO("bw_solo", GameStatistics.class), BW_DOUBLES("bw_doubles", GameStatistics.class);

    private final String mongoCollection;
    private final Class<? extends Statistics> statusClass;

}
