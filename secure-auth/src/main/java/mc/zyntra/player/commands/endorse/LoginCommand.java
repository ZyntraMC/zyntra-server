package mc.zyntra.player.commands.endorse;

import mc.zyntra.Secure;
import mc.zyntra.player.generator.User;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.player.event.PlayerLoggedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
            User user = Secure.getInstance().getUserParser().get(player.getUniqueId());

            if (args.length == 1) {
                if (!zyntraPlayer.isRegistered()) {
                    player.sendMessage("§cVocê não possuí cadastro.");
                    return false;
                }

                if (zyntraPlayer.isLogged()) {
                    player.sendMessage("§cVocê já está logado.");
                    return false;
                }

                String password = args[0];

                if (!zyntraPlayer.getPassword().equals(password)) {
                    if (user.getRemainingAttemptsLogin() <= 0) {
                        player.kickPlayer("§cVocê excedeu o limite de tentativas de login.");
                        return false;
                    }

                    user.setRemainingAttemptsLogin(user.getRemainingAttemptsLogin() - 1);
                    player.sendMessage("§cSenha errada! Você possuí " + user.getRemainingAttemptsLogin() + " tentativas restantes.");
                    player.playSound(user.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 100f, 100f);
                    return false;
                }

                zyntraPlayer.setLogged(true);
                zyntraPlayer.update("logged");
                Core.getDataPlayer().save(zyntraPlayer);

                Bukkit.getPluginManager().callEvent(new PlayerLoggedEvent(player));
                return false;
            }

            player.sendMessage(String.format(Constant.COMMAND_USAGE, label + " [senha]"));
        }
        return false;
    }
}
