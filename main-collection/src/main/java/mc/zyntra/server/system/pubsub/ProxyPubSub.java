package mc.zyntra.server.system.pubsub;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.networking.PacketOutUpdateAccountGroup;
import mc.zyntra.general.networking.Payload;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.group.GroupAttribute;
import mc.zyntra.general.utils.json.JsonUtils;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.JedisPubSub;

public class ProxyPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        Payload type;
        try {
            type = Payload.valueOf(channel);
        } catch (Exception e) {
            Core.getLogger().info("UNKNOWN PAYLOAD TYPE > " + channel);
            return;
        }

        switch (type) {
            case UPDATE_ACCOUNT_GROUP_ADD: {
                PacketOutUpdateAccountGroup packet = JsonUtils.jsonToObject(message, PacketOutUpdateAccountGroup.class);
                ZyntraPlayer zyntraPlayer = Core.getDataPlayer().load(packet.getUniqueId());

                ProxyServer.getInstance().getPlayers().stream()
                        .filter(proxiedPlayer ->
                                Core.getAccountController().get(proxiedPlayer.getUniqueId()).hasGroupPermission(Group.ADMIN)
                                        && Core.getAccountController().get(proxiedPlayer.getUniqueId()).getConfiguration().isEnabled(ConfigType.SEEING_LOGS))
                        .forEach(proxiedPlayer ->
                                proxiedPlayer.sendMessage("§7§o[" + packet.getPrimaryGroupData().getAddedBy() + " adicionou o rank " +
                                        packet.getPrimaryGroupData().getGroup().name() + " para " + zyntraPlayer.getName() +
                                        " com duração " + (packet.getPrimaryGroupData().getDuration() == -1L ? "vitalícia" :
                                        "de " + DateUtils.getTime(packet.getPrimaryGroupData().getDuration())) +
                                        (packet.getPrimaryGroupData().getAttribute().equals(GroupAttribute.PAYMENT)
                                                ? " - " + packet.getPrimaryGroupData().getAttribute().getName() : "") + "]")
                        );
                break;
            }

            case UPDATE_ACCOUNT_GROUP_REMOVE: {
                PacketOutUpdateAccountGroup packet = JsonUtils.jsonToObject(message, PacketOutUpdateAccountGroup.class);
                ZyntraPlayer zyntraPlayer = Core.getDataPlayer().load(packet.getUniqueId());

                ProxyServer.getInstance().getPlayers().stream()
                        .filter(proxiedPlayer ->
                                Core.getAccountController().get(proxiedPlayer.getUniqueId()).hasGroupPermission(Group.ADMIN)
                                        && Core.getAccountController().get(proxiedPlayer.getUniqueId()).getConfiguration().isEnabled(ConfigType.SEEING_LOGS))
                        .forEach(proxiedPlayer ->
                                proxiedPlayer.sendMessage("§7§o[" + packet.getPrimaryGroupData().getAddedBy() + " removeu o rank " +
                                        packet.getPrimaryGroupData().getGroup().name() + " da conta de " + zyntraPlayer.getName() + "]")
                        );
                break;
            }

            default: {
                break;
            }
        }
    }
}
