package mc.zyntra.server.player.commands.registry.player;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.clan.Clan;
import mc.zyntra.general.clan.member.ClanMember;
import mc.zyntra.general.clan.member.role.MemberRole;
import mc.zyntra.general.command.Command;
import mc.zyntra.general.command.CommandClass;
import mc.zyntra.general.command.CommandContext;
import mc.zyntra.server.ServerMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.UUID;

public class ClanCommand implements CommandClass {

    @Command(
            name = "clan",
            aliases = {"alianca", "clã", "guild", "g", "guilda", "facção", "f", "fac"},
            inGameOnly = true
    )
    public void clan(CommandContext context) {
        String[] args = context.getArguments();
        ProxiedPlayer player = context.getProxiedPlayer();
        UUID playerUuid = player.getUniqueId();

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(playerUuid);

        if (!zyntraPlayer.hasGroup(Group.GOLD)) {
            player.sendMessage("§c§lERROR ➜ §r§cVocê não tem permissão para usar este comando.");
            return;
        }

        Clan playerClan = ServerMain.getInstance().getClanController().getAllClans().stream()
                .filter(c -> c.isMember(playerUuid))
                .findFirst().orElse(null);

        if (args.length == 0) {
            sendHelpPage1(player);
            return;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "criar":
                if (args.length < 3) {
                    player.sendMessage("§c/clan criar [nome] [tag] §7➜ Cria um clan com as informações fornecidas");
                    return;
                }
                if (playerClan != null) {
                    player.sendMessage("§c§lERROR ➜ §r§cVocê já está em um clã.");
                    return;
                }
                String name = args[1];
                String tag = args[2];
                if (ServerMain.getInstance().getClanController().getClanByName(name) != null) {
                    player.sendMessage("§c§lERROR ➜ §r§ccNome já utilizado.");
                    return;
                }
                if (ServerMain.getInstance().getClanController().getClanByTag(tag) != null) {
                    player.sendMessage("§c§lERROR ➜ §r§cTag já utilizada.");
                    return;
                }
                Clan clan = ServerMain.getInstance().getClanController().createClan(name, playerUuid, tag);
                ServerMain.getInstance().getClanController().saveClan(clan);
                player.sendMessage("§aClã criado com sucesso!");
                return;

            case "deletar":
                if (playerClan == null || !playerClan.isLeader(playerUuid)) {
                    player.sendMessage("§cVocê não pode deletar este clã.");
                    return;
                }
                ServerMain.getInstance().getClanController().deleteClan(playerClan);
                player.sendMessage("§aClã deletado com sucesso.");
                return;

            case "convidar":
                if (args.length < 2) {
                    player.sendMessage("§c/clan convidar [Jogador] §7➜ Envia um convite para o seu clã para o jogador");
                    return;
                }
                if (playerClan == null || !playerClan.isLeader(playerUuid)) {
                    player.sendMessage("§c§lERROR ➜ §r§cVocê não pode convidar jogadores.");
                    return;
                }
                String targetName = args[1];
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
                if (target == null) {
                    player.sendMessage("§c§lERROR ➜ §r§cJogador não encontrado.");
                    return;
                }
                ServerMain.getInstance().getClanController().invitePlayer(target.getUniqueId(), playerClan);
                player.sendMessage("§aConvite enviado para " + targetName);
                return;

            case "aceitar":
                if (playerClan != null) {
                    player.sendMessage("§cVocê já está em um clã.");
                    return;
                }
                if (!ServerMain.getInstance().getClanController().hasInvite(playerUuid)) {
                    player.sendMessage("§cVocê não tem convites pendentes.");
                    return;
                }
                String clanName = ServerMain.getInstance().getClanController().getInvite(playerUuid);
                Clan acceptingClan = ServerMain.getInstance().getClanController().getClanByName(clanName);
                if (acceptingClan == null) {
                    player.sendMessage("§c§lERROR ➜ §r§cClã não encontrado.");
                    return;
                }
                acceptingClan.addMember(new ClanMember(playerUuid, MemberRole.DEFAULT));
                ServerMain.getInstance().getClanController().saveClan(acceptingClan);
                ServerMain.getInstance().getClanController().removeInvite(playerUuid);
                player.sendMessage("§aVocê entrou no clã " + acceptingClan.getClanName() + ".");
                return;

            case "recusar":
                ServerMain.getInstance().getClanController().removeInvite(playerUuid);
                player.sendMessage("§aConvite recusado.");
                return;

            case "listar":
                Collection<Clan> clans = ServerMain.getInstance().getClanController().getAllClans();
                player.sendMessage("§eClãs disponíveis:");
                for (Clan c : clans) {
                    player.sendMessage("§7- " + c.getClanName() + " [" + c.getTag() + "]");
                }
                return;

            case "ver":
                if (args.length < 2) {
                    player.sendMessage("§c/clan ver [nome] §7➜ Visualiza informações sobre o clã mencionado");
                    return;
                }
                Clan viewed = ServerMain.getInstance().getClanController().getClanByName(args[1]);
                if (viewed == null) {
                    player.sendMessage("§c§lERROR ➜ §r§cClã não encontrado.");
                    return;
                }
                player.sendMessage("§eClã: §6" + viewed.getClanName());
                player.sendMessage("§7Tag: §f" + viewed.getTag());
                player.sendMessage("§7Membros: §f" + viewed.getSize());
                return;

            default:
                if (isPageCommand(subcommand)) {
                    sendHelpPage(player, subcommand);
                } else {
                    sendHelpPage1(player);
                }
        }
    }

    private boolean isPageCommand(String arg) {
        return arg.equals("1") || arg.equals("2") || arg.equals("3");
    }

    private void sendHelpPage1(ProxiedPlayer player) {
        player.sendMessage("§eComandos principais de clã. Use /clan [pagina]:");
        player.sendMessage("§e/clan criar [nome] [tag] §7➜ Cria um clan com as informações fornecidas");
        player.sendMessage("§e/clan deletar §7➜ Deleta o seu clã §cEFEITO IRREVERSIVEL");
        player.sendMessage("§e/clan aceitar [nome] §7➜ Aceita um pedido de clã que te foi enviado");
        player.sendMessage("§e/clan recusar [nome] §7➜ Recusa um pedido de clã que te foi enviado");
        player.sendMessage("§ePágina 1 de 3");
    }

    private void sendHelpPage(ProxiedPlayer player, String page) {
        switch (page) {
            case "2":
                player.sendMessage("§e/clan convidar [Jogador] §7➜ Envia um convite para o seu clã para o jogador");
                player.sendMessage("§e/clan ver [nome] §7➜ Visualiza informações sobre o clã mencionado");
                player.sendMessage("§e/clan expulsar [Jogador] §7➜ Remove um integrante do seu clã");
                player.sendMessage("§e/clan sair §7➜ Você sai do clã onde você está participando");
                player.sendMessage("§ePágina 2 de 3");
                break;
            case "3":
                player.sendMessage("§e/clan rebaixar [Jogador] §7➜ Rebaixa um membro do seu clã");
                player.sendMessage("§e/clan ver [nome] §7➜ Visualiza informações sobre o clã mencionado");
                player.sendMessage("§ePágina 3 de 3");
            default:
                sendHelpPage1(player);
                break;
        }
    }
}
