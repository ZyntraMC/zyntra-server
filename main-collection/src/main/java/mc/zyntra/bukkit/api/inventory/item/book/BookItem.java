package mc.zyntra.bukkit.api.inventory.item.book;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class BookItem extends ItemStack {

    private final BookMeta meta = (BookMeta) getItemMeta();

    public BookItem() {
        super(Material.WRITTEN_BOOK);
    }

    public BookItem title(String title) {
        meta.setTitle(title);
        setItemMeta(meta);

        return this;
    }

    public BookItem author(String author) {
        meta.setAuthor(author);
        setItemMeta(meta);

        return this;
    }

    public BookItem page(int index, String page) {
        meta.setPage(index, page);
        setItemMeta(meta);

        return this;
    }

    public BookItem pages(String... pages) {
        meta.addPage(pages);
        setItemMeta(meta);

        return this;
    }

    public BookItem pageComponents(TextComponent... components) {
        try {
            List<IChatBaseComponent> pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(meta);
            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(components));
            pages.add(page);

            setItemMeta(meta);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return this;
    }

    public void open(Player player) {
        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, this);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte) 0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        player.getInventory().setItem(slot, old);
    }
}
