package mc.zyntra.server.player.commands.registry;

import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.general.account.friend.FriendController;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.general.command.Completer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerCommand implements CommandClass {

    @Command(
            name = "tell",
            aliases = {"whisper", "w"},
            inGameOnly = true
    )
    public void tell(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <target> <msg>"));
            return;
        }

        ZyntraPlayer target = Core.getAccountController().get(args[0]);

        if (target == null) {
            player.sendMessage(Constant.PLAYER_NOT_FOUND);
            return;
        }

        if (target.equals(zyntraPlayer)) {
            player.sendMessage("§cVocê não pode conversar com si mesmo.");
            return;
        }

        if (!zyntraPlayer.getConfiguration().isEnabled(ConfigType.TELL)) {
            player.sendMessage("§cVocê não pode enviar mensagens privadas pois está desabilitado.");
            return;
        }

        if (!target.getConfiguration().isEnabled(ConfigType.TELL)) {
            player.sendMessage("§cVocê não pode conversar com este jogador pois ele possuí as mensagens privadas desabilitadas.");
            return;
        }

        if (zyntraPlayer.hasCooldown("tell")) {
            player.sendMessage("§cAguarde " + Constant.DECIMAL_FORMAT.format(zyntraPlayer.getCooldown("tell")) + " para conversar novamente.");
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        zyntraPlayer.setLastTell(target.getName());
        target.setLastTell(zyntraPlayer.getName());

        player.sendMessage("§8[Mensagem para " + target.getDisguise() + "§8] §6" + message);
        target.sendMessage("§8[Mensagem de " + zyntraPlayer.getDisguise() + "§8] §6" + message);

        zyntraPlayer.addCooldown("tell", 2);
    }

    @Command(
            name = "reply",
            aliases = {"r"},
            inGameOnly = true
    )
    public void reply(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(String.format(Constant.COMMAND_USAGE, context.getCommandLabel() + " <msg>"));
            return;
        }

        if (zyntraPlayer.getLastTell() == null) {
            player.sendMessage("§cNenhuma conversa foi encontrada recentemente.");
            return;
        }

        ZyntraPlayer target = Core.getAccountController().get(zyntraPlayer.getLastTell());

        if (target == null) {
            player.sendMessage(Constant.PLAYER_NOT_FOUND);
            return;
        }

        if (!zyntraPlayer.getConfiguration().isEnabled(ConfigType.TELL)) {
            player.sendMessage("§cVocê não pode enviar mensagens privadas pois está desabilitado.");
            return;
        }

        if (!target.getConfiguration().isEnabled(ConfigType.TELL)) {
            player.sendMessage("§cVocê não pode conversar com este jogador pois ele possuí as mensagens privadas desabilitadas.");
            return;
        }

        zyntraPlayer.setLastTell(target.getName());
        target.setLastTell(zyntraPlayer.getName());

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        player.sendMessage("§8[§7Você §f» §7" + target.getDisguise() + "§8] §e" + message);
        target.sendMessage("§8[§7" + zyntraPlayer.getDisguise() + " §f» §7Você§8] §e" + message);
    }

    @Command(
            name = "ping",
            inGameOnly = true
    )
    public void ping(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();

        if (args.length == 1) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(Constant.PLAYER_NOT_FOUND);
                return;
            }

            player.sendMessage("§eO ping de §6" + target.getName() + " §eé de §b" + target.getPing() + "ms§e.");
            return;
        }

        player.sendMessage("§eO seu ping é de §b" + player.getPing() + "ms§e.");
    }

    @Completer(
            name = "tell",
            aliases = {"whisper", "w"}
    )
    public List<String> tellCompleter(CommandContext context) {
        List<String> list = new ArrayList<>();
        Core.getAccountController().list().forEach(zyntraPlayer -> list.add(zyntraPlayer.getDisguise()));
        return list;
    }

    private boolean page(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Command(
            name = "amigo",
            aliases = {"friend", "amigos", "friends"},
            usage = "/amigo <adicionar|aceitar|negar|remover|lista|pendentes> [jogador]",
            description = "Gerencie sua lista de amigos",
            inGameOnly = true
    )
    public void amigo(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer ar = context.getProxiedPlayer();
        ZyntraPlayer player = Core.getAccountController().get(ar.getUniqueId());

        FriendController controller = Core.getFriendController();

        if (args.length == 0) {
            player.sendMessage("");
            player.sendMessage("");
            player.sendMessage("§a/amigo adicionar [Jogador] §7➜ Mandar pedido de amizade para um jogador");
            player.sendMessage("§a/amigo aceitar [Jogador] §7➜ Aceita um pedido de amizade");
            player.sendMessage("§a/amigo negar [Jogador] §7➜ Nega um pedido de amizade");
            player.sendMessage("§a/amigo remover [Jogador] §7➜ Remove um amigo da sua lista");
            player.sendMessage("§a/amigo lista §7➜ Mostra a lista de todas suas amizades");
            player.sendMessage("§a/amigo pendentes §7➜ Mostra os pedidos de amizade pendentes");
            return;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "adicionar":
                if (args.length < 2) {
                    player.sendMessage("§cUso correto: /amigo adicionar <jogador>");
                    return;
                }
                ZyntraPlayer targetAdd = Core.getAccountController().get(args[1]);
                if (targetAdd != null) controller.sendRequest(player, targetAdd);
                else player.sendMessage(Constant.PLAYER_NOT_FOUND);
                break;

            case "aceitar":
                if (args.length < 2) {
                    player.sendMessage("§cUso correto: /amigo aceitar <jogador>");
                    return;
                }
                ZyntraPlayer targetAccept = Core.getAccountController().get(args[1]);
                if (targetAccept != null) controller.acceptRequest(player, targetAccept);
                else player.sendMessage(Constant.PLAYER_NOT_FOUND);
                break;

            case "negar":
                if (args.length < 2) {
                    player.sendMessage("§cUso correto: /amigo negar <jogador>");
                    return;
                }
                ZyntraPlayer targetDeny = Core.getAccountController().get(args[1]);
                if (targetDeny != null) controller.denyRequest(player, targetDeny);
                else player.sendMessage(Constant.PLAYER_NOT_FOUND);
                break;

            case "remover":
                if (args.length < 2) {
                    player.sendMessage("§cUso correto: /amigo remover <jogador>");
                    return;
                }
                ZyntraPlayer targetRemove = Core.getAccountController().get(args[1]);
                if (targetRemove != null) controller.removeFriend(player, targetRemove);
                else player.sendMessage(Constant.PLAYER_NOT_FOUND);
                break;

            case "lista":
                controller.listFriends(player);
                break;

            case "pendentes":
                controller.listRequests(player);
                break;

            default:
                ZyntraPlayer defaultTarget = Core.getAccountController().get(args[0]);
                if (defaultTarget != null) controller.sendRequest(player, defaultTarget);
                else player.sendMessage(Constant.PLAYER_NOT_FOUND);
                break;
        }
    }
}
