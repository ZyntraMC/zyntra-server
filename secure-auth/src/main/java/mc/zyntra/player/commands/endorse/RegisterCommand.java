package mc.zyntra.player.commands.endorse;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.player.event.PlayerCompletedRegistrationEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

            if (args.length == 2) {
                if (zyntraPlayer.isRegistered()) {
                    player.sendMessage("§cVocê já está cadastrado.");
                    return false;
                }

                String password = args[0];

                if (password.length() < 8 || password.length() > 16) {
                    player.sendMessage("§cA senha precisa ter no mínimo 8 caracteres com o máximo de 16 possuindo pelo menos um simbolo.");
                    return false;
                }

                if (!password.equals(args[1])) {
                    player.sendMessage("§cA senha precisa ser igual.");
                    return false;
                }

                zyntraPlayer.setLogged(true);
                zyntraPlayer.setPassword(password);
                zyntraPlayer.update("logged");
                zyntraPlayer.update("password");
                Core.getDataPlayer().update(zyntraPlayer, "password");
                Core.getDataPlayer().save(zyntraPlayer);
                Bukkit.getPluginManager().callEvent(new PlayerCompletedRegistrationEvent(player));
                return false;
            }

            player.sendMessage(String.format(Constant.COMMAND_USAGE, label + " [senha] [confirmar senha]"));
        }
        return false;
    }
}
