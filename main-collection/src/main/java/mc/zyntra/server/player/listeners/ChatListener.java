package mc.zyntra.server.player.listeners;

import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.punishment.constructor.Punishment;
import mc.zyntra.general.account.punishment.constructor.PunishmentType;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer) || event.isCommand())
            return;

        ProxiedPlayer player = ((ProxiedPlayer) event.getSender());
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (!zyntraPlayer.isLogged()) {
            player.sendMessage("§cVocê não está logado.");
            event.setCancelled(true);
            return;
        }

        if (zyntraPlayer.hasGroupPermission(Group.HELPER) && zyntraPlayer.getConfiguration().isEnabled(ConfigType.STAFFCHAT)) {
            if (!zyntraPlayer.getConfiguration().isEnabled(ConfigType.SEEING_STAFFCHAT)) {
                player.sendMessage("§cVocê não pode conversar no bate-papo da equipe pois você desabilitou a visibilidade dele.");
                event.setCancelled(true);
                return;
            }

            Tag tag = zyntraPlayer.getPrimaryGroupData().getGroup().getTag();
            ProxyServer.getInstance().getPlayers().stream().filter(proxiedPlayer ->
                            Core.getAccountController().get(proxiedPlayer.getUniqueId()).hasGroupPermission(Group.HELPER) &&
                                    Core.getAccountController().get(proxiedPlayer.getUniqueId()).getConfiguration().isEnabled(ConfigType.SEEING_STAFFCHAT))
                    .forEach(proxiedPlayer ->
                            proxiedPlayer.sendMessage("§d[EQUIPE]§f " + tag.getColor() + "[" + tag.getName() + "] " +
                                    tag.getColor() + zyntraPlayer.getName() + ": §f" + event.getMessage()));

            event.setCancelled(true);
            return;
        }

        Punishment punishment = zyntraPlayer.getPunishmentHistoric().getActivePunishment(PunishmentType.MUTE);

        if (punishment != null) {
            player.sendMessage("\n§cVocê está silenciado por " + punishment.getCategory().getName() + (punishment.getExpires() == -1L
                    ? "" : " com duração de " + DateUtils.getTime(punishment.getExpires())) + ".\n");

            event.setCancelled(true);
        }
    }
}
