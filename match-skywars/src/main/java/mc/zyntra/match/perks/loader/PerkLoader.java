package mc.zyntra.match.perks.loader;

import lombok.Getter;
import lombok.Setter;
import mc.zyntra.Leading;
import mc.zyntra.general.utils.ClassGetter;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.perks.Perks;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;


@Getter
@Setter
public class PerkLoader {
    @Getter
    private static final HashMap<String, Perks> abilities = new HashMap<>();

    public void load() {
        List<Class<?>> list =
                ClassGetter.getClassesForPackage(Leading.getInstance().getClass(), "mc.zyntra.match" +
                        ".perks.registry");

        list.forEach(clazz -> {
            if (clazz != Perks.class && Perks.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    if (constructor != null) {
                        Perks perks = (Perks) constructor.newInstance();
                        abilities.put(clazz.getSimpleName(), perks);
                        Leading.getInstance().getServer().getPluginManager().registerEvents(perks, Leading.getInstance());
                        Leading.getInstance().getLogger().info("[GAME-CORE] > Registered ability '" + clazz.getSimpleName() + "'.");
                    }
                } catch (Exception e) {
                    Leading.getInstance().getLogger().warning("[GAME-CORE] > Warning! Failed to register '" + clazz.getSimpleName() + "' abilitie.");
                    e.printStackTrace();
                }
            }
        });
    }

    public Perks getAbility(String name) {
        for (Perks perks : abilities.values()) {
            if (perks.getName().equalsIgnoreCase(name)) {
                return perks;
            }
        }
        return null;
    }

    public boolean hasAbility(MatchPlayer matchPlayer) {
        for (Perks perks : abilities.values()) {
            if (matchPlayer.getKit().equalsIgnoreCase(perks.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUsingAbilityPerName(MatchPlayer matchPlayer, String name) {
        for (Perks perks : abilities.values()) {
            if ((matchPlayer.getKit().equalsIgnoreCase(name)) && perks.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
