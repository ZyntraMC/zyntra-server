package mc.zyntra.general.account.punishment.constructor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PunishmentCategory {

    CHEATING("Uso de Trapaças"),
    COMMUNITY("Violação das diretrizes da comunidade"),
    EVENT_RULES("Violação das diretrizes do evento");

    private final String name;
}
