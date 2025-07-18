package mc.zyntra.match.listener;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.account.statistics.StatisticsType;
import mc.zyntra.general.account.statistics.types.GameStatistics;
import mc.zyntra.general.utils.ClassGetter;
import mc.zyntra.match.kit.Kit;
import mc.zyntra.match.perks.Perks;
import mc.zyntra.util.Platform;
import mc.zyntra.Leading;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.room.Room;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.lang.reflect.Constructor;
import java.util.List;

public class PlayerJoinListener implements Listener {

    List<Class<?>> list =
            ClassGetter.getClassesForPackage(Leading.getInstance().getClass(), "mc.zyntra.match" +
                    ".kit.registry");

    List<Class<?>> list2 =
            ClassGetter.getClassesForPackage(Leading.getInstance().getClass(), "mc.zyntra.match" +
                    ".perks.registry");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        Room room = Leading.getInstance().getRoom();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        if (room == null) {
            if (!zyntraPlayer.hasGroupPermission(Group.ADMIN)) {
                player.kickPlayer("§cA sala cujo o nome é §e" + Core.getServerName() + " §cé inexistente.");
                return;
            }
        }

        if (room.getPlayers().size() >= room.getMaxPlayers()) {
            player.kickPlayer("§cA sala atingiu o número maximo de jogadores.");
        }

        matchPlayer.setRoom(Leading.getInstance().getRoom());
        if (room != null) {
            room.add(player);
            Leading.getInstance().getScheduleScoreboard().handleScoreboard(player);
        }

        matchPlayer.setStatistics(Core.getStatisticsController().loadStatistics(player.getUniqueId(), StatisticsType.SW_SOLO,
                GameStatistics.class));

        list.forEach(clazz -> {
            if (clazz != Kit.class && Kit.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    if (constructor != null) {
                        Kit kit = (Kit) constructor.newInstance();
                        matchPlayer.getKits().add(kit.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        list2.forEach(clazz -> {
            if (clazz != Perks.class && Perks.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    if (constructor != null) {
                        Perks kit = (Perks) constructor.newInstance();
                        matchPlayer.getPerks().add(kit.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        matchPlayer.setProtect(true);
        event.setJoinMessage(null);
    }
}
