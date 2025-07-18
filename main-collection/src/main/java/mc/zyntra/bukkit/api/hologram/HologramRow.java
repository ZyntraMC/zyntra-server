package mc.zyntra.bukkit.api.hologram;

import lombok.Getter;
import lombok.NonNull;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Getter
public class HologramRow {

    private Location location;

    private final EntityArmorStand armorStand;

    public HologramRow(Location location, String text) {
        this.location = location;
        this.armorStand = createArmorStand(location, text);
    }

    public void teleport(@NonNull Location loc) {
        this.location = loc;
        this.armorStand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public int getId() {
        return armorStand.getId();
    }

    public String getText() {
        return armorStand.getCustomName();
    }

    public void setText(String text) {
        armorStand.setCustomName(text);
    }

    public void spawn(Player player) {
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void despawn(Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void update(Player player) {
        PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void respawn(Player player) {
        despawn(player);
        spawn(player);
    }

    private EntityArmorStand createArmorStand(Location loc, String name) {
        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle());
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        stand.setCustomName(name);
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setInvisible(true);
        stand.n(false); // setMarker
        return stand;
    }
}
