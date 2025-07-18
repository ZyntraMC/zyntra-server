package mc.zyntra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mc.zyntra.collectibles.parser.loader.PlayerLoader;


public class cMain {

    @Getter
    private static cMain instance;

    @Getter
    private static PlayerLoader playerLoader = new PlayerLoader();

    @Getter
    private Collectible types;

    @Getter
    @NoArgsConstructor
    public enum Collectible {
        MAIN, HATS, VICTORY_ANIM, GADGETS, TITLES, PETS, WINGS, OUTFITS, PARTICLES;
    }
}
