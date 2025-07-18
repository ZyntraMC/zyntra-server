package mc.zyntra.player.listeners;

import mc.zyntra.Flags;
import mc.zyntra.Secure;
import mc.zyntra.general.Constant;
import mc.zyntra.general.account.config.Language;
import mc.zyntra.player.event.PlayerCompletedRegistrationEvent;
import mc.zyntra.player.event.PlayerLoggedEvent;
import mc.zyntra.player.generator.User;
import mc.zyntra.player.inventories.CaptchaInventory;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.title.TitleAPI;
import mc.zyntra.bukkit.event.update.UpdateEvent;
import mc.zyntra.general.server.ServerType;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.NetworkManager;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(player.getUniqueId());
        if (zyntraPlayer.getAccountType().equals(ZyntraPlayer.AccountType.PREMIUM) && !zyntraPlayer.hasGroupPermission(Group.ADMIN)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cSomente jogadores que possuem a conta do tipo 'CRACKED' podem acessar esta sala.");
            return;
        }
        User user = new User(event.getPlayer().getUniqueId());
        user.setPlayer(player);
        Secure.getInstance().getUserParser().create(user);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Secure.getInstance().getUserParser().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = Secure.getInstance().getUserParser().get(player.getUniqueId());

        player.setExp(0);
        player.setFireTicks(0);
        player.setMaxHealth(1.5D);
        player.setHealth(1.5D);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setWalkSpeed(0.25f);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.teleport(Secure.getInstance().getLocationFromConfig("spawn"));

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.hidePlayer(player);
            player.hidePlayer(p);
        });

        TitleAPI.clearTitle(player);

        //user.initializeScoreboard();

        ZyntraPlayer aPlayer = Core.getAccountController().get(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(Secure.getInstance(), () -> new CaptchaInventory(user).open(player), 20);

        player.sendMessage(aPlayer.getLanguage() == Language.PT_BR ? Flags.CAPTCHA.getPt_br()
                : Flags.CAPTCHA.getEn_us());
    }

    @EventHandler
    public void onPlayerCompletedRegistration(PlayerCompletedRegistrationEvent event) {
        Player player = event.getPlayer();
        User user = Secure.getInstance().getUserParser().get(player.getUniqueId());

        ZyntraPlayer aPlayer = Core.getAccountController().get(player.getUniqueId());

        TitleAPI.setTitle(player, Constant.FORMATTED_NAME, (aPlayer.getLanguage() == Language.PT_BR ? "§f§fYAY! Enviando..." : "§fYAY! Sending..."));

        player.sendMessage(aPlayer.getLanguage() == Language.PT_BR ? Flags.REGISTERED.getPt_br() : Flags.REGISTERED.getEn_us());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 100f, 100f);
    }

    @EventHandler
    public void onPlayerLogged(PlayerLoggedEvent event) {
        Player player = event.getPlayer();
        User user = Secure.getInstance().getUserParser().get(player.getUniqueId());


        ZyntraPlayer aPlayer = Core.getAccountController().get(player.getUniqueId());
        TitleAPI.setTitle(player, Constant.FORMATTED_NAME, (aPlayer.getLanguage() == Language.PT_BR ? "§f§fYAY! Enviando..." : "§fYAY! Sending..."));

        player.sendMessage(aPlayer.getLanguage() == Language.PT_BR ? Flags.LOGGED.getPt_br() : Flags.LOGGED.getEn_us());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 100f, 100f);
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateEvent.UpdateType.SECOND)
            return;

        for (User user : Secure.getInstance().getUserParser().list()) {
            ZyntraPlayer zyntraPlayer = Core.getAccountController().get(user.getUniqueId());

            if (zyntraPlayer.isLogged()) {
                BukkitMain.getInstance().searchServer(user.getPlayer(), ServerType.LOBBY);
                continue;
            }

            if (user.getTimeLeftCaptcha() <= 0) {
                user.getPlayer().kickPlayer("§cVocê demorou muito para se autenticar.");
                return;
            }

            if (user.getTimeLeftLogin() <= 0) {
                user.getPlayer().kickPlayer("§cVocê demorou muito para se autenticar.");
                return;
            }

            if (user.isCompletedCaptcha())
                user.setTimeLeftLogin(user.getTimeLeftLogin() - 1);
            else
                user.setTimeLeftCaptcha(user.getTimeLeftCaptcha() - 1);

            if (!user.isCompletedCaptcha())
                continue;

            user.getPlayer().sendMessage(zyntraPlayer.getPassword() == null
                    ? (zyntraPlayer.getLanguage() == Language.PT_BR ? Flags.REGISTER.getPt_br() : Flags.REGISTER.getEn_us())
                    : (zyntraPlayer.getLanguage() == Language.PT_BR ? Flags.LOGIN.getPt_br() : Flags.LOGIN.getEn_us()));
        }
    }

    private static int getPlayerProtocolVersion(Player player) throws NoSuchFieldException, IllegalAccessException {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        PlayerConnection connection = entityPlayer.playerConnection;
        NetworkManager networkManager = connection.networkManager;
        Class networkManagerClass = networkManager.getClass();
        Field protocolVersionField = networkManagerClass.getDeclaredField("protocolVersion");
        protocolVersionField.setAccessible(true);
        return protocolVersionField.getInt(networkManager);
    }
}
