package mc.zyntra.lobby.inventory.sub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.general.server.ServerConfiguration;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.utils.string.StringLoreUtils;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class SearchInventory extends MenuInventory {

    @Setter @Getter
    String mapLore;
    int maps = Core.getDataServer().getServers(ServerType.SW_SOLO).size();

    public static final Map<ZyntraPlayer, Server> returnServer = new HashMap<>();

    public SearchInventory(ZyntraPlayer opener, Server server) {
        super("Search", 3);

        int onlineCount;

        switch (server) {
            case SKYWARS_SOLO:
                setTitle("Skywars: Solo");
                setItem(11, new ItemBuilder(Material.FIREWORK)
                        .setName("§aJogo Rápido")
                        .setLore(
                                "",
                                " §7Entre na partida mais próxima",
                                " §7de começar!.",
                                "",
                                " §eClique para jogar!"
                        )
                        .build(), (p, inv, type, stack, slot) ->
                        BukkitMain.getInstance().searchServer(p, ServerType.SW_SOLO));

                setItem(15, new ItemBuilder(Material.MAP)
                        .setName((opener.hasGroupPermission(Group.GOLD) ? "§a" : "§c") + "Selecionar mapa")
                        .setLore(StringLoreUtils.formatForLore(
                                (!opener.hasGroupPermission(Group.GOLD) ?
                                        "\n\n §7Requer plano §eOuro §7ou superior\n §7para selecionar mapas.\n\n §eClique para entrar em nossa loja." :
                                        "\n\n §7Selecione o seu mapa favorito!\n §7Estão disponíveis: §6" + maps + "§7 mapas.\n\n §eClique para selecionar!"
                                )))
                        .build(), (p, inv, type, stack, slot) -> {
                    if(opener.hasGroupPermission(Group.GOLD)) {
                        new SearchInventory(opener, Server.MAPS).open(p);
                    } else {
                        p.sendMessage("§cPara usufruir desta função adquira uma posição §eOuro§c ou superior em §6mc-zyntra.net/store§c.");
                    }
                });
                break;
            case BEDWARS_SOLO:
                setItem(11, new ItemBuilder(Material.FIREWORK)
                        .setName("§aJogo Rápido")
                        .setLore(
                                "",
                                " §7Entre na partida mais próxima",
                                " §7de começar!.",
                                "",
                                " §eClique para jogar!",
                                ""
                        )
                        .build(), (p, inv, type, stack, slot) ->
                        BukkitMain.getInstance().searchServer(p, ServerType.BW_SOLO));
                break;
            case ESCONDE_ESCONDE:
                setItem(11, new ItemBuilder(Material.FIREWORK)
                        .setName("§aJogo Rápido")
                        .setLore(
                                "",
                                " §7Entre na partida mais próxima",
                                " §7de começar!.",
                                "",
                                " §eClique para jogar!",
                                ""
                        )
                        .build(), (p, inv, type, stack, slot) ->
                        BukkitMain.getInstance().searchServer(p, ServerType.ESCONDE_ESCONDE));
                break;
            case MAPS:
                int slot = 10;
                for (ServerConfiguration serverConfiguration : Core.getDataServer().getServers(ServerType.SW_SOLO)) {
                    setItem(slot, new ItemBuilder(Material.MAP)
                                    .setName("§a" + serverConfiguration.getMap())
                            .setLore("",
                                    " §7Existem §6" + serverConfiguration.getOnlineCount() + "§7 jogadores",
                                    " §7no mapa neste exato momento.",
                                    "",
                                    " §eClique para jogar no mapa!"
                            )
                                    .build(),
                            (p, inv, type, stack, slot1) -> {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Connect");
                                out.writeUTF(serverConfiguration.getName());
                                p.sendPluginMessage(BukkitMain.getInstance(), "BungeeCord", out.toByteArray());
                            });
                    slot++;
                }
                break;
        }
    }

    @Getter
    @NoArgsConstructor
    public enum Server {
        BEDWARS_SOLO, BEDWARS_DUPLA,
        SKYWARS_SOLO, SKYWARS_DUPLA,
        ESCONDE_ESCONDE,

        MAPS;
    }

    public String loreMap(ZyntraPlayer player) {
        if (player.hasGroupPermission(Group.GOLD)) {
            setMapLore("\n\n §7Requer plano §eOuro §7ou superior\n §7para selecionar mapas.\n\n §eClique para entrar em nossa loja.");
        }
        return null;
    }
}
