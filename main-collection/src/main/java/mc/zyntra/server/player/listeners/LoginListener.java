package mc.zyntra.server.player.listeners;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.punishment.constructor.Punishment;
import mc.zyntra.general.account.punishment.constructor.PunishmentType;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.utils.mojang.UUIDFetcher;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        event.getConnection().setOnlineMode(UUIDFetcher.getUUID(event.getConnection().getName()) != null);

        ServerConfiguration serverConfiguration = Core.getDataServer().getServers(event.getConnection().isOnlineMode() ?
                        ServerType.LOBBY : ServerType.AUTH)
                .stream()
                .findFirst()
                .orElse(null);

        if (serverConfiguration == null || ProxyServer.getInstance().getServerInfo(serverConfiguration.getName()) == null) {
            event.setCancelReason(Constant.NO_SERVER_FOUND);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = 32)
    public void onLogin(LoginEvent event) {
        PendingConnection connection = event.getConnection();

        ZyntraPlayer zyntraPlayer = Core.getDataPlayer().load(connection.getUniqueId());

        if (zyntraPlayer == null) {
            zyntraPlayer = Core.getDataPlayer().create(connection.getUniqueId(), connection.getName());
        } else if (connection.isOnlineMode() && !zyntraPlayer.getName().equals(connection.getName())) {
            zyntraPlayer.setName(connection.getName());
            zyntraPlayer.update("name");
        }

        if (zyntraPlayer == null) {
            event.setCancelled(true);
            event.setCancelReason(Constant.ACCOUNT_LOAD_FAILED);
            return;
        }

        if (connection.getName().length() > 15) {
            event.setCancelled(true);
            event.setCancelReason(Constant.NAME_TOO_LONG);
            return;
        }

        if (!Core.getWhitelistController().canJoin(zyntraPlayer)) {
            event.setCancelled(true);
            event.setCancelReason("§cOs servidores estão disponíveis somente para nossa equipe.");
            return;
        }

        // Check if the player is blacklisted

        Punishment punishment = zyntraPlayer.getPunishmentHistoric().getActivePunishment(PunishmentType.BLACKLIST);

        if (punishment != null) {
            event.setCancelled(true);
            event.setCancelReason(String.format(Constant.YOU_ARE_BLACKLISTED, punishment.getCategory().getName(), punishment.getId()));
            return;
        }

        // Check if the player is banned

        punishment = zyntraPlayer.getPunishmentHistoric().getActivePunishment(PunishmentType.BAN);

        if (punishment != null) {
            event.setCancelled(true);
            event.setCancelReason(String.format(Constant.YOU_ARE_BANNED, punishment.getExpires() == -1L
                    ? "permanentemente" : "temporariamente", punishment.getCategory().getName(),
                    punishment.getExpires() == -1L ? "Nunca" : DateUtils.getTime(punishment.getExpires()),
                    punishment.getId()));
            return;
        }

        zyntraPlayer.setAccountType(event.getConnection().isOnlineMode() ? ZyntraPlayer.AccountType.PREMIUM : ZyntraPlayer.AccountType.CRACKED);
        zyntraPlayer.setLogged(event.getConnection().isOnlineMode());
        zyntraPlayer.setOnline(true);
        zyntraPlayer.setLastLogin(System.currentTimeMillis());

        zyntraPlayer.update("accountType");
        zyntraPlayer.update("logged");
        zyntraPlayer.update("online");
        zyntraPlayer.update("lastLogin");

        Core.getAccountController().create(zyntraPlayer);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(event.getPlayer().getUniqueId());

        if (zyntraPlayer.hasDisguise())
            zyntraPlayer.setDisguise(zyntraPlayer.getName());

        zyntraPlayer.setOnline(false);
        zyntraPlayer.setLogged(false);

        zyntraPlayer.update("online");
        zyntraPlayer.update("logged");

        Core.getAccountController().remove(event.getPlayer().getUniqueId());
    }
}
