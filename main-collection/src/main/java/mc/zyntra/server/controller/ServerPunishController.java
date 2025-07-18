package mc.zyntra.server.controller;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.punishment.constructor.Punishment;
import mc.zyntra.general.controller.PunishController;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerPunishController implements PunishController {

    @Override
    public void punish(ZyntraPlayer zyntraPlayer, Punishment punishment) {
        zyntraPlayer.getPunishmentHistoric().punish(punishment);
        zyntraPlayer.update("punishmentHistoric");

        switch (punishment.getType()) {
            case BAN: {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(zyntraPlayer.getUniqueId());
                if (player != null) {
                    player.disconnect(String.format(Constant.YOU_ARE_BANNED, punishment.getExpires() == -1L
                            ? "permanentemente" : "temporariamente", punishment.getCategory().getName(),
                            punishment.getExpires() == -1L
                            ? "Nunca"
                            : DateUtils.getTime(punishment.getExpires()),
                            punishment.getId()));
                }

                Core.getAccountController().notify(zyntraPlayer.getName() + " foi banido na categoria " + punishment.getCategory().name().toLowerCase()
                                + " por " + punishment.getPunishedBy() + " pelo motivo " + punishment.getReason());
                break;
            }
            case BLACKLIST: {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(zyntraPlayer.getUniqueId());
                if (player != null) {
                    player.disconnect(String.format(Constant.YOU_ARE_BLACKLISTED, punishment.getCategory().getName(), punishment.getId()));
                }

                Core.getAccountController().notify(zyntraPlayer.getName() + " foi blacklisted por " + punishment.getPunishedBy()
                                + " pelo motivo " + punishment.getReason());
                break;
            }
            case MUTE: {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(zyntraPlayer.getUniqueId());
                if (player != null) {
                    player.sendMessage("§cVocê foi silenciado por " + punishment.getCategory().getName() + (punishment.getExpires() == -1L
                            ? "" : " com duração de " + DateUtils.getTime(punishment.getExpires())) + ".");
                }

                Core.getAccountController().notify(zyntraPlayer.getName() + " foi silenciado na categoria " + punishment.getCategory().name().toLowerCase()
                                + " por " + punishment.getPunishedBy() + " pelo motivo " + punishment.getReason());
                break;
            }
            case BLOCK_REPORT: {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(zyntraPlayer.getUniqueId());
                if (player != null) {
                    player.sendMessage("§cVocê foi bloqueado de denunciar jogadores por " + punishment.getCategory().getName() + (punishment.getExpires() == -1L
                            ? "" : " com duração de " + DateUtils.getTime(punishment.getExpires())) + ".");
                }

                Core.getAccountController().notify(zyntraPlayer.getName() + " foi impossibilitado de denunciar jogadores na categoria "
                                + punishment.getCategory().name().toLowerCase() + " por " + punishment.getPunishedBy()
                                + " pelo motivo " + punishment.getReason());
                break;
            }
            case BLACKLIST_EVENT: {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(zyntraPlayer.getUniqueId());
                if (player != null) {
                    player.sendMessage("§cVocê foi banido de torneios competitivos por " + punishment.getCategory().getName() + (punishment.getExpires() == -1L
                            ? "" : " com duração de " + DateUtils.getTime(punishment.getExpires())) + ".");
                }

                Core.getAccountController().notify(zyntraPlayer.getName() + " foi banido de torneios competitivos na categoria "
                                + punishment.getCategory().name().toLowerCase() + " por " + punishment.getPunishedBy()
                                + " pelo motivo " + punishment.getReason());
                break;
            }
            default:
                break;
        }
    }

    public void revoke(ZyntraPlayer zyntraPlayer, String id, String revokedBy) {
        zyntraPlayer.getPunishmentHistoric().revoke(id, revokedBy);
        zyntraPlayer.update("punishmentHistoric");

        Core.getAccountController().notify(revokedBy + " revogou a punição #" + id + " de " + zyntraPlayer.getName());
    }
}
