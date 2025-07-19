package mc.zyntra.lobby.inventory;

import mc.zyntra.general.Constant;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.general.Core;
import mc.zyntra.general.server.ServerType;
import org.bukkit.Material;

public class GamesInventory extends MenuInventory {

    public GamesInventory() {
        super("Navegador", 4);

        setItem(10, new ItemBuilder(Material.NETHER_STAR)
                .setName("§aLobby Principal")
                .setLore("§eClique para entrar!")
                .build(), (p, inv, type, stack, slot) -> BukkitMain.getInstance().searchServer(p, ServerType.LOBBY));
        setItem(19, new ItemBuilder(Material.BOOK)
                .setName("§eVisite nossa loja")
                .setLore(
                        "§7Impulse sua jornada com Ranks, Cash,",
                        "§7benefícios exclusivos e muito mais",
                        "§7na nossa loja!",
                        "",
                        "§7Acesse: §bhttps://" + Constant.STORE,
                        ""
                )
                .build(), (p, inv, type, stack, slot) -> {
            TextComponent msg = new TextComponent("§aClique ");
            TextComponent component = new TextComponent("§b§lAQUI");
            TextComponent fim = new TextComponent(" §r§apara acessar nossa loja!");

            msg.addExtra(component);
            msg.addExtra(fim);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + Constant.STORE));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.GRAY + "https://mc-zyntra.com/store")}));

            p.closeInventory();
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 5.0f, 5.0f);
            p.sendMessage("");
            p.spigot().sendMessage(msg);
            p.sendMessage("");
        });

        int onlineCount = Core.getDataServer().getOnlineCount(ServerType.LOBBY_BEDWARS) +
                Core.getDataServer().getOnlineCount(ServerType.BW_SOLO) +
                Core.getDataServer().getOnlineCount(ServerType.BW_DUO) +
                Core.getDataServer().getOnlineCount(ServerType.BW_MEGA);

        setItem(12, new ItemBuilder(Material.BED)
                .setName("§aBed Wars")
                .setLore("",
                        "§7Proteja sua cama, destrua as camas",
                        "§7inimigas e seja o último de pé!",
                        "",
                        "§e» Clique para jogar!",
                        "§7" + onlineCount + " Jogando agora!",
                        ""
                )
                .build(), (p, inv, type, stack, slot) ->
                BukkitMain.getInstance().searchServer(p, ServerType.LOBBY_BEDWARS));

        onlineCount = Core.getDataServer().getOnlineCount(ServerType.LOBBY_SKYWARS) +
                Core.getDataServer().getOnlineCount(ServerType.SW_SOLO) +
                Core.getDataServer().getOnlineCount(ServerType.SW_DUO) +
                Core.getDataServer().getOnlineCount(ServerType.SW_MEGA);

        setItem(13, new ItemBuilder(Material.BOW)
                .setName("§aSky Wars")
                .setLore(
                        "",
                        "§7Saque os baús, derrote os inimigos",
                        "§7e seja o último sobrevivente na ilha!",
                        "",
                        "§e» Clique para jogar!",
                        "§7" + onlineCount + " Jogando agora!",
                        ""
                )
                .build(), (p, inv, type, stack, slot) ->
                BukkitMain.getInstance().searchServer(p, ServerType.SW_SOLO));

        onlineCount = Core.getDataServer().getOnlineCount(ServerType.LOBBY_PVP) +
                Core.getDataServer().getOnlineCount(ServerType.PVP_ARENA) + Core.getDataServer().getOnlineCount(ServerType.PVP_FPS) + Core.getDataServer().getOnlineCount(ServerType.PVP_FPS);

        setItem(14, new ItemBuilder(Material.IRON_CHESTPLATE)
                .setName("§aPvP")
                .setLore(
                        "",
                        "§7Afie sua espada, entre na batalha",
                        "§7e vença todos no combate!",
                        "",
                        "§e» Clique para jogar!",
                        "§7" + onlineCount + " Jogando agora!",
                        ""
                )
                .build(), (p, inv, type, stack, slot) -> BukkitMain.getInstance().searchServer(p, ServerType.LOBBY_PVP));
    }
}
