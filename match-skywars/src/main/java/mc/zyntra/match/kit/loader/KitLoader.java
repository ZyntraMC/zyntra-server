package mc.zyntra.match.kit.loader;

import mc.zyntra.general.utils.ClassGetter;
import mc.zyntra.Leading;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.kit.Kit;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;


@Getter
@Setter
public class KitLoader {
    @Getter
    private static final HashMap<String, Kit> abilities = new HashMap<>();

    public void load() {
        List<Class<?>> list =
                ClassGetter.getClassesForPackage(Leading.getInstance().getClass(), "mc.zyntra.match" +
                        ".kit.registry");

        list.forEach(clazz -> {
            if (clazz != Kit.class && Kit.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    if (constructor != null) {
                        Kit kit = (Kit) constructor.newInstance();
                        abilities.put(clazz.getSimpleName(), kit);
                        Leading.getInstance().getServer().getPluginManager().registerEvents(kit, Leading.getInstance());
                        Leading.getInstance().getLogger().info("[GAME-CORE] > Registered ability '" + clazz.getSimpleName() + "'.");
                    }
                } catch (Exception e) {
                    Leading.getInstance().getLogger().warning("[GAME-CORE] > Warning! Failed to register '" + clazz.getSimpleName() + "' abilitie.");
                    e.printStackTrace();
                }
            }
        });
    }

    public Kit getAbility(String name) {
        for (Kit kit : abilities.values()) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public boolean hasAbility(MatchPlayer matchPlayer) {
        for (Kit kit : abilities.values()) {
            if (matchPlayer.getKit().equalsIgnoreCase(kit.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUsingAbilityPerName(MatchPlayer matchPlayer, String name) {
        for (Kit kit : abilities.values()) {
            if ((matchPlayer.getKit().equalsIgnoreCase(name)) && kit.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
