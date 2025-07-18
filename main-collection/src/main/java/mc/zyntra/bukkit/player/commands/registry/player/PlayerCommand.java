package mc.zyntra.bukkit.player.commands.registry.player;

import mc.zyntra.general.Constant;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.bukkit.player.inventories.AccountInventory;
import mc.zyntra.bukkit.player.inventories.preferences.PreferencesInventory;
import mc.zyntra.bukkit.player.inventories.statistics.StatisticsInventory;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.account.group.Group;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandClass {

    @Command(
            name = "profile",
            aliases = {"perfil"},
            inGameOnly = true
    )
    public void profile(CommandContext context) {
        new AccountInventory(Core.getAccountController().get(context.getPlayer().getUniqueId())).open(context.getPlayer());
    }

    @Command(
            name = "status",
            aliases = {"stats"},
            inGameOnly = true
    )
    public void status(CommandContext context) {
        String[] args = context.getArguments();
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        ZyntraPlayer target = null;

        if (args.length == 1) {
            if (!zyntraPlayer.hasGroupPermission(Group.GOLD)) {
                player.sendMessage(Constant.COMMAND_NO_ACCESS);
                return;
            }

            target = Core.getDataPlayer().load(args[0]);

            if (target == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            if (!target.getConfiguration().isEnabled(ConfigType.STATISTICS_VISIBILITY) && !zyntraPlayer.hasGroupPermission(Group.CREATOR)) {
                player.sendMessage("§c§lERROR ➜ §r§cVocê não pode visualizar as estatísticas deste jogador.");
                return;
            }
        }

        new StatisticsInventory(zyntraPlayer, target == null ? zyntraPlayer : target, StatisticsInventory.StatisticsMenuType.MAIN, true).open(player);
    }

    @Command(
            name = "preferences",
            aliases = {"preferencias", "prefs", "service"},
            inGameOnly = true
    )
    public void preferences(CommandContext context) {
        Player player = context.getPlayer();
        new PreferencesInventory(Core.getAccountController().get(player.getUniqueId()), true).open(player);
    }
}