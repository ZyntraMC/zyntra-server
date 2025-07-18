package mc.zyntra.bukkit.api.tablist;

import lombok.NonNull;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Tablist {

    @NonNull
    private String[] header;

    @NonNull
    private String[] footer;

    @NonNull
    public String[] getHeader() {
        return this.header;
    }

    public Tablist setHeader(String... header) {
        this.header = header;
        return this;
    }

    @NonNull
    public String[] getFooter() {
        return this.footer;
    }

    public Tablist setFooter(String... footer) {
        this.footer = footer;
        return this;
    }

    public void send(Player player) {
        StringBuilder headerBuilder = new StringBuilder();
        for (String value : this.header)
            headerBuilder.append(value);
        StringBuilder footerBuilder = new StringBuilder();
        for (String s : this.footer)
            footerBuilder.append(s);
        CraftPlayer craftplayer = (CraftPlayer) player;
        PlayerConnection connection = (craftplayer.getHandle()).playerConnection;
        IChatBaseComponent headerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + headerBuilder + "\"}");
        IChatBaseComponent footerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footerBuilder + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, headerJSON);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footerJSON);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        connection.sendPacket((Packet) packet);
    }
}
