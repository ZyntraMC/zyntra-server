package mc.zyntra.general;

import net.md_5.bungee.api.chat.BaseComponent;

import java.util.UUID;

public interface Platform {

    UUID getUUID(String playerName);

    <T> T getPlayerByName(String playerName, Class<T> clazz);

    <T> T getExactPlayerByName(String playerName, Class<T> clazz);

    <T> T getPlayerByUniqueId(UUID uniqueId, Class<T> clazz);

    boolean isOnline(UUID uniqueId);

    boolean isOnline(String name);

    void sendMessage(UUID uniqueId, String message);

    void sendMessage(UUID uniqueId, BaseComponent message);

    void sendMessage(UUID uniqueId, BaseComponent[] message);

    void runAsync(Runnable runnable);

}