package mc.zyntra.lobby.generator.scheduler;

import mc.zyntra.General;
import mc.zyntra.Main;
import mc.zyntra.bukkit.api.cooldown.CooldownAPI;
import mc.zyntra.bukkit.api.cooldown.types.Cooldown;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.vanish.VanishManager;
import mc.zyntra.bukkit.player.inventories.AccountInventory;
import mc.zyntra.cMain;
import mc.zyntra.collectibles.inventory.CollectiblesInventory;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.utils.string.WaveAnimation;
import mc.zyntra.lobby.inventory.GamesInventory;
import mc.zyntra.lobby.inventory.LobbiesInventory;
import mc.zyntra.lobby.generator.user.User;
import mc.zyntra.bukkit.api.nametag.NametagController;
import mc.zyntra.bukkit.api.scoreboard.Scoreboard;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LobbyGenerator implements General, Listener {

    private static final String COOLDOWN_VISIBILITY_KEY = "players-visibility";

    private static final WaveAnimation SB_1 = new WaveAnimation(Constant.SERVER_NAME.toUpperCase(),
            Constant.FIRST_COLOR + "§l", Constant.SECOND_COLOR + "§l", "§f§l");

    private static String SCOREBOARD_TITLE;

    private static final String[] VISIBILITY_STATES = {"ALL", "FRIENDS", "NONE"};

    // Mapa para armazenar o estado de visibilidade por jogador
    private final Map<UUID, Integer> visibilityStates = new HashMap<>();

    @Override
    public void handleInventory(Player player) {
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        player.getInventory().setItem(4, new ItemBuilder(Material.COMPASS)
                .setName("§aModos de jogo")
                .setLore("§eClique para acessar os modos de jogos!")
                .build((p, item, action, clicked) -> {
                    new GamesInventory().open(player);
                    return false;
                }));

        player.getInventory().setItem(1, new ItemBuilder(Material.SKULL_ITEM)
                .setName("§aSeu perfil")
                .setLore("§eClique para acessar seu perfil!")
                .setDurability(3)
                .setPlayerHead(zyntraPlayer.getName())
                .build((p, item, action, clicked) -> {
                    new AccountInventory(zyntraPlayer).open(player);
                    return false;
                }));

        player.getInventory().setItem(0, new ItemBuilder(Material.RAW_FISH)
                .setName("§aCosméticos")
                .setLore("§eClique para ver os cosméticos!")
                .setDurability(3)
                .build((p, item, action, clicked) -> {
                    new CollectiblesInventory(zyntraPlayer, cMain.Collectible.MAIN).open(player);
                    return false;
                }));

        updateVisibilityItem(player, zyntraPlayer);

        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR)
                .setName("§aLobby's")
                .setLore("§eClique para selecionar um lobby!")
                .build((p, item, action, clicked) -> {
                    new LobbiesInventory().open(player);
                    return false;
                }));
    }

    private void updateVisibilityItem(Player player, ZyntraPlayer zyntraPlayer) {
        int stateIndex = visibilityStates.getOrDefault(player.getUniqueId(), 0);
        String state = VISIBILITY_STATES[stateIndex];

        Material material = Material.INK_SACK;
        short durability;
        String displayName;

        switch (state) {
            case "ALL":
                durability = 10;
                displayName = "§fVisibilidade: §aTodos";
                break;
            case "FRIENDS":
                durability = 11;
                displayName = "§fVisibilidade: §bApenas Amigos";
                break;
            default:
                durability = 8;
                displayName = "§fVisibilidade: §cNinguém";
                break;
        }

        player.getInventory().setItem(7, new ItemBuilder(material)
                .setDurability(durability)
                .setName(displayName)
                .setLore("§7Alterne a visibilidade de players.")
                .build((p, item, action, clicked) -> toggleVisibility(p, zyntraPlayer)));
    }

    private boolean toggleVisibility(Player player, ZyntraPlayer zyntraPlayer) {
        if (CooldownAPI.getInstance().hasCooldown(player, COOLDOWN_VISIBILITY_KEY)) {
            Cooldown cooldown = CooldownAPI.getInstance().getCooldown(player.getUniqueId(), COOLDOWN_VISIBILITY_KEY);
            player.sendMessage("§cAguarde " + Constant.DECIMAL_FORMAT.format(cooldown.getRemaining()) + "s para fazer isso novamente.");
            return false;
        }

        CooldownAPI.getInstance().addCooldown(player.getUniqueId(), COOLDOWN_VISIBILITY_KEY, 3);

        int currentIndex = visibilityStates.getOrDefault(player.getUniqueId(), 0);
        int nextIndex = (currentIndex + 1) % VISIBILITY_STATES.length;
        visibilityStates.put(player.getUniqueId(), nextIndex);
        String nextState = VISIBILITY_STATES[nextIndex];

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (VanishManager.hasVanish(online)) {
                player.hidePlayer(online);
                continue;
            }

            switch (nextState) {
                case "ALL":
                    player.showPlayer(online);
                    break;
                case "FRIENDS":
                    if (zyntraPlayer.getFriends().contains(online.getUniqueId())) {
                        player.showPlayer(online);
                    } else {
                        player.hidePlayer(online);
                    }
                    break;
                default:
                    player.hidePlayer(online);
                    break;
            }
        }

        switch (nextState) {
            case "ALL":
                player.sendMessage("§aAgora você vê todos os jogadores.");
                break;
            case "FRIENDS":
                player.sendMessage("§bAgora você vê apenas amigos.");
                break;
            default:
                player.sendMessage("§cAgora você não vê mais ninguém.");
                break;
        }

        updateVisibilityItem(player, zyntraPlayer);

        return false;
    }

    @Override
    public void handleScoreboard(Player player) {
        User user = Main.getInstance().getUserParser().get(player.getUniqueId());
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());

        Scoreboard scoreboard = new Scoreboard(SCOREBOARD_TITLE);
        scoreboard.add(7, "");
        scoreboard.add(6, "§fCargo: " + zyntraPlayer.getPrimaryGroupData().getGroup().getColoredName());
        scoreboard.add(5, "");
        scoreboard.add(4, "§fLobby: §a" + (Core.getServerName().contains("-") ? Core.getServerName().split("-0")[1] : "N/A"));
        scoreboard.add(3, "§fJogadores: §a" + Core.getDataServer().getServer("proxy").getOnlineCount());
        scoreboard.add(2, "");
        scoreboard.add(1, "§e§o" + Constant.SERVER_ADDRESS);
        user.setScoreboard(scoreboard);
        player.setScoreboard(scoreboard.getScoreboard());

        NametagController.getInstance().update(player);
    }

    @Override
    public void updateScoreboard() {
        SCOREBOARD_TITLE = SB_1.next();

        for (User user : Main.getInstance().getUserParser().list()) {
            if (user == null || user.getUniqueId() == null) continue;

            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(user.getUniqueId());
            if (zyntraPlayer == null) continue;

            Scoreboard scoreboard = user.getScoreboard();
            if (scoreboard == null) continue;

            scoreboard.setDisplayName(SCOREBOARD_TITLE);
            scoreboard.add(6, "§fCargo: " + zyntraPlayer.getPrimaryGroupData().getGroup().getColoredName());
            scoreboard.add(4, "§fLobby: §a" + (Core.getServerName().contains("-") ? Core.getServerName().split("-0")[1] : "N/A"));
            scoreboard.add(3, "§fJogadores: §a" + Core.getDataServer().getServer("proxy").getOnlineCount());
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getCurrentTick() % 2 == 0) {
            updateScoreboard();
        }
    }
}
