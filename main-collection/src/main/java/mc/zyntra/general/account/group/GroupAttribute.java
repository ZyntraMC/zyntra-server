package mc.zyntra.general.account.group;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupAttribute {

    AUTO("Atribuído automaticamente"),
    SYSTEM("Atribuído pelo sistema"),
    STAFFER("Atribuído por um staffer"),
    PAYMENT("Atribuído por um pagamento");

    private final String name;

}
