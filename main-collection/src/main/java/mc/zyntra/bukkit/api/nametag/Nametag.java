package mc.zyntra.bukkit.api.nametag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Nametag {

    private final String prefix, suffix, team;

}
