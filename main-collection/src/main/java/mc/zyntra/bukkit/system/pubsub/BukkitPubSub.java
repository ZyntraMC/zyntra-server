package mc.zyntra.bukkit.system.pubsub;

import mc.zyntra.bukkit.event.account.PlayerGroupAddEvent;
import mc.zyntra.bukkit.event.account.PlayerGroupRemoveEvent;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.GroupData;
import mc.zyntra.bukkit.event.redis.RedisPubSubMessageEvent;
import mc.zyntra.bukkit.event.server.ServerStartEvent;
import mc.zyntra.bukkit.event.server.ServerStopEvent;
import mc.zyntra.bukkit.event.server.ServerUpdateEvent;
import mc.zyntra.general.networking.PacketOutUpdateAccountField;
import mc.zyntra.general.networking.PacketOutUpdateAccountGroup;
import mc.zyntra.general.networking.PacketServerInfo;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.utils.json.JsonUtils;
import mc.zyntra.general.utils.reflection.ReflectionUtils;
import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

public class BukkitPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        Payload type;
        try {
            type = Payload.valueOf(channel);
        } catch (Exception e) {
            Core.getLogger().info("UNKNOWN PAYLOAD TYPE > " + channel);
            return;
        }

        RedisPubSubMessageEvent event = new RedisPubSubMessageEvent(channel, message);
        Bukkit.getPluginManager().callEvent(event);

        switch (type) {
            case SERVER_INFO: {
                PacketServerInfo packet = JsonUtils.jsonToObject(message, PacketServerInfo.class);
                ServerConfiguration serverConfiguration = packet.getServerConfiguration(ServerConfiguration.class);
                PacketServerInfo.Action action = packet.getAction();

                switch (action) {
                    case START: {
                        Bukkit.getPluginManager().callEvent(new ServerStartEvent(serverConfiguration.getName(), serverConfiguration));
                        break;
                    }
                    case STOP: {
                        Bukkit.getPluginManager().callEvent(new ServerStopEvent(packet.getSource()));
                        break;
                    }
                    case UPDATE: {
                        Bukkit.getPluginManager().callEvent(new ServerUpdateEvent(packet.getSource(), serverConfiguration));
                        break;
                    }
                }
                break;
            }
            case UPDATE_ACCOUNT_FIELD: {
                PacketOutUpdateAccountField packet = JsonUtils.jsonToObject(message, PacketOutUpdateAccountField.class);
                if (packet.getSource().equals(Core.getServerName()))
                    return;

                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(packet.getUniqueId());
                if (zyntraPlayer != null) {
                    Object value = ReflectionUtils.getFieldValue(packet.getZyntraPlayer(), packet.getFieldName());
                    ReflectionUtils.setFieldValue(zyntraPlayer, packet.getFieldName(), value);
                }
                break;
            }
            case UPDATE_ACCOUNT_GROUP_ADD: {
                PacketOutUpdateAccountGroup packet = JsonUtils.jsonToObject(message, PacketOutUpdateAccountGroup.class);
                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(packet.getUniqueId());
                GroupData groupData = packet.getPrimaryGroupData();
                if (zyntraPlayer != null && groupData != null)
                    Bukkit.getPluginManager().callEvent(new PlayerGroupAddEvent(zyntraPlayer.toPlayer(), groupData, groupData.getDuration()));
                break;
            }
            case UPDATE_ACCOUNT_GROUP_REMOVE: {
                PacketOutUpdateAccountGroup packet = JsonUtils.jsonToObject(message, PacketOutUpdateAccountGroup.class);
                ZyntraPlayer zyntraPlayer = Core.getAccountController().get(packet.getUniqueId());
                GroupData groupData = packet.getPrimaryGroupData();
                if (zyntraPlayer != null && groupData != null)
                    Bukkit.getPluginManager().callEvent(new PlayerGroupRemoveEvent(zyntraPlayer.toPlayer(), groupData));
                break;
            }
            default:
                break;
        }
    }
}
