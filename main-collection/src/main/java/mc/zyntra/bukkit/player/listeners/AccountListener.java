package mc.zyntra.bukkit.player.listeners;

import mc.zyntra.bukkit.BukkitGeneral;
import mc.zyntra.bukkit.event.account.PlayerGroupAddEvent;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.Language;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.group.GroupAttribute;
import mc.zyntra.general.account.group.GroupData;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.config.ServerConfiguration;
import mc.zyntra.bukkit.api.inventory.item.book.BookItem;
import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.bukkit.api.profile.ProfileManager;
import mc.zyntra.bukkit.api.tablist.Tablist;
import mc.zyntra.bukkit.event.account.PlayerGroupRemoveEvent;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.utils.component.TextComponentBuilder;
import mc.zyntra.general.utils.http.CountryRequest;
import mc.zyntra.general.utils.string.DateUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.UUID;

public class AccountListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        ZyntraPlayer zyntraPlayer = Core.getDataPlayer().load(event.getUniqueId());

        if (zyntraPlayer == null) {
            zyntraPlayer = Core.getDataPlayer().create(event.getUniqueId(), event.getName());
        }

        if (zyntraPlayer == null) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Constant.ACCOUNT_LOAD_FAILED);
            return;
        }

        if (event.getName().length() > 15) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Constant.NAME_TOO_LONG);
            return;
        }

        Core.getAccountController().create(zyntraPlayer);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (!zyntraPlayer.getFake().getName().equalsIgnoreCase(zyntraPlayer.getName())) {
            BukkitGeneral.getFakeAPI().changeFake(player, zyntraPlayer.getFake());
        } else {
            NametagController.getInstance().setNametag(zyntraPlayer.toPlayer(), zyntraPlayer.getTagHandler().getSelectedTag());
        }
        try {
            String json = CountryRequest.get(player.getAddress().getAddress().getHostAddress());
            JSONObject jsonObject = new JSONObject(json);
            String countryCode = jsonObject.optString("country", "");
            if (zyntraPlayer.getLanguage() == null) {
                zyntraPlayer.setLanguage((countryCode.equals("BR") ? Language.PT_BR : countryCode.equals("US") ? Language.EN_US : Language.PT_BR));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    return;

                if (zyntraPlayer.hasDisguise())
                    ProfileManager.changePlayerName(player, zyntraPlayer.getDisguise());

                NametagController.getInstance().setNametag(player, zyntraPlayer.getTagHandler().getSelectedTag());
            }
        }.runTaskAsynchronously(BukkitMain.getInstance());

        final Tablist TABLIST = new Tablist()
                .setHeader(Constant.TABLIST_HEADER.replace("%s", player.getDisplayName()))
                .setFooter(Constant.TABLIST_FOOTER);

        if (ServerConfiguration.DEFAULT_SERVER_TABLIST)
            TABLIST.send(player);

        Bukkit.getScheduler().runTaskLater(BukkitMain.getInstance(), () -> {
            try (Jedis jedis = Core.getRedisBackend().getPool().getResource()) {
                if (jedis.exists("bungee-teleport:" + player.getName())) {
                    Player target = Bukkit.getPlayer(UUID.fromString(jedis.get("bungee-teleport:" + player.getName())));

                    if (target != null) {
                        player.performCommand("tp " + target.getName());
                    }

                    jedis.del("bungee-teleport:" + player.getName());
                }
            }
        }, 6);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        Core.getAccountController().remove(event.getPlayer().getUniqueId());
        Core.getStatisticsController().unloadStatistics(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerGroupChange(PlayerGroupAddEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        Group group = event.getGroup().getGroup();

        player.sendMessage(event.getGroup().getAttribute().equals(GroupAttribute.PAYMENT) ?
                "§aSua compra foi processada! Você recebeu o rank " + group.getName() + " com duração " +
                        (zyntraPlayer.getPrimaryGroupData().getDuration() == -1L ? "vitalícia" : "de " + DateUtils.getTime(event.getDuration())) + "." :
                "§aVocê recebeu o rank " + group.getName() + " com duração " +
                        (zyntraPlayer.getPrimaryGroupData().getDuration() == -1L ? "vitalícia" : "de " + DateUtils.getTime(event.getDuration())) + ".");

        if (event.getGroup().getAttribute().equals(GroupAttribute.PAYMENT)) {
            new BookItem()
                    .title(null)
                    .author(null)
                    .pageComponents(
                            new TextComponent(
                                    "Sua compra foi processada!\n\n" +
                                            "Você recebeu os seguintes produtos:\n"
                            ),
                            new TextComponentBuilder(group.getColor() + "Rank " + group.getName())
                                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7A duração deste produto é " +
                                            (zyntraPlayer.getPrimaryGroupData().getDuration() == -1L ? "§fvitalícia" : "de §f" + DateUtils.getTime(event.getDuration())) + "§7.")))
                                    .build(),
                            new TextComponent(
                                    "\n\nSe você estiver com algum problema, contate-nos em:\n"
                            ),
                            new TextComponentBuilder("§9§lCLIQUE AQUI")
                                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aEntre em nosso Discord")))
                                    .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                            Constant.DISCORD.startsWith("https://") ? Constant.DISCORD : "https://" + Constant.DISCORD))
                                    .build()
                    )
                    .open(player);
        }

        NametagController.getInstance().setNametag(player, group.getTag());
    }

    @EventHandler
    public void onPlayerGroupRemove(PlayerGroupRemoveEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        Group group = event.getGroup().getGroup();

        player.sendMessage("§cO cargo " + group.getName() + " foi removido da sua conta.");

        NametagController.getInstance().setNametag(player, zyntraPlayer.getPrimaryGroupData().getGroup().getTag());
    }


    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateEvent.UpdateType.SECOND)
            return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
            GroupData groupData = zyntraPlayer.getPrimaryGroupData();

            if (groupData.getDuration() == -1)
                continue;

            if (groupData.getDuration() >= System.currentTimeMillis())
                continue;

            NametagController.getInstance().setNametag(zyntraPlayer.toPlayer(), zyntraPlayer.getTagHandler().getSelectedTag());

            if (zyntraPlayer.getFake() != null) {
                if (!zyntraPlayer.getFake().getName().equalsIgnoreCase(player.getName())) {
                    NametagController.getInstance().setNametag(zyntraPlayer.toPlayer(), zyntraPlayer.getFake().getTag());
                } else {
                    NametagController.getInstance().setNametag(zyntraPlayer.toPlayer(), zyntraPlayer.getTagHandler().getSelectedTag());
                }
            }

            player.sendMessage("");
            player.sendMessage("§c§lLAMENTAMOS");
            player.sendMessage("§cA duração do seu grupo " + groupData.getGroup().getName() + " obteve o seu termino :(");
            player.sendMessage("§cAdquira-o novamente em: §e" + Constant.STORE);
            player.sendMessage("");
            groupData.setGroup(Group.DEFAULT);
            groupData.setAttribute(GroupAttribute.SYSTEM);
            groupData.setDate(System.currentTimeMillis());
            groupData.setDuration(-1L);
            groupData.setAddedBy("CONSOLE");

            zyntraPlayer.update("groupData");

            NametagController.getInstance().setNametag(player, Tag.MEMBRO);
        }
    }
}
