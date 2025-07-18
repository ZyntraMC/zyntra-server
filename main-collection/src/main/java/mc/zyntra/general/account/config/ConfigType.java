package mc.zyntra.general.account.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigType {

    PLAYERS_VISIBILITY(true),
    FLY(false),
    TELL(true),
    CHAT(true),
    STATISTICS_VISIBILITY(true),
    SCOREBOARD(true),

    SEEING_STAFFCHAT(true),
    SEEING_LOGS(true),
    SEEING_ANTICHEAT(true),

    ADMIN_ON_JOIN(true),
    STAFFCHAT(false),

    BUILD_MODE(false);

    private final boolean defaultValue;

}
