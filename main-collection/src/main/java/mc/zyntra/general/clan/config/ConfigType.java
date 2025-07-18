package mc.zyntra.general.clan.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConfigType {

    ALLIES(true),
    JOINS_REQUESTS(true),
    CHAT(true);

    private final boolean defaultValue;
}
