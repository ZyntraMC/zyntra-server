package mc.zyntra.bukkit.player.listeners;

import mc.zyntra.bukkit.BukkitGeneral;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.ConfigType;
import mc.zyntra.bukkit.api.cooldown.CooldownAPI;
import mc.zyntra.bukkit.api.cooldown.types.Cooldown;
import mc.zyntra.bukkit.api.config.ServerConfiguration;
import mc.zyntra.general.account.config.Language;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.medal.enums.Medal;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.clan.Clan;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        Clan playerClan = BukkitMain.getInstance().getClanController().getAllClans().stream()
                .filter(c -> c.isMember(player.getUniqueId()))
                .findFirst().orElse(null);

        String suffixClan = "";

        String suffixMedal = "";

        if (playerClan != null) {
            if (playerClan.getAura() < 500) {
                suffixClan = "";
            } else if (playerClan.getAura() > 500) {
                suffixClan = "§5[" + playerClan.getTag() + "§5]";
            }
        }

        Medal medal = zyntraPlayer.getMedalHandler().getSelectedMedal();
        if (medal != Medal.NONE) {
            suffixMedal = " " +  medal.getColor() + medal.getSymbol() + " §7";
        }

        String suffix = suffixMedal + " " + suffixClan;

        if (ServerConfiguration.DEFAULT_SERVER_CHAT) {
            if (!ServerConfiguration.CHAT_ENABLED && !zyntraPlayer.hasGroupPermission(Group.MODERATOR)) {
                player.sendMessage("§cO bate-papo está desabilitado.");
                return;
            }

            if (CooldownAPI.getInstance().hasCooldown(player, "chat")) {
                Cooldown cooldown = CooldownAPI.getInstance().getCooldown(player.getUniqueId(), "chat");
                player.sendMessage("§cAguarde " + Constant.DECIMAL_FORMAT.format(cooldown.getRemaining()) + " para conversar novamente.");
                return;
            }

            Arrays.stream(Constant.SWEAR_WORDS)
                    .filter(word -> event.getMessage().toLowerCase().contains(word))
                    .forEach(word ->
                            event.setMessage(event.getMessage().toLowerCase().replace(word, StringUtils.repeat("*", word.length()))));

            Core.getAccountController().list().stream()
                    .filter(players -> players.getConfiguration().isEnabled(ConfigType.CHAT))
                    .forEach(players -> {

                        if (!zyntraPlayer.getFake().getName().equalsIgnoreCase(zyntraPlayer.getName())) {
                            players.sendMessage( zyntraPlayer.getFake().getTag().getColor() + "[" + zyntraPlayer.getFake().getTag().getName() + "]§7 " + zyntraPlayer.getFake().getName() + suffix + "§f: " + (zyntraPlayer.hasGroupPermission(Group.HELPER) ? "§f" +
                                    event.getMessage().replace("&", "§") : "§f" + event.getMessage()));
                        } else {
                            players.sendMessage( zyntraPlayer.getTagHandler().getSelectedTag().getColor() + "[" + zyntraPlayer.getTagHandler().getSelectedTag().getName() + "]§7 " + zyntraPlayer.getName() + suffix + "§f: " + (zyntraPlayer.hasGroupPermission(Group.HELPER) ? "§f" +
                                    event.getMessage().replace("&", "§") : "§f" + event.getMessage()));
                        }
                    });

            if (!zyntraPlayer.hasGroupPermission(Group.HELPER)) {
                CooldownAPI.getInstance().addCooldown(player.getUniqueId(), "chat", zyntraPlayer.hasGroupPermission(Group.GOLD) ? 1 : 2);
            }
        }

        //System.out.println("<" + player.getName() + "> " + event.getMessage());
    }
}
