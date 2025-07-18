package mc.zyntra.collectibles.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mc.zyntra.collectibles.parser.hats.Hats;
import mc.zyntra.collectibles.parser.particles.Particles;

import java.util.UUID;


@Getter
@Setter
@RequiredArgsConstructor
public class CollectiblePlayer {
    private final UUID uniqueId;
    private boolean particles = false, wings = false;
    private Hats hat;
    private Particles particle;
    private double alpha = 0;
}
