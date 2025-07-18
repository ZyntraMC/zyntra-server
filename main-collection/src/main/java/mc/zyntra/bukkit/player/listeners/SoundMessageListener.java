package mc.zyntra.bukkit.player.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class SoundMessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("zyntra:sound")) return;

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String soundName = in.readUTF();
            float volume = in.readFloat();
            float pitch = in.readFloat();

            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
