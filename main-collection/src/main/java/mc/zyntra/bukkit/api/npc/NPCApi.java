package mc.zyntra.bukkit.api.npc;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.general.Core;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;

import java.util.UUID;
import java.util.logging.Level;

public class NPCApi {

    public static NPCHuman spawnHuman(Location location, UUID skinUniqueId) {
        return spawnHuman(NPCHuman.class, location, skinUniqueId);
    }

    public static NPCHuman spawnHuman(Location location, Property textures) {
        return spawnHuman(NPCHuman.class, location, textures);
    }

    public static NPCHuman spawnHuman(Class<? extends NPCHuman> npcClass, Location location, UUID skinUniqueId) {
        Property textures = null;
        try {
            textures = Core.getMojangAPI().getTextures(skinUniqueId);
        } catch (Exception e) {
            Core.getLogger().log(Level.INFO, "Não foi possível buscar a skin de " + skinUniqueId.toString());
        }
        return spawnHuman(npcClass, location, textures);
    }

    public static NPCHuman spawnHuman(Class<? extends NPCHuman> npcClass, Location location, Property textures) {
        return BukkitMain.getInstance().getNpcManager().spawnHuman(npcClass, location, textures);
    }
}