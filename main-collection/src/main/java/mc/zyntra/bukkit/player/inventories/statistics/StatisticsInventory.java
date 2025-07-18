package mc.zyntra.bukkit.player.inventories.statistics;

import mc.zyntra.bukkit.player.inventories.AccountInventory;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.statistics.StatisticsType;
import mc.zyntra.general.account.statistics.types.GameStatistics;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

public class StatisticsInventory extends MenuInventory {

    public StatisticsInventory(ZyntraPlayer opener, ZyntraPlayer target, StatisticsMenuType menuType, boolean cameByCommand) {
        super(target.equals(opener) ? "Suas estatísticas" : "Estatísticas de " + target.getName(), 4);

        switch (menuType) {
            case MAIN: {
                setItem(11, new ItemBuilder(Material.GRASS)
                                .setName("§aSky Wars")
                                .setLore("§7Clique para ver detalhes.").build(),
                        (p, inv, type, stack, slot) -> new StatisticsInventory(opener, target, StatisticsMenuType.SKYWARS, cameByCommand).open(p));

                setItem(13, new ItemBuilder(Material.BED)
                                .setName("§aBed Wars")
                                .setLore("§7Clique para ver detalhes.").build(),
                        (p, inv, type, stack, slot) -> new StatisticsInventory(opener, target, StatisticsMenuType.BEDWARS, cameByCommand).open(p));


                setItem(15, new ItemBuilder(Material.IRON_CHESTPLATE)
                                .setName("§aPvP")
                                .setLore("§7Clique para ver detalhes.").build(),
                        (p, inv, type, stack, slot) -> new StatisticsInventory(opener, target, StatisticsMenuType.PVP, cameByCommand).open(p));


                if (!cameByCommand) {
                    setItem(31, new ItemBuilder(Material.ARROW)
                                    .setName("§cVoltar").build(),
                            (p, inv, type, stack, slot) -> new AccountInventory(opener).open(p));
                }
                break;
            }

            case SKYWARS: {
                setTitle(target.equals(opener) ? "Suas estatísticas : Sky Wars" : "Estatísticas de " + target.getName() + " : Sky Wars");
                setItem(11, new ItemBuilder(Material.GRASS)
                        .setName("§a§l➜ §aSkywars (Solo)")
                        .setLore(
                                "§7Partidas jogadas: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getMatchesPlayed(),
                                "§7Vitórias: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getWins(),
                                "§7Derrotas: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getDeaths(),
                                "",
                                "§7WinStreak atual: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getWinstreak(),
                                "§7Melhor WinStreak: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getBestWinstreak(),
                                "",
                                "§7Abates: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getKills(),
                                "§7Mortes: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_SOLO)).getDeaths(),
                                "",
                                "§7Tempo total jogado: §a" + time((int) System.currentTimeMillis())
                        )
                        .build());

                setItem(13, new ItemBuilder(Material.GRASS).setAmount(2)
                        .setName("§a§l➜ §aSkywars (Dupla)")
                        .setLore(
                                "§7Partidas jogadas: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getMatchesPlayed(),
                                "§7Vitórias: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getWins(),
                                "§7Derrotas: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getDeaths(),
                                "",
                                "§7WinStreak atual: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getWinstreak(),
                                "§7Melhor WinStreak: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getBestWinstreak(),
                                "",
                                "§7Abates: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getKills(),
                                "§7Assistências: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getAssists(),
                                "§7Mortes: §a" + ((GameStatistics) Core.getDataStatus().load(opener.getUniqueId(), StatisticsType.SW_DOUBLES)).getDeaths(),
                                "",
                                "§7Tempo total jogado: §a" + time((int) System.currentTimeMillis())
                        )
                        .build());

                setItem(31, new ItemBuilder(Material.ARROW)
                                .setName("§cVoltar").build(),
                        (p, inv, type, stack, slot) -> new StatisticsInventory(opener, target, StatisticsMenuType.MAIN, cameByCommand).open(p));
                break;
            }

            case BEDWARS: {
                setTitle(target.equals(opener) ? "Suas estatísticas : Bed Wars" : "Estatísticas de " + target.getName() + " : Bed Wars");
                setItem(13, new ItemBuilder(Material.WEB)
                        .setName("§cVazio! cri... cri... cri...")
                        .setLore("§7Ainda não há nada por aqui...", "§7O que será que está por vir?!")
                        .build());

                setItem(31, new ItemBuilder(Material.ARROW)
                                .setName("§cVoltar").build(),
                        (p, inv, type, stack, slot) -> new StatisticsInventory(opener, target, StatisticsMenuType.MAIN, cameByCommand).open(p));
                break;
            }

            case PVP: {
                setTitle(target.equals(opener) ? "Suas estatísticas : PvP" : "Estatísticas de " + target.getName() + " : PvP");
                setItem(13, new ItemBuilder(Material.WEB)
                        .setName("§cVazio! cri... cri... cri...")
                        .setLore("§7Ainda não há nada por aqui...", "§7O que será que está por vir?!")
                        .build());

                setItem(31, new ItemBuilder(Material.ARROW)
                                .setName("§cVoltar").build(),
                        (p, inv, type, stack, slot) -> new StatisticsInventory(opener, target, StatisticsMenuType.MAIN, cameByCommand).open(p));
                break;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public enum StatisticsMenuType {
        MAIN, SKYWARS, BEDWARS, PVP;
    }

    private String time(int time) {
        return time / 60 + ":" + (time % 60 < 10 ? "0" : "") + time % 60;
    }
}
