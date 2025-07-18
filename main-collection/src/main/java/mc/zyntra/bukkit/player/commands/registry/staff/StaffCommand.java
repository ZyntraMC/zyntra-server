package mc.zyntra.bukkit.player.commands.registry.staff;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.bukkit.api.config.ServerConfiguration;
import mc.zyntra.bukkit.api.vanish.VanishManager;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandClass {

    @Command(
            name = "clearchat",
            aliases = "cc",
            group = Group.MODERATOR,
            inGameOnly = true
    )
    public void clearchat(CommandContext context) {
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(players -> {
            players.sendMessage(StringUtils.repeat(" \n", 100));
            players.sendMessage("§dO bate-papo público foi limpo.");
        });

        notify(zyntraPlayer.getName() + " limpou o bate-papo");
    }

    @Command(
            name = "chat",
            group = Group.MODERATOR,
            inGameOnly = true
    )
    public void chat(CommandContext context) {
        Player player = context.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        ServerConfiguration.CHAT_ENABLED = !ServerConfiguration.CHAT_ENABLED;

        broadcast(ServerConfiguration.CHAT_ENABLED
                ? "§dO bate-papo público foi habilitado!"
                : "§dO bate-papo público foi desabilitado!"
        );

        notify(zyntraPlayer.getName() + " " + (ServerConfiguration.CHAT_ENABLED ? "habilitou" : "desabilitou") + " o bate-papo");
    }

    @Command(
            name = "admin",
            aliases = {"vanish", "v"},
            group = Group.CREATOR,
            inGameOnly = true
    )
    public void admin(CommandContext context) {
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(context.getPlayer().getUniqueId());
        VanishManager.vanish(zyntraPlayer);
    }
}