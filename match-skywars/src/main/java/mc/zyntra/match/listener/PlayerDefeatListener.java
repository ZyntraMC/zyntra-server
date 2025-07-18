package mc.zyntra.match.listener;

import mc.zyntra.bukkit.api.title.TitleAPI;
import mc.zyntra.general.account.statistics.types.GameStatistics;
import mc.zyntra.match.event.player.defeat.MatchPlayerDefeatEvent;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.util.Platform;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

public class PlayerDefeatListener implements Listener {

    @EventHandler
    public void onMatchPlayerDefeat(MatchPlayerDefeatEvent event) {
        Player player = event.getPlayer();
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        GameStatistics stats = matchPlayer.getStatistics();
        Random random = new Random();

        int coin = random.nextInt(16) + 5;

        stats.addDeath();
        stats.addMatch();
        stats.addCoins((matchPlayer.getKills() == 0 ? -5 : coin));

        TextComponent msg = new TextComponent("§eDeseja jogar novamente? ");
        TextComponent component = new TextComponent("§b§lCLIQUE AQUI");
        TextComponent fim = new TextComponent("§r§e!");
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playagain"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.YELLOW + "Clique para jogar novamente!")}));
        msg.addExtra(component);
        msg.addExtra(fim);
        player.sendMessage("");
        player.sendMessage("§cVocê morreu!");
        player.spigot().sendMessage(msg);
        player.sendMessage("");
        TitleAPI.setTitle(player, "§4§lDERROTA!", "§eNão foi dessa vez, tente novamente!", 1, 15, 1);
    }
}
