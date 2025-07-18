package mc.zyntra.lobby.npc;

import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.hologram.Hologram;
import mc.zyntra.bukkit.api.hologram.HologramAPI;
import mc.zyntra.bukkit.api.npc.NPCApi;
import mc.zyntra.bukkit.api.npc.NPCHuman;
import mc.zyntra.general.Core;
import mc.zyntra.general.server.ServerType;
import mc.zyntra.general.utils.mojang.UUIDFetcher;
import mc.zyntra.Main;
import mc.zyntra.lobby.inventory.sub.SearchInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

public class NPCController {

    private final List<NPCData> npcs = new ArrayList<>();

    public void registerNPCs() {
        addNPC("npcs.sw", Material.BOW, Arrays.asList("§b§lSky Wars", "§eClique para jogar!", "§70 jogando agora."),
                player -> Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    new SearchInventory(Core.getAccountController().get(player.getUniqueId()), SearchInventory.Server.SKYWARS_SOLO).open(player);
                }));

        addNPC("npcs.bw", Material.BED, Arrays.asList("§b§lBed Wars", "§eClique para jogar!", "§70 jogando agora."),
                player -> player.sendMessage("§cModo em desenvolvimento."));

        addNPC("npcs.pvp", Material.STONE_SWORD, Arrays.asList("§b§lKit PvP", "§eClique para jogar!", "§70 jogando agora."),
                player -> player.sendMessage("§cModo em desenvolvimento."));
    }

    public void addNPC(String configPath, Material handItem, List<String> hologramLines, java.util.function.Consumer<Player> onClick) {
        Location location = Main.getInstance().getLocationFromConfig(configPath);
        if (location == null) {
            Core.getLogger().warning("Localização do NPC '" + configPath + "' não encontrada.");
            return;
        }

        String skinName = Main.getInstance().getConfig().getString(configPath + ".skin", "Steve");

        Core.getLogger().info("Gerando NPC: " + configPath + " com skin: " + skinName);
        UUID skinUUID = UUIDFetcher.getUUID(skinName);
        Core.getLogger().info("UUID obtido para skin '" + skinName + "': " + skinUUID);

        if (skinUUID == null) {
            Core.getLogger().warning("Não foi possível obter o UUID para a skin: " + skinName);
            return;
        }


        Hologram hologram = new Hologram(location.clone().add(0, 0.4, 0), hologramLines);
        HologramAPI.spawnGlobal(hologram);
        BukkitMain.getInstance().getHologramManager().getGlobalHolograms().add(hologram);

        NPCHuman npc = NPCApi.spawnHuman(location, skinUUID);
        npc.setItemInHand(new ItemStack(handItem));
        npc.setOnClickListener(player -> onClick.accept(player));
        npc.spawn();

        BukkitMain.getInstance().getNpcManager().getGlobalNpc().add(npc);

        npcs.add(new NPCData(npc, hologram, configPath));
    }


    public void respawnNPC(String configPath, Material handItem, List<String> hologramLines, Consumer<Player> onClick) {
        // Remover NPC antigo:
        Iterator<NPCData> it = npcs.iterator();
        while (it.hasNext()) {
            NPCData data = it.next();
            if (data.configPath.equals(configPath)) {
                // Despawn do NPC
                if (data.npc.isSpawned()) {
                    data.npc.despawn();  // Método para despawnar o NPC do mundo (confira se existe)
                }
                // Remover holograma do mundo, se aplicável
                if (data.hologram != null) {
                    HologramAPI.removeGlobal(data.hologram);
                    BukkitMain.getInstance().getHologramManager().getGlobalHolograms().remove(data.hologram);
                }
                // Remover da lista
                it.remove();
            }
        }

        // Adiciona o NPC novo
        addNPC(configPath, handItem, hologramLines, onClick);
    }


    public void respawnAllNPCs() {
        List<NPCData> snapshot = new ArrayList<>(npcs);
        npcs.clear();

        for (NPCData data : snapshot) {
            Location loc = data.npc.getLocation();
            if (loc != null) {
                respawnNPC(data.configPath, data.npc.getItemInHand().getType(), data.hologram.getLines(),
                        player -> {
                            // Dependendo do configPath, faça o que deve acontecer no clique
                            switch (data.configPath) {
                                case "npcs.sw":
                                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                        new SearchInventory(Core.getAccountController().get(player.getUniqueId()), SearchInventory.Server.SKYWARS_SOLO).open(player);
                                    });
                                    break;
                                case "npcs.bw":
                                    player.sendMessage("§cModo em desenvolvimento.");
                                    break;
                                case "npcs.pvp":
                                    player.sendMessage("§cModo em desenvolvimento.");
                                    break;
                                default:
                                    break;
                            }
                        });
            }
        }
    }

    public void updateHolograms() {
        int swCount = Core.getDataServer().getOnlineCount(ServerType.LOBBY_SKYWARS)
                + Core.getDataServer().getOnlineCount(ServerType.SW_SOLO)
                + Core.getDataServer().getOnlineCount(ServerType.SW_DUO)
                + Core.getDataServer().getOnlineCount(ServerType.SW_MEGA);

        int bwCount = Core.getDataServer().getOnlineCount(ServerType.LOBBY_BEDWARS)
                + Core.getDataServer().getOnlineCount(ServerType.BW_SOLO)
                + Core.getDataServer().getOnlineCount(ServerType.BW_DUO)
                + Core.getDataServer().getOnlineCount(ServerType.BW_MEGA);

        int pvpCount = Core.getDataServer().getOnlineCount(ServerType.LOBBY_PVP)
                + Core.getDataServer().getOnlineCount(ServerType.PVP_FPS)
                + Core.getDataServer().getOnlineCount(ServerType.PVP_ARENA)
                + Core.getDataServer().getOnlineCount(ServerType.PVP_CHALLENGE);

        for (NPCData data : npcs) {
            if (data.hologram.getLines().get(0).contains("Sky Wars")) {
                data.hologram.setText(2, "§7" + swCount + " jogando agora.");
            } else if (data.hologram.getLines().get(0).contains("Bed Wars")) {
                data.hologram.setText(2, "§7" + bwCount + " jogando agora.");
            } else if (data.hologram.getLines().get(0).contains("PvP")) {
                data.hologram.setText(2, "§7" + pvpCount + " jogando agora.");
            }
        }
    }

    public void knockbackPlayersNearNPCs(Player player) {
        for (NPCData data : npcs) {
            if (!data.npc.isSpawned()) continue;
            if (player.getLocation().distance(data.npc.getLocation()) <= 1.2) {
                Vector knock = player.getLocation().toVector()
                        .subtract(data.npc.getLocation().toVector())
                        .normalize()
                        .multiply(0.7)
                        .setY(0.3);
                player.setVelocity(knock);
            }
        }
    }

    public Consumer<Player> getClickAction(String type) {
        switch (type.toLowerCase()) {
            case "sw":
                return player -> Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    new SearchInventory(Core.getAccountController().get(player.getUniqueId()), SearchInventory.Server.SKYWARS_SOLO).open(player);
                });

            case "bw":
                return player -> player.sendMessage("§cModo em desenvolvimento.");

            case "pvp":
                return player -> player.sendMessage("§cModo em desenvolvimento.");

            default:
                return player -> player.sendMessage("§cAção desconhecida.");
        }
    }

    private static class NPCData {
        public final NPCHuman npc;
        public final Hologram hologram;
        public final String configPath;

        public NPCData(NPCHuman npc, Hologram hologram, String configPath) {
            this.npc = npc;
            this.hologram = hologram;
            this.configPath = configPath;
        }
    }
}
