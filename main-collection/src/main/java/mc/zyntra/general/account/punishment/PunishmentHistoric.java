package mc.zyntra.general.account.punishment;

import mc.zyntra.general.account.punishment.constructor.Punishment;
import mc.zyntra.general.account.punishment.constructor.PunishmentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class PunishmentHistoric {

    private final List<Punishment> punishments = new ArrayList<>();

    public void punish(Punishment punishment) {
        punishments.add(punishment);
    }

    public void revoke(String id, String revokedBy) {
        punishments.stream().filter(p -> p.getId().equals(id) && p.isActive()).forEach(punishment -> punishment.revoke(revokedBy));
    }

    public void revoke(String id, String revokedBy, UUID revokedByUUID) {
        punishments.stream().filter(p -> p.getId().equals(id) && p.isActive()).forEach(punishment -> punishment.revoke(revokedBy, revokedByUUID));
    }

    public Punishment getActivePunishment(PunishmentType type) {
        return punishments.stream().filter(punishment -> punishment.getType().equals(type) && punishment.isActive())
                .findFirst().orElse(null);
    }

    public Punishment getPunishment(String id) {
        return punishments.stream().filter(punishment -> punishment.getId().equals(id))
                .findFirst().orElse(null);
    }
}
