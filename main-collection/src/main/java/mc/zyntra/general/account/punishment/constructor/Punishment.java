package mc.zyntra.general.account.punishment.constructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class Punishment {

    private final String id = RandomStringUtils.randomAlphanumeric(5);
    private final PunishmentType type;
    private final PunishmentCategory category;

    private final String punishedBy;
    private UUID punishedByUUID;
    private final String reason;
    private final long expires;

    private boolean revoked = false;
    private String revokedBy;
    private UUID revokedByUUID;

    public Punishment(PunishmentType type, PunishmentCategory category, String punishedBy, UUID punishedByUUID, String reason, long expires) {
        this.type = type;
        this.category = category;
        this.punishedBy = punishedBy;
        this.punishedByUUID = punishedByUUID;
        this.reason = reason;
        this.expires = expires;
    }

    public void revoke(String revokedBy, UUID revokedByUUID) {
        this.revoked = true;
        this.revokedBy = revokedBy;
        this.revokedByUUID = revokedByUUID;
    }

    public void revoke(String revokedBy) {
        this.revoked = true;
        this.revokedBy = revokedBy;
    }

    public boolean isExpired() {
        return !(expires == -1L) && !(expires >= System.currentTimeMillis());
    }

    public boolean isActive() {
        return !isRevoked() && !isExpired();
    }
}
