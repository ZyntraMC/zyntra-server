package mc.zyntra.room;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.title.TitleAPI;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.statistics.types.GameStatistics;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.match.inventory.sub.PerksSelectorInventory;
import mc.zyntra.match.perks.Perks;
import mc.zyntra.stage.MatchStage;
import mc.zyntra.match.event.player.defeat.MatchPlayerDefeatEvent;
import mc.zyntra.util.Platform;
import mc.zyntra.Leading;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.match.kit.Kit;
import mc.zyntra.match.event.player.win.MatchPlayerWinEvent;
import mc.zyntra.match.inventory.KitSelectorInventory;
import mc.zyntra.match.inventory.SpectatorInventory;
import mc.zyntra.match.listener.PlayerLootListener;
import mc.zyntra.room.service.type.RoomSubType;
import mc.zyntra.room.service.type.RoomType;
import mc.zyntra.room.parser.CageParser;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

import java.util.*;


@Getter @Setter
public class Room {
    private String name;
    private String map;

    private Location lobby;

    private RoomType type;
    private RoomSubType subType;
    private MatchStage stage;

    private int maxPlayers;
    private int minPlayers;

    private int time;

    private List<Location> spawns = new ArrayList<>();
    private HashSet<CageParser> cages = new HashSet<>();

    private List<Block> chests;
    private List<Block> fChests;

    private List<Chest> feastChest;

    private Set<Chest> opened;

    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();

    Map<String, List<Player>> teams = new HashMap<>();

    private Player winner;

    private int id = 0;

    public Room(String name, String map, Location lobby) {
        this.name = name;
        this.map = map;
        this.lobby = lobby;
        setStage(MatchStage.ESPERANDO);
    }

    public void start() {
        Leading.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Leading.getInstance(), new Runnable() {
            @Override
            public void run() {
                switch (stage) {
                    case ESPERANDO:
                        if (players.size() >= minPlayers && players.size() < maxPlayers) {
                            time = 30;
                            setStage(MatchStage.INICIANDO);
                            players.forEach(player -> {
                                player.playSound(player.getLocation(), Sound.NOTE_PLING, 5.0f, 5.0f);
                                TitleAPI.setTitle(player, "§6§lSKY WARS", "§eIniciando em §6" + time + " §esegundos!", 1, 20, 1);
                                player.sendMessage("§aO jogo vai começar em §f" + time + "§a segundos!");
                            });
                        }

                        if (players.size() >= 8 && players.size() < maxPlayers) {
                            time = 15;
                            players.forEach(player -> {
                                player.playSound(player.getLocation(), Sound.NOTE_PLING, 5.0f, 5.0f);
                                TitleAPI.setTitle(player, "§6§lSKY WARS", "§eIniciando em §6" + time + " §esegundos!", 1, 20, 1);
                                player.sendMessage("§aO jogo vai começar em §f" + time + "§a segundos!");
                            });
                        }

                        if (players.size() == maxPlayers) {
                            time = 10;
                            players.forEach(player -> {
                                player.playSound(player.getLocation(), Sound.NOTE_PLING, 5.0f, 5.0f);
                                TitleAPI.setTitle(player, "§6§lSKY WARS", "§eIniciando em §6" + time + " §esegundos!", 1, 20, 1);
                                player.sendMessage("§aO jogo vai começar em §f" + time + "§a segundos!");
                            });
                        }
                        break;
                    case INICIANDO:
                        time--;
                        if (players.size() < minPlayers) {
                            players.forEach(player -> {
                                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 3.0F, 3.0F);
                                player.sendMessage("§cNão existem jogadores o suficiente para iniciar a partida.");
                            });
                            setStage(MatchStage.ESPERANDO);
                        }

                        if (time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                            players.forEach(player -> {
                                player.playSound(player.getLocation(), Sound.NOTE_PLING, 5.0f, 5.0f);
                                TitleAPI.setTitle(player, "§6§lSKY WARS", "§eIniciando em §6" + time + " §esegundos!", 0, 30, 0, true);
                                player.sendMessage("§aO jogo vai começar em §f" + time + "§a segundos!");
                            });
                        }

                        if (time == 0) {
                            players.forEach(player -> {
                                player.playSound(player.getLocation(), Sound.LEVEL_UP, 5.0f, 5.0f);
                                TitleAPI.setTitle(player, "§6§lSKY WARS", "§eComeçou!", 3, 10, 3);
                            });
                            setStage(MatchStage.EM_JOGO);
                            getCages().forEach(CageParser::clearBlocks);
                            atribbute(players);
                            players.forEach(player -> {
                                if (player != null && player.isOnline()) {
                                    player.closeInventory();
                                    player.getInventory().clear();
                                    player.setGameMode(GameMode.SURVIVAL);
                                    MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
                                    Kit kit = Platform.getKitLoader().getAbility(matchPlayer.getKit());
                                    if (kit != null) {
                                        kit.applyItems(player);
                                    }
                                    Perks perks = Platform.getPerkLoader().getAbility(matchPlayer.getPerk());
                                    if (perks != null) {
                                        perks.applyItems(player);
                                    }
                                }
                            });
                            time = 600;
                        }
                        break;
                    case EM_JOGO:
                        time--;

                        if (time == 596) {
                            players.forEach(p -> {
                                p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.5f, 1.5f);
                                p.sendMessage(" ");
                                p.sendMessage("§c§lAVISO:");
                                p.sendMessage(" §c- §cA aliança com outros jogadores é proibida e resultará em §c§lpunição§c.");
                                p.sendMessage(" §c- §cO uso de §c§lbugs §ce §c§ltrapaças §c é proibido e resultará em §c§lpunição§c.");
                                p.sendMessage(" ");
                            });
                        }

                        if (time == 510) {
                            players.forEach(winner -> {
                                winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                                //TitleAPI.setTitle(winner, "", "§aOs baús foram preenchidos!", 1, 10, 1);
                                winner.sendMessage("§aTodos os baús foram preenchidos.");
                                PlayerLootListener.getOpened().clear();
                            });
                        }

                        if (time == 330) {
                            players.forEach(winner -> {
                                winner.playSound(winner.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                                //TitleAPI.setTitle(winner, "", "§eOs baús foram recarregados!", 1, 10, 1);
                                winner.sendMessage("§aTodos os baús foram preenchidos.");
                                PlayerLootListener.getOpened().clear();
                            });
                        }

                        if (players.size() == 1) {
                            time = 15;
                            players.forEach(winner -> {
                                MatchPlayer matchPlayer = Platform.getPlayerLoader().get(winner);
                                //win(winner);
                                check();
                                //Bukkit.getPluginManager().callEvent(new MatchPlayerWinEvent(Leading.getInstance().getRoom(), winner));
                               // Bukkit.getPluginManager().callEvent(new MatchPlayerWinEvent(matchPlayer.getRoom(), winner));
                                winner.playSound(winner.getLocation(), Sound.FIREWORK_LAUNCH, 20, 20);
                            });


                            spectators.forEach(death -> {
                                //defeat(death);
                                check();
                                //Bukkit.getPluginManager().callEvent(new MatchPlayerWinEvent(Leading.getInstance().getRoom(), death));
                                death.playSound(death.getLocation(), Sound.FIREWORK_LAUNCH, 20, 20);
                            });
                            setStage(MatchStage.ENCERRANDO);
                        }
                        break;
                    case ENCERRANDO:
                        time--;

                        if (time == 5) {
                            Leading.setStarted(false);
                            players.forEach(winners -> BukkitMain.getInstance().searchServer(winners, ServerType.SW_SOLO));
                            spectators.forEach(winners -> BukkitMain.getInstance().searchServer(winners, ServerType.SW_SOLO));
                            setStage(MatchStage.REINICIANDO);
                        }
                        break;
                    case REINICIANDO:
                        players.forEach(winners -> BukkitMain.getInstance().searchServer(winners, ServerType.LOBBY));
                        spectators.forEach(winners -> BukkitMain.getInstance().searchServer(winners, ServerType.LOBBY));
                        Leading.getInstance().getServer().shutdown();
                        break;
                }
            }
        }, 0L, 20L);
    }

    public void addSpawn(Location location) {
        spawns.add(location);
    }
    public void addCage(CageParser cage) {
        cages.add(cage);
    }

    public void add(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.SURVIVAL);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);

        players.add(player);

        players.forEach(players -> {
            for (Player spec : spectators) {
                players.hidePlayer(spec);
            }
        });
        try {
            Location spawnLocation = spawns.get(id);
            CageParser cage = new CageParser(spawnLocation, player);
            addCage(cage);
            player.teleport(spawnLocation.clone());
            id += 1;
        } catch (IndexOutOfBoundsException ex) {
            Leading.getInstance().getLogger().severe("Sem spawns para enviar os jogadores na sala (" + getName() + ")");
        }

        player.getInventory().setItem(0, new ItemBuilder(Material.CHEST)
                .setName("§aSeletor de kits")
                .build(
                        (player1, item, action, clicked) -> {
                            new KitSelectorInventory(player, 1).open(player);
                            return false;
                        }
                ));

        player.getInventory().setItem(1, new ItemBuilder(Material.NETHER_STAR)
                .setName("§aSeletor de habilidades")
                .build(
                        (player1, item, action, clicked) -> {
                            new PerksSelectorInventory(player, 1).open(player);
                            return false;
                        }
                ));

        player.getInventory().setItem(8, new ItemBuilder(Material.BED)
                .setName("§cVoltar")
                .setLore("§7Seja enviado de volta ao lobbie!")
                .build(
                        (player1, item, action, clicked) -> {
                            players.remove(player);
                            BukkitMain.getInstance().searchServer(player1, ServerType.LOBBY);
                            return false;
                        }
                ));

        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        players.forEach(p -> {
            p.sendMessage("§7" + zyntraPlayer.getTagHandler().getSelectedTag().getColor() + player.getName() + "§e entrou na partida! §a(" + players.size() + "/" + maxPlayers + ")");
        });
    }

    public void remove(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setGameMode(GameMode.ADVENTURE);

        players.remove(player);

        if (getStage() == MatchStage.ESPERANDO || getStage() == MatchStage.INICIANDO) {
            players.forEach(p -> {
                ZyntraPlayer w = Core.getAccountController().get(player.getUniqueId());
                p.sendMessage(w.getTag().getColor() + player.getName() + " §esaiu na partida. §a(" + players.size() + "/" + maxPlayers + ")");
            });
        } else if (getStage() == MatchStage.EM_JOGO) {
            players.forEach(p -> {
                ZyntraPlayer targetPlayer = Core.getAccountController().get(player.getUniqueId());
                Bukkit.broadcastMessage("§e" + targetplayer.getTagHandler().getSelectedTag().getColor() + player.getName() + " §cabandonou a partida.");
            });
        }
    }

    public void addSpec(Player player) {
        player.setHealth(1.5D);
        player.setMaxHealth(1.5D);
        player.setFireTicks(0);
        player.setFoodLevel(20);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);

        player.setPlayerListName("§8[ESPECTADOR] " + player.getName());

        TitleAPI.setTitle(player, "§4§lDERROTA!", "§eNão foi dessa vez, tente novamente!", 1, 15, 1);

        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS)
                .setName("§cEspectar jogadores")
                .setLore("§7Especte jogadores que ainda estão vivos!")
                .build(
                        (player1, item, action, clicked) -> {
                            new SpectatorInventory().open(player);
                            return false;
                        }
                ));

        player.getInventory().setItem(7, new ItemBuilder(Material.PAPER)
                .setName("§aProcurar nova partida")
                .setLore("§7Procure uma nova partida para jogar!")
                .build(
                        (player1, item, action, clicked) -> {
                            spectators.remove(player);
                            BukkitMain.getInstance().searchServer(player, ServerType.SW_SOLO);
                            return false;
                        }
                ));

        player.getInventory().setItem(8, new ItemBuilder(Material.BED)
                .setName("§cVoltar")
                .setLore("§7Seja enviado de volta ao lobbie!")
                .build(
                        (player1, item, action, clicked) -> {
                            spectators.remove(player);
                            BukkitMain.getInstance().searchServer(player, ServerType.LOBBY);
                            return false;
                        }
                ));

        spectators.add(player);
    }

    public void removeSpec(Player player) {
        player.setHealth(20.0D);
        player.setFireTicks(0);
        player.setFoodLevel(20);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        spectators.remove(player);
    }

    public void atribbute(List<Player> players) {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
        int index = 0;

        for (Player player : players) {
            String teamName = letters[index];
            player.setPlayerListName("§c[" + teamName + "] " + player.getName());
            teams.computeIfAbsent(teamName, k -> new ArrayList<>()).add(player);

            index = (index + 1) % letters.length;
        }
    }

    public void win(Player player) {
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        GameStatistics stats = matchPlayer.getStatistics();
        Random random = new Random();

        int coin = random.nextInt(81) + 40;

        stats.addWin();
        stats.addWinstreak();
        stats.addCoins(coin);

        player.setHealth(20.0D);
        player.setExp(0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setExhaustion(0);

        EnderDragon enderDragon = player.getWorld().spawn(player.getLocation(), EnderDragon.class);
        enderDragon.setCustomNameVisible(false);
        enderDragon.setHealth(2048D);
        enderDragon.setMaxHealth(2048D);
        enderDragon.setNoDamageTicks(Integer.MAX_VALUE);
        enderDragon.setPassenger(player);

        TextComponent msg = new TextComponent("§eDeseja jogar novamente? ");
        TextComponent component = new TextComponent("§b§lCLIQUE AQUI");
        TextComponent fim = new TextComponent("§r§e!");
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playagain"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.YELLOW + "Clique para jogar novamente!")}));
        msg.addExtra(component);
        msg.addExtra(fim);
        player.sendMessage("");
        player.sendMessage("§aVocê venceu a partida!");
        player.spigot().sendMessage(msg);
        player.sendMessage("");
        TitleAPI.setTitle(player, "§6§lVITÓRIA!", "§eVocê venceu essa partida!", 1, 15, 1);
    }

    public void defeat(Player player) {
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(player.getUniqueId());
        GameStatistics stats = matchPlayer.getStatistics();
        Random random = new Random();

        int coin = random.nextInt(16) + 5;

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

    public boolean check() {
        if (getStage() == MatchStage.EM_JOGO) {
            if (players.size() > 1) {
                return false;
            }
            if (players.size() == 0) {
                Bukkit.shutdown();
                return false;
            }

            if (players.size() == 1 && winner == null) {
                winner = Bukkit.getPlayer(players.get(0).getUniqueId());
                Bukkit.getPluginManager().callEvent(new MatchPlayerWinEvent(Leading.getInstance().getRoom(), winner));
                winner.setAllowFlight(true);
                winner.setFlying(true);
                for (Player player : spectators) {
                    Bukkit.getPluginManager().callEvent(new MatchPlayerDefeatEvent(Leading.getInstance().getRoom(), player));
                }
                return true;
            }
        }
        return false;
    }
}
