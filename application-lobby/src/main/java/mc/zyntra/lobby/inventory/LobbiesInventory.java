package mc.zyntra.lobby.inventory;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.general.Core;
import mc.zyntra.general.server.ServerConfiguration;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Material;

public class LobbiesInventory extends MenuInventory {

    public LobbiesInventory() {
        super("Selecione um lobby", 4);

        int slot = 10;
        for (ServerConfiguration serverConfiguration : Core.getDataServer().getServers(Core.getServerType())) {
            setItem(slot, new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .setName("§a" +
                                    (serverConfiguration.getServerType().getName().equalsIgnoreCase("Main Lobby")
                                            ? "Lobby Principal" : serverConfiguration.getServerType().getName())
                                    + " #" +
                                    (serverConfiguration.getName().contains("-") ? serverConfiguration.getName().split("-")[1] : "???"))
                            .setLore("§7Players: " + serverConfiguration.getOnlineCount() + "/" + serverConfiguration.getMaxPlayers(),
                                    "",
                                    (serverConfiguration.getName() == Core.getServerName() ? "§cVocê já está conectado neste saguão!" : "§eClique para conectar!"))
                            .setDurability((serverConfiguration.getName() == Core.getServerName() ? 7 : 5))
                            .build(),
                    (p, inv, type, stack, slot1) -> {
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(serverConfiguration.getName());
                        p.sendPluginMessage(BukkitMain.getInstance(), "BungeeCord", out.toByteArray());
                    });
            slot++;
        }
    }
}