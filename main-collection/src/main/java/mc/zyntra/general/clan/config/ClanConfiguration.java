package mc.zyntra.general.clan.config;

import java.util.HashMap;
import java.util.Map;

public class ClanConfiguration {

    private final Map<ConfigType, Boolean> configs = new HashMap<>();

    public void setEnabled(ConfigType configType, boolean enabled) {
        configs.put(configType, enabled);
    }

    public boolean isEnabled(ConfigType configType) {
        return configs.containsKey(configType) ? configs.get(configType) : configType.isDefaultValue();
    }
}

