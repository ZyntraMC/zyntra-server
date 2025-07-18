package mc.zyntra.bukkit.api.fake;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mc.zyntra.bukkit.BukkitGeneral;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.fake.Fake;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.utils.mojang.PremiumUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class FakeAPI {

    private List<String> randomFakes = new ArrayList<>();
    private HashMap<String, Player> fakes = new HashMap<>();

    public void loadFakes() {
        File file = new File(BukkitMain.getInstance().getDataFolder(), "fakes.yml");
        YamlConfiguration yaml;

        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
                InputStream isResource = BukkitMain.getInstance().getResource(file.getName());
                yaml = YamlConfiguration.loadConfiguration(isResource);
                yaml.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        yaml = YamlConfiguration.loadConfiguration(file);

        for (String string : yaml.getStringList("fakes")) {
            randomFakes.add(string);
        }

        Core.getLogger().info("Foram carregados " + randomFakes.size() + " fakes.");
    }

    public boolean isOriginal(String nick) {
        boolean uuid = PremiumUtils.isPremium(nick);
        return uuid;
    }

    public boolean already(String name) {
        return fakes.get(name) != null;
    }

    public Fake randomFake(Tag rank) {
        for (int i = 0; i < randomFakes.size(); i++) {
            if (randomFakes.get(i).length() >= 14) {
                continue;
            }
            if (isOriginal(randomFakes.get(i))) {
                continue;
            }

            String name = randomFakes.get(new Random().nextInt(randomFakes.size()));
            return new Fake(name, rank);
        }
        return null;
    }

    public void removeFromTab(Player player, Collection<? extends Player> players) {
        PacketPlayOutPlayerInfo removePlayerInfo =
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle());

        for (Player online : players) {
            if (online.canSee(player)) {
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(removePlayerInfo);
            }
        }
    }

    public void openBook(Player player, TextComponent... components) {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) stack.getItemMeta();
        meta.setAuthor(player.getName());
        meta.setTitle("Selecione o Rank:");
        List<IChatBaseComponent> pages = null;
        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(meta);
        } catch (Exception e) {
            Core.getLogger().info("Ocorreu um erro ao abrir o livro. (FAKE)");
        }
        IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(components));
        pages.add(page);
        stack.setItemMeta(meta);

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, stack);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte) 0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        player.getInventory().setItem(slot, old);
    }
    public void changeFake(Player player, Fake fake) {
        Collection<? extends Player> players = player.getWorld().getPlayers();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        GameProfile gameProfile = entityPlayer.getProfile();

        fakes.put(fake.getName(), player);
        ZyntraPlayer systemPlayer = Core.getAccountController().get(player.getUniqueId());
        if (systemPlayer.getName() == fake.getName()) {
            randomFakes.add(systemPlayer.getFake().getName());
            fakes.remove(systemPlayer.getName());
        } else {
            randomFakes.remove(fake.getName());
        }

        removeFromTab(player, players);

        try {
            Field field = gameProfile.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(gameProfile, fake.getName());
            field.setAccessible(false);
            entityPlayer.getClass().getDeclaredField("displayName").set(entityPlayer, fake.getName());
        } catch (Exception e) {
            Core.getLogger().severe("Ocorreu um erro ao tentar alterar o fake do jogador " + player.getName());
        }


        try {
            if (!player.isOnline()) {
                return;
            }

            int entityId = entityPlayer.getId();
            Location l = player.getLocation();

            PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle());

            PacketPlayOutPlayerInfo addPlayer = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle());

            PacketPlayOutEntityDestroy removeEntity =
                    new PacketPlayOutEntityDestroy(entityId);

            PacketPlayOutNamedEntitySpawn addNamed =
                    new PacketPlayOutNamedEntitySpawn(entityPlayer);

            PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(((WorldServer) entityPlayer.getWorld()).dimension,
                    entityPlayer.world.getDifficulty(), entityPlayer.getWorld().worldData.getType(), WorldSettings.EnumGamemode.getById(player.getGameMode().getValue()));

            PacketPlayOutPosition pos =
                    new PacketPlayOutPosition(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), new HashSet<>());

            PacketPlayOutEntityEquipment itemhand =
                    new PacketPlayOutEntityEquipment(entityId, 0, CraftItemStack.asNMSCopy(player.getItemInHand()));

            PacketPlayOutEntityEquipment helmet =
                    new PacketPlayOutEntityEquipment(entityId, 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));

            PacketPlayOutEntityEquipment chestplate =
                    new PacketPlayOutEntityEquipment(entityId, 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));

            PacketPlayOutEntityEquipment leggings =
                    new PacketPlayOutEntityEquipment(entityId, 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));

            PacketPlayOutEntityEquipment boots =
                    new PacketPlayOutEntityEquipment(entityId, 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));

            PacketPlayOutHeldItemSlot slot =
                    new PacketPlayOutHeldItemSlot(player.getInventory().getHeldItemSlot());

            List<Player> toUpdate = new ArrayList<>();

            Bukkit.getOnlinePlayers().forEach(on -> {
                CraftPlayer craftPlayerOn = ((CraftPlayer) on);
                PlayerConnection connection = craftPlayerOn.getHandle().playerConnection;

                if (on == player) {
                    connection.sendPacket(playerInfo);
                    connection.sendPacket(addPlayer);
                    connection.sendPacket(respawn);
                    connection.sendPacket(pos);
                    connection.sendPacket(slot);

                    craftPlayerOn.updateScaledHealth();
                    craftPlayerOn.getHandle().triggerHealthUpdate();
                    craftPlayerOn.updateInventory();
                } else if ((on.canSee(player)) && (on.getWorld().equals(player.getWorld()))) {
                    connection.sendPacket(removeEntity);
                    connection.sendPacket(playerInfo);
                    connection.sendPacket(addPlayer);
                    connection.sendPacket(addNamed);
                    connection.sendPacket(itemhand);
                    connection.sendPacket(helmet);
                    connection.sendPacket(chestplate);
                    connection.sendPacket(leggings);
                    connection.sendPacket(boots);

                    toUpdate.add(on);
                } else {
                    connection.sendPacket(addPlayer);
                }
            });

            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

            if (!zyntraPlayer.getFake().getName().equalsIgnoreCase(zyntraPlayer.getName())) {
                NametagController.getInstance().setNametag(zyntraPlayer.toPlayer().getPlayer(), zyntraPlayer.getFake().getTag());
            } else {
                NametagController.getInstance().setNametag(zyntraPlayer.toPlayer(), zyntraPlayer.getTagHandler().getSelectedTag());
            }

            toUpdate.forEach(on -> {
                on.hidePlayer(player);
                BukkitMain.getInstance().getServer().getScheduler().runTaskLater(BukkitMain.getInstance(), () -> {
                    on.showPlayer(player);
                }, 5);
            });
        } catch (Exception e) {
            PacketPlayOutPlayerInfo addPlayer = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)player).getHandle());
            players.forEach(play -> {
                if (play.canSee(player)) {
                    ((CraftPlayer)play).getHandle().playerConnection.sendPacket(addPlayer);
                }
            });
            Core.getLogger().severe("Ocorreu um erro ao tentar alterar o fake do jogador " + player.getName());
        }
    }
}
