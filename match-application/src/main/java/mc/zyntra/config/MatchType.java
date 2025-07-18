package mc.zyntra.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchType {

    ESCONDE_ESCONDE("Esconde Esconde"), DUELS("Duelos"), SKYWARS("Sky Wars"), BEDWARS("Bed Wars");

    String name;
}
