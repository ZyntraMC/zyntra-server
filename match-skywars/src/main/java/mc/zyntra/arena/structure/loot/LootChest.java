package mc.zyntra.arena.structure.loot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LootChest {
    NORMAL(Type.NORMAL),
    MINIFEAST(Type.MINIFEAST),
    FEAST(Type.FEAST);

    private final Type type;

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        NORMAL,
        MINIFEAST,
        FEAST
    }
}
