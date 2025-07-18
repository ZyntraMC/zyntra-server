package mc.zyntra.bukkit.api.npc;

import mc.zyntra.bukkit.BukkitMain;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class NPCHuman extends NPC implements Listener {

    private final UUID uniqueId = UUID.randomUUID();
    private final GameProfile profile;
    private final String TAG = "§8[NPC] ";
    private final Set<Player> viewers = new HashSet<>();
    private boolean spawned;
    private int entityId, batEntityId;
    @Setter
    private ItemStack helmet, chestplate, leggings, boots, itemInHand;

    public NPCHuman(NPCManager npcManager, Location location) {
        this(npcManager, location, null);
    }

    public NPCHuman(NPCManager npcManager, Location location, Property textures) {
        super(npcManager, location);

        this.profile = new GameProfile(uniqueId, TAG + uniqueId.toString().substring(0, 8));

        if (textures != null) {
            profile.getProperties().put("textures", textures);
        }
    }

    private static PacketPlayOutNamedEntitySpawn buildSpawnHumanPacket(int entityId, Location loc, GameProfile profile) {
        DataWatcher dataWatcher = new DataWatcher(null);
        dataWatcher.a(10, (byte) 127); // (0b01111111) ativando todas as partes da skin para 1.8+
        dataWatcher.a(6, (float) 20);

        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setFieldValue(packet, "a", entityId);//id
        setFieldValue(packet, "b", profile.getId());//uuid
        setFieldValue(packet, "c", floor(loc.getX() * 32));//x
        setFieldValue(packet, "d", floor(loc.getY() * 32));//y
        setFieldValue(packet, "e", floor(loc.getZ() * 32));//z
        setFieldValue(packet, "f", getCompressedAngle(loc.getYaw()));//yaw
        setFieldValue(packet, "g", getCompressedAngle(loc.getPitch()));//pitch
        setFieldValue(packet, "h", 0); // item hand
        setFieldValue(packet, "i", dataWatcher);

        return packet;
    }

    private static PacketPlayOutEntityHeadRotation buildEntityHeadRotationPacket(int entityId, float yaw) {
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();
        setFieldValue(packet, "a", entityId);
        setFieldValue(packet, "b", getCompressedAngle(yaw));
        return packet;
    }

    private static PacketPlayOutPlayerInfo buildPlayerInfoPacket(EnumPlayerInfoAction action, GameProfile profile) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        setFieldValue(packet, "a", action);

        List<PlayerInfoData> dataList = getFieldValue(packet, "b");
        IChatBaseComponent nameComponent = ChatSerializer.a("{\"text\":\"" + profile.getName() + "\"}");
        dataList.add(packet.new PlayerInfoData(profile, 1, EnumGamemode.NOT_SET, nameComponent));

        return packet;
    }

    private static PacketPlayOutSpawnEntityLiving buildSpawnBatPacket(int entityId, Location loc) {
        DataWatcher watcher = new DataWatcher(null);
        watcher.a(0, (byte) (1 << 5));

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
        setFieldValue(packet, "a", entityId);
        setFieldValue(packet, "b", 65);
        setFieldValue(packet, "c", floor(loc.getX() * 32D));
        setFieldValue(packet, "d", floor(loc.getY() * 32D));
        setFieldValue(packet, "e", floor(loc.getZ() * 32D));
        setFieldValue(packet, "l", watcher);

        return packet;
    }

    private static PacketPlayOutAttachEntity buildAttachPacket(int a, int b) {
        PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
        setFieldValue(packet, "a", 0);
        setFieldValue(packet, "b", a);
        setFieldValue(packet, "c", b);
        return packet;
    }

    private static PacketPlayOutEntityDestroy buildDestroyPacket(int... entityIds) {
        return new PacketPlayOutEntityDestroy(entityIds);
    }

    private static PacketPlayOutEntityEquipment buildEquipmentPacket(int entityId, int slot, ItemStack item) {
        if (item == null) {
            item = new ItemStack(Material.AIR);
        }
        return new PacketPlayOutEntityEquipment(entityId, slot, CraftItemStack.asNMSCopy(item));
    }

    private static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < (double) var2 ? var2 - 1 : var2;
    }

    protected static byte getCompressedAngle(float value) {
        return (byte) ((int) value * 256.0F / 360.0F);
    }

    public static void setFieldValue(Object instance, String fieldName, Object value) {
        try {
            Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    public static <T> T getFieldValue(Object instance, String fieldName) {
        try {
            Field f = instance.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return (T) f.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getEyeHeight() {
        return 1.62D;
    }

    public boolean isShown(Player player) {
        return viewers.contains(player);
    }

    public void spawn() {
        if (!spawned) {
            spawned = true;

            entityId = npcManager.nextEntityId();
            batEntityId = npcManager.nextEntityId();

            npcManager.add(this);

            getPlayers().stream()
                    .filter(p -> npcManager.canSpawn(p, location))
                    .forEach(this::spawnTo);
        }
    }

    public void despawn() {
        if (spawned) {
            spawned = false;
            npcManager.remove(this);
            getPlayers().forEach(this::despawnTo);
        }
    }

    public void respawn() {
        if (spawned) {
            viewers.forEach(v -> {
                sendDespawn(v);
                sendSpawn(v);
            });
        }
    }

    public void updateEquipment() {
        viewers.forEach(this::updateEquipment);
    }

    private void updateEquipment(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(buildEquipmentPacket(entityId, 0, itemInHand));
        connection.sendPacket(buildEquipmentPacket(entityId, 1, boots));
        connection.sendPacket(buildEquipmentPacket(entityId, 2, leggings));
        connection.sendPacket(buildEquipmentPacket(entityId, 3, chestplate));
        connection.sendPacket(buildEquipmentPacket(entityId, 4, helmet));
    }

    private void sendSpawn(Player player) {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
        conn.sendPacket(buildPlayerInfoPacket(EnumPlayerInfoAction.ADD_PLAYER, profile));
        conn.sendPacket(buildSpawnHumanPacket(entityId, location, profile));
        conn.sendPacket(buildEntityHeadRotationPacket(entityId, location.getYaw()));
        conn.sendPacket(buildSpawnBatPacket(batEntityId, location));
        conn.sendPacket(buildAttachPacket(batEntityId, entityId));

        this.updateEquipment(player);

        Bukkit.getScheduler().runTaskLater(BukkitMain.getInstance(), () -> conn.sendPacket(buildPlayerInfoPacket(EnumPlayerInfoAction.REMOVE_PLAYER, profile)), 40);
    }

    private void sendDespawn(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(buildPlayerInfoPacket(EnumPlayerInfoAction.REMOVE_PLAYER, profile));
        connection.sendPacket(buildDestroyPacket(entityId, batEntityId));
    }

    public void spawnTo(Player player) {
        if (!viewers.contains(player)) {
            viewers.add(player);
            sendSpawn(player);
        }
    }

    public void despawnTo(Player player) {
        if (viewers.contains(player)) {
            viewers.remove(player);
            sendDespawn(player);
        }
    }
}