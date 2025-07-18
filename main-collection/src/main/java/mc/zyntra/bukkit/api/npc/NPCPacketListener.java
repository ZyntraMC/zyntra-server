package mc.zyntra.bukkit.api.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class NPCPacketListener extends PacketAdapter {

    private final NPCManager npcManager;

    public NPCPacketListener(Plugin plugin, NPCManager npcManager) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY);

        this.npcManager = npcManager;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        try {
            Player player = event.getPlayer();
            PacketContainer packet = event.getPacket();

            int entityId = packet.getIntegers().read(0);
            EntityUseAction action = packet.getEntityUseActions().read(0);

            if (action == EntityUseAction.ATTACK || action == EntityUseAction.INTERACT) {
                for (NPC npc : npcManager.getNPCs()) {
                    if (npc.isSpawned() && npc instanceof NPCHuman) {
                        if (npc.getEntityId() == entityId) {
                            Location loc = player.getLocation();

                            if (loc.distanceSquared(npc.getLocation()) < 36.0) {
                                NPC.onClickListener listener = npc.getOnClickListener();

                                if (listener != null) {
                                    listener.onClick(player);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

}
