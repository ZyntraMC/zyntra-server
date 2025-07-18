package mc.zyntra.bukkit.api.npc;

import mc.zyntra.bukkit.BukkitMain;
import com.comphenix.protocol.ProtocolLibrary;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NPCManager {

    protected static double cosFOV = Math.cos(Math.toRadians(60));
    protected static double bukkitRange = NumberConversions.square(Bukkit.getViewDistance() << 4);
    private static Field entityCountField;

    static {
        try {
            entityCountField = Entity.class.getDeclaredField("entityCount");
            entityCountField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Map<Integer, NPC> npcs = new ConcurrentHashMap<>();
    @Getter
    private final Set<NPCHuman> globalNpc = new HashSet<>();

    public NPCManager() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new NPCPacketListener(BukkitMain.getInstance(), this));
    }

    private boolean inViewOf(Location l, Player player) {
        Vector dir = l.toVector().subtract(player.getEyeLocation().toVector()).normalize();
        return dir.dot(player.getEyeLocation().getDirection()) >= cosFOV;
    }

    public boolean canSpawn(Player player, Location loc) {
        return canSpawn(player.getLocation(), loc) && inViewOf(loc, player);
    }

    public boolean canSpawn(Location a, Location b) {
        return a.getWorld().equals(b.getWorld()) && a.distanceSquared(b) <= bukkitRange;
    }

    public NPC getNPC(int id) {
        return npcs.get(id);
    }

    public void add(NPC npc) {
        npcs.put(npc.getEntityId(), npc);
    }

    public void remove(NPC npc) {
        npcs.remove(npc.getEntityId());
    }

    public Collection<NPC> getNPCs() {
        return npcs.values();
    }

    public NPCHuman spawnHuman(Location location, Property textures) {
        return spawnHuman(NPCHuman.class, location, textures);
    }

    public NPCHuman spawnHuman(Class<? extends NPCHuman> clazz, Location location, Property textures) {
        NPCHuman npc = createHuman(clazz, location, textures);
        Bukkit.getPluginManager().registerEvents(npc, BukkitMain.getInstance());
        return npc;
    }

    public NPCHuman createHuman(Class<? extends NPCHuman> clazz, Location location, Property textures) {
        try {
            return clazz.getConstructor(NPCManager.class, Location.class, Property.class).newInstance(this, location, textures);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized int nextEntityId() {
        try {
            int currentId = entityCountField.getInt(null);
            entityCountField.set(null, currentId + 1);
            return currentId;
        } catch (Exception e) {
            return -1;
        }
    }
}