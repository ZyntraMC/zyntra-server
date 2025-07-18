package mc.zyntra.general.account.skin;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Skin {

    private final UUID uniqueId;
    private final String displayName, name;

    public Skin(UUID uniqueId, String displayName, String name) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.name = name;
    }
}