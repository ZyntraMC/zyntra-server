package mc.zyntra.bukkit;

import lombok.Getter;
import mc.zyntra.bukkit.api.fake.FakeAPI;
import mc.zyntra.general.Platform;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class BukkitGeneral implements Platform {

    @Getter
    private static FakeAPI fakeAPI = new FakeAPI();

    @Override
    public UUID getUUID(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return player != null ? player.getUniqueId() : null;
    }

    @Override
    public <T> T getPlayerByName(String playerName, Class<T> clazz) {
        Player player = Bukkit.getPlayer(playerName);
        return player != null ? clazz.cast(player) : null;
    }

    @Override
    public <T> T getExactPlayerByName(String playerName, Class<T> clazz) {
        Player p = null;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(playerName)) {
                p = player;
                break;
            }
        }

        return p != null ? clazz.cast(p) : null;
    }

    @Override
    public <T> T getPlayerByUniqueId(UUID uniqueId, Class<T> clazz) {
        Player player = Bukkit.getPlayer(uniqueId);
        return player != null ? clazz.cast(player) : null;
    }

    @Override
    public boolean isOnline(UUID uniqueId) {
        Player player = getPlayerByUniqueId(uniqueId, Player.class);
        return player != null;
    }

    @Override
    public boolean isOnline(String name) {
        Player player = getPlayerByName(name, Player.class);
        return player != null;
    }

    @Override
    public void sendMessage(UUID uniqueId, String message) {
        Player player = getPlayerByUniqueId(uniqueId, Player.class);
        if (player != null)
            player.sendMessage(message);
    }

    @Override
    public void sendMessage(UUID uniqueId, BaseComponent message) {
        Player player = getPlayerByUniqueId(uniqueId, Player.class);
        if (player != null)
            player.spigot().sendMessage(message);
    }

    @Override
    public void sendMessage(UUID uniqueId, BaseComponent[] message) {
        Player player = getPlayerByUniqueId(uniqueId, Player.class);
        if (player != null)
            player.sendMessage(Arrays.toString(message));
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(BukkitMain.getInstance(), runnable);
    }
}