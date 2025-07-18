package mc.zyntra.match.listener;

import mc.zyntra.general.Core;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.account.group.Group;
import mc.zyntra.match.generator.player.MatchPlayer;
import mc.zyntra.util.Platform;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Dispenser;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onMatchPlayerExecuteCommand(PlayerInteractEvent event) {
        ZyntraPlayer zyntraPlayer = Core.getAccountController().get(event.getPlayer().getUniqueId());
        MatchPlayer matchPlayer = Platform.getPlayerLoader().get(event.getPlayer());
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && matchPlayer.getRoom().getSpectators().contains(event.getPlayer())) {
            Block b = event.getClickedBlock();
            if (b.getState() instanceof DoubleChest || b.getState() instanceof Chest || b.getState() instanceof Hopper
                    || b.getState() instanceof Dispenser || b.getState() instanceof Furnace
                    || b.getState() instanceof Beacon) {
                if (!zyntraPlayer.hasGroupPermission(Group.MODERATOR))
                    event.setCancelled(true);
            }
        }
    }
}
