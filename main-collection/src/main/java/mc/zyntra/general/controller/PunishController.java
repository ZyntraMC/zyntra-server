package mc.zyntra.general.controller;

import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.punishment.constructor.Punishment;

public interface PunishController {

    void punish(ZyntraPlayer zyntraPlayer, Punishment punishment);

    void revoke(ZyntraPlayer zyntraPlayer, String id, String revokedBy);

}
