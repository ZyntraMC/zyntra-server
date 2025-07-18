package mc.zyntra.player.inventories;

import mc.zyntra.Flags;
import mc.zyntra.bukkit.api.title.TitleAPI;
import mc.zyntra.general.Constant;
import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.config.Language;
import mc.zyntra.player.generator.User;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Random;

public class CaptchaInventory extends MenuInventory {

    public CaptchaInventory(User user) {
        super("Captcha", 3);
        setReopenInventory(true);

        for (int i = 0; i < 3 * 9; i++) {
            setItem(i, new ItemBuilder(Material.SKULL_ITEM)
                            .setDurability(3)
                            .setPlayerHeadBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY1ZjNiYWUwZDIwM2JhMTZmZTFkYzNkMTMwN2E4NmE2MzhiZTkyNDQ3MWYyM2U4MmFiZDlkNzhmOGEzZmNhIn19fQ==")
                            .setName("§c")
                            .build(),
                    (p, inv, type, stack, slot) -> {
                        if (user.getRemainingAttemptsCaptcha() <= 0) {
                            user.getPlayer().kickPlayer("§cVocê errou todas as tentativas de captcha.");
                            return;
                        }

                        user.setRemainingAttemptsCaptcha(user.getRemainingAttemptsCaptcha() - 1);
                        user.getPlayer().sendMessage("§cVocê errou o captcha! Você possuí " + user.getRemainingAttemptsCaptcha() + " tentativas restantes.");
                        user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.NOTE_BASS_DRUM, 100f, 100f);

                        setReopenInventory(false);
                        new CaptchaInventory(user).open(user.getPlayer());
                    });
        }

        setItem(new Random().nextInt(3 * 9), new ItemBuilder(Material.SKULL_ITEM)
                        .setDurability(3)
                        .setPlayerHeadBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNlOWY0ZGJhZGRlMGY3MjdjNTgwM2Q3NWQ4YmIzNzhmYjlmY2I0YjYwZDMzYmVjMTkwOTJhM2EyZTdiMDdhOSJ9fX0=")
                        .setName("§a")
                        .build(),
                (p, inv, type, stack, slot) -> {
                    setReopenInventory(false);

                    user.setCompletedCaptcha(true);
                    user.getPlayer().closeInventory();
                    user.getPlayer().sendMessage("§aYAY!");
                    ZyntraPlayer aPlayer = Core.getAccountController().get(user.getPlayer().getUniqueId());
                    TitleAPI.setTitle(user.getPlayer(), Constant.FORMATTED_NAME, (aPlayer.getPassword() == null ? (aPlayer.getLanguage() == Language.PT_BR ? Flags.TITLE_REGISTER.getPt_br() : Flags.TITLE_REGISTER.getEn_us()) : (aPlayer.getLanguage() == Language.PT_BR ? Flags.TITLE_LOGIN.getPt_br() : Flags.TITLE_LOGIN.getEn_us())), 5, 9999, 5);
                    user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.LEVEL_UP, 100f, 100f);
                });
    }
}
