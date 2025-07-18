package mc.zyntra.bukkit.api.nametag;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import lombok.Getter;
import mc.zyntra.general.account.medal.enums.Medal;
import mc.zyntra.general.account.tag.enums.Tag;
import mc.zyntra.general.clan.Clan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

public class NametagController implements Listener {

    private final HashMap<UUID, Nametag> nametagMap = new HashMap<>();

    @Getter
    private static NametagController instance;

    public NametagController() {
        instance = this;
    }

    public void setNametag(Player player, String prefix, String suffix, String teamFormat) {
        nametagMap.put(player.getUniqueId(), new Nametag(prefix, suffix, teamFormat));
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            update(onlinePlayer);
        }
    }

    public void update(Player player) {
        Nametag nametag = nametagMap.get(player.getUniqueId());
        if (nametag == null)
            return;

        Scoreboard scoreboard = player.getScoreboard();
        String format = nametag.getTeam() + player.getName();

        Team newTeam = scoreboard.getTeam(format);
        if (newTeam == null) {
            for (Team team : scoreboard.getTeams()) {
                if (team.getName().equalsIgnoreCase(format)) {
                    team.unregister();
                }
            }
            newTeam = scoreboard.registerNewTeam(format);
        }
        if (!newTeam.hasEntry(player.getName())) {
            newTeam.addPlayer(player);
        }
        newTeam.setPrefix(nametag.getPrefix());
        newTeam.setSuffix(nametag.getSuffix());
        player.setDisplayName(newTeam.getPrefix() + player.getName());

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId() == players.getUniqueId())
                continue;

            Nametag ntag = nametagMap.get(players.getUniqueId());
            if (ntag == null)
                continue;

            String f = ntag.getTeam() + players.getName();
            Team newer = scoreboard.getTeam(f);
            if (newer == null) {
                for (Team team : scoreboard.getTeams()) {
                    if (team.getName().equalsIgnoreCase(f)) {
                        team.unregister();
                    }
                }
                newer = scoreboard.registerNewTeam(f);
            }
            newer.setPrefix(ntag.getPrefix());
            newer.setSuffix(ntag.getSuffix());
            if (!newer.hasEntry(players.getName()))
                newer.addPlayer(players);
        }
    }

    public void setNametag(Player player, Tag tag) {
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        String prefix = (tag.equals(Tag.MEMBRO) ? tag.getColor() : tag.getColor() + "[" + tag.getName() + "] §7");
        Clan playerClan = BukkitMain.getInstance().getClanController().getAllClans().stream()
                .filter(c -> c.isMember(player.getUniqueId()))
                .findFirst().orElse(null);

        String suffixClan = "";

        String suffixMedal = "";

        if (playerClan != null) {
            if (playerClan.getAura() < 500) {
                suffixClan = "";
            } else if (playerClan.getAura() > 500) {
                suffixClan = "§6[" + playerClan.getTag() + "§6]";
            }
        }


        Medal medal = zyntraPlayer.getMedalHandler().getSelectedMedal();
        if (medal != Medal.NONE) {
            suffixMedal = medal.getColor() + medal.getSymbol() + " §7";
        }

        String suffix = " " + suffixClan;

        zyntraPlayer.getTagHandler().setSelectedTag(tag);
        setNametag(player, prefix, suffix, tag.getTeam());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        nametagMap.remove(event.getPlayer().getUniqueId());
    }
}