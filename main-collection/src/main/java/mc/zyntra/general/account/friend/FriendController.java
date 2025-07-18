package mc.zyntra.general.account.friend;

import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.Core;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class FriendController {

    private final Map<UUID, Set<UUID>> friendRequests = new HashMap<>();

    public void sendRequest(ZyntraPlayer sender, ZyntraPlayer target) {
        if (sender.equals(target)) {
            sender.sendMessage("§cVocê não pode adicionar a si mesmo.");
            return;
        }

        if (sender.getFriends().contains(target.getUniqueId())) {
            sender.sendMessage("§cVocês já são amigos.");
            return;
        }

        friendRequests.computeIfAbsent(target.getUniqueId(), k -> new HashSet<>()).add(sender.getUniqueId());

        TextComponent message = new TextComponent("§eVocê recebeu um pedido de amizade de §f" + target.getTagHandler().getSelectedTag().getColor() + "[" + target.getTagHandler().getSelectedTag().getName() + "] §7"+ target.getName() + " ");

        TextComponent acceptComponent = new TextComponent("§a[ACEITAR]");
        acceptComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§eClique para aceitar o pedido de amizade.").create()));
        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/amigo aceitar " + sender.getName()));

        TextComponent denyComponent = new TextComponent("§c[NEGAR]");
        denyComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§eClique para recusar o pedido de amizade.").create()));
        denyComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/amigo negar " + sender.getName()));

        message.addExtra(acceptComponent);
        message.addExtra(" ");
        message.addExtra(denyComponent);

        target.sendMessage(message);

        sender.sendMessage("§aPedido de amizade enviado para §f" + target.getTagHandler().getSelectedTag().getColor() + "[" + target.getTagHandler().getSelectedTag().getName() + "] §7"+ target.getName() + ".");

        playSound(sender, "LEVEL_UP", 1.0f, 1.0f);
        playSound(target, "LEVEL_UP", 1.0f, 1.0f);
    }

    public void acceptRequest(ZyntraPlayer player, ZyntraPlayer requester) {
        Set<UUID> requests = friendRequests.get(player.getUniqueId());
        if (requests == null || !requests.contains(requester.getUniqueId())) {
            player.sendMessage("§cVocê não possui um pedido de amizade deste jogador.");
            return;
        }

        requests.remove(requester.getUniqueId());

        player.getFriends().add(requester.getUniqueId());
        requester.getFriends().add(player.getUniqueId());

        player.save();
        requester.save();

        player.sendMessage("§aVocê aceitou o pedido de amizade de §f" + requester.getTagHandler().getSelectedTag().getColor() + "[" + requester.getTagHandler().getSelectedTag().getName() + "] §7"+ requester.getName() + ".");
        requester.sendMessage("§aSeu pedido de amizade foi aceito por §f" + player.getTagHandler().getSelectedTag().getColor() + "[" + player.getTagHandler().getSelectedTag().getName() + "] §7"+ player.getName() + ".");

        playSound(player, "LEVEL_UP", 1.0f, 1.0f);
        playSound(requester, "LEVEL_UP", 1.0f, 1.0f);
    }

    public void denyRequest(ZyntraPlayer player, ZyntraPlayer requester) {
        Set<UUID> requests = friendRequests.get(player.getUniqueId());
        if (requests == null || !requests.contains(requester.getUniqueId())) {
            player.sendMessage("§cVocê não possui um pedido de amizade deste jogador.");
            return;
        }

        requests.remove(requester.getUniqueId());

        player.sendMessage("§cVocê recusou o pedido de amizade de §f" + requester.getTagHandler().getSelectedTag().getColor() + "[" + requester.getTagHandler().getSelectedTag().getName() + "] §7"+ requester.getName() + ".");
        requester.sendMessage("§cSeu pedido de amizade foi recusado por §f" + player.getTagHandler().getSelectedTag().getColor() + "[" + player.getTagHandler().getSelectedTag().getName() + "] §7"+ player.getName() + ".");

        playSound(player, "NOTE_BASS_DRUM", 1.0f, 1.0f);
        playSound(requester, "NOTE_BASS_DRUM", 1.0f, 1.0f);
    }

    public void removeFriend(ZyntraPlayer player, ZyntraPlayer target) {
        if (!player.getFriends().contains(target.getUniqueId())) {
            player.sendMessage("§cEste jogador não está na sua lista de amigos.");
            return;
        }

        player.getFriends().remove(target.getUniqueId());
        target.getFriends().remove(player.getUniqueId());

        player.save();
        target.save();

        player.sendMessage("§eVocê removeu §f" + target.getTagHandler().getSelectedTag().getColor() + "[" + target.getTagHandler().getSelectedTag().getName() + "] §7" + target.getName() + " §eda sua lista de amigos.");
        target.sendMessage("§eVocê foi removido da lista de amigos de §f" + player.getTagHandler().getSelectedTag().getColor() + "[" + player.getTagHandler().getSelectedTag().getName() + "] §7"+ player.getName() + ".");

        playSound(player, "NOTE_BASS_DRUM", 1.0f, 1.0f);
        playSound(target, "NOTE_BASS_DRUM", 1.0f, 1.0f);
    }

    public void listFriends(ZyntraPlayer player) {
        if (player.getFriends().isEmpty()) {
            player.sendMessage("§cSua lista de amigos está vazia.");
            return;
        }

        player.sendMessage("§aSeus amigos:");
        for (UUID uuid : player.getFriends()) {
            ZyntraPlayer friend = getPlayerByUUID(uuid);
            if (friend != null) {

                TextComponent message = new TextComponent("§e- " + friend.getTagHandler().getSelectedTag().getColor() + "[" + friend.getTagHandler().getSelectedTag().getName() + "] §7"+ friend.getName() + " | ");

                TextComponent removeComponent = new TextComponent("§c[Remover]");
                removeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§eClique para remove-lo da sua lista de amigos").create()));
                removeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/amigo remover " + friend.getName()));

                TextComponent inviteComponent = new TextComponent("§d[Convidar]");
                inviteComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§eClique para convida-lo para sua party").create()));
                inviteComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party convidar " + friend.getName()));

                message.addExtra(removeComponent);
                message.addExtra(" ");
                message.addExtra(inviteComponent);

                player.sendMessage(message);
            }
        }
    }

    public void listRequests(ZyntraPlayer player) {
        Set<UUID> requests = friendRequests.get(player.getUniqueId());
        if (requests == null || requests.isEmpty()) {
            player.sendMessage("§eVocê não possui pedidos de amizade pendentes.");
            return;
        }

        player.sendMessage("§aPedidos de amizade pendentes:");
        for (UUID uuid : requests) {
            ZyntraPlayer requester = getPlayerByUUID(uuid);
            if (requester != null) {

                TextComponent message = new TextComponent("§e- " + requester.getTagHandler().getSelectedTag().getColor() + "[" + requester.getTagHandler().getSelectedTag().getName() + "] §7"+ requester.getName() + " | ");

                TextComponent removeComponent = new TextComponent("§A[Aceitar]");
                removeComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§eClique para aceita-lo como amigo").create()));
                removeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/amigo aceitar " + requester.getName()));

                TextComponent inviteComponent = new TextComponent("§c[Recusar]");
                inviteComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§eClique para recusa-lo como amigo").create()));
                inviteComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party recusar " + requester.getName()));

                message.addExtra(removeComponent);
                message.addExtra(" ");
                message.addExtra(inviteComponent);

                player.sendMessage(message);
            }
        }
    }

    private ZyntraPlayer getPlayerByUUID(UUID uuid) {
        return Core.getAccountController().get(uuid);
    }

    public void playSound(ZyntraPlayer player, String sound, float volume, float pitch) {
        ProxiedPlayer proxiedPlayer = player.toProxiedPlayer();
        if (proxiedPlayer == null) return;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF(sound);
            out.writeFloat(volume);
            out.writeFloat(pitch);
        } catch (IOException e) {
            e.printStackTrace();
        }

        proxiedPlayer.getServer().sendData("bungee:sound", b.toByteArray());
    }

}