package mc.zyntra.player.inventories;

import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.player.generator.User;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Random;

public class OldCaptchaInventory extends MenuInventory {

    public OldCaptchaInventory(User user) {
        super("Captcha", 3);
        setReopenInventory(true);

        for (int i = 0; i < 3 * 9; i++) {
            setItem(i, new ItemBuilder(Material.FURNACE)
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

        setItem(new Random().nextInt(3 * 9), new ItemBuilder(Material.WORKBENCH)
                        .setName("§a")
                        .build(),
                (p, inv, type, stack, slot) -> {
                    setReopenInventory(false);

                    user.setCompletedCaptcha(true);
                    user.getPlayer().closeInventory();
                    user.getPlayer().sendMessage("§aCaptcha concluído!");
                    user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.LEVEL_UP, 100f, 100f);
                });
    }
}
