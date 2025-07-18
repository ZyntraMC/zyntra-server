package mc.zyntra.collectibles.inventory;

import mc.zyntra.cMain;
import mc.zyntra.bukkit.BukkitMain;
import mc.zyntra.bukkit.api.inventory.MenuInventory;
import mc.zyntra.bukkit.api.inventory.item.ItemBuilder;
import mc.zyntra.collectibles.parser.CollectiblePlayer;
import mc.zyntra.collectibles.parser.hats.Hats;
import mc.zyntra.collectibles.parser.particles.Particles;
import mc.zyntra.general.Constant;
import mc.zyntra.general.account.ZyntraPlayer;
import mc.zyntra.general.utils.string.StringLoreUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;


public class CollectiblesInventory extends MenuInventory {

    public CollectiblesInventory(ZyntraPlayer opener, cMain.Collectible menuType) {
        super("Cosméticos", 6);
        CollectiblePlayer collectiblePlayer = cMain.getPlayerLoader().get(opener.getUniqueId());

        switch (menuType) {
            case MAIN: {
                setTitle("Cosméticos");

                setItem(10, new ItemBuilder(Material.FIREWORK)
                        .setName("§aAnimações de Vitória")
                        .setLore("§7Comemore de diversas maneiras",
                                "§7quando você vencer uma partida!",
                                "",
                                "§7Desbloqueados: §bN/A §8(N/A)",
                                "",
                                "§eClique para acessar!")
                        .build());

                setItem(12, new ItemBuilder(Material.LEATHER_HELMET)
                        .setName("§aChapéus")
                        .setLore("§7Equipe cabeças personalizadas,",
                                "§7banners e muito mais!",
                                "",
                                "§7Desbloqueados: §b" + (Hats.getHatsNumber(opener.getPrimaryGroupData().getGroup())) + "§b/" + (Hats.getTotalHats()) + " §8(" + (Hats.getPercentageComplete(opener.getPrimaryGroupData().getGroup())) + ")",
                                "",
                                "§eClique para acessar!").build(),
                        (p, inv, type, stack, slot) -> new CollectiblesInventory(opener, cMain.Collectible.HATS).open(p));

                setItem(14, new ItemBuilder(Material.RAW_FISH).setDurability(3)
                        .setName("§aPets")
                        .setLore("§7Escolha um pet para ser o seu",
                                "§7companheiro no servidor!",
                                "",
                                "§7Desbloqueados: §bN/A §8(N/A)",
                                "",
                                "§eClique para acessar!")
                        .build());

                setItem(16, new ItemBuilder(Material.BLAZE_POWDER)
                        .setName("§aPartículas")
                        .setLore("§7Equipe uma partícula para",
                                "§7emanar de você no lobby!",
                                "",
                                "§7Desbloqueados: §b" + (Particles.getHatsNumber(opener.getPrimaryGroupData().getGroup())) + "§b/" + (Particles.getTotalHats()) + " §8(" + (Particles.getPercentageComplete(opener.getPrimaryGroupData().getGroup())) + ")",
                                "",
                                "§eClique para acessar!")
                                .build(),
                        (p, inv, type, stack, slot) -> new CollectiblesInventory(opener, cMain.Collectible.PARTICLES).open(p));

                setItem(28, new ItemBuilder(Material.TNT)
                        .setName("§aEngenhocas")
                        .setLore("§7Faça várias gambiarras com as",
                                "§7nossas engenhocas e divirta-se",
                                "§7pelo lobby!",
                                "",
                                "§7Desbloqueados: §bN/A §8(N/A)",
                                "",
                                "§eClique para acessar!")
                        .build());

                setItem(30, new ItemBuilder(Material.NAME_TAG)
                        .setName("§aTítulos")
                        .setLore("§7Equipe um título que será exibido",
                                "§7acima de seu nickname no lobby!",
                                "",
                                "§7Desbloqueados: §bN/A §8(N/A)",
                                "",
                                "§eClique para acessar!")
                        .build());

                setItem(32, new ItemBuilder(Material.FEATHER)
                        .setName("§aAsas")
                        .setLore("§7Equipe magnificas asas que",
                                "§7serão projetadas em suas",
                                "§7costas!",
                                "",
                                "§7Desbloqueados: §bN/A §8(N/A)",
                                "",
                                "§eClique para acessar!")
                        .build());

                setItem(34, new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setName("§aTrajes")
                        .setLore("§7Desfile com estilo pelo lobby",
                                "§7com trajes incríveis!",
                                "",
                                "§7Desbloqueados: §bN/A §8(N/A)",
                                "",
                                "§eClique para acessar!")
                        .build());

                setItem(48, new ItemBuilder(Material.BOOK)
                        .setName("§eComo obter cosméticos?")
                        .setLore("§7Você pode adquirir cosméticos ao",
                                "§7abrir §5Caixas Misteriosas §7e em",
                                "§3Eventos Especiais §7do servidor!",
                                "",
                                "§eClique para acessar nossa loja!")
                        .build());

                setItem(50, new ItemBuilder(Material.BARRIER)
                        .setName("§cRemover tudo")
                        .setLore("§7Remova todos os cosméticos",
                                "§7que você está equipado!",
                                "",
                                "§eClique para remover!")
                        .build());
                break;
            }
            case HATS: {
                setTitle("Cosméticos - Chapéus");
                int slot = 10;
                for (Hats hats : Hats.values()) {
                    setItem(slot, (!opener.hasGroupPermission(hats.getPermission()) ? new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setName("§a" + hats.getDisplay())
                                    .setLore(StringLoreUtils.formatForLore("§7Setar argumento de descrição\n§7para os chapéus!\n\n" + (!opener.hasGroupPermission(hats.getPermission()) ? "§cRequer o rank " + hats.getPermission().getColoredName() + " §cou superior\n§cAdquira o seu em §e" + Constant.STORE + "\n\n§cVocê não tem acesso!" : "§eClique para selecionar!")))
                                    .setPlayerHeadBase64(hats.getValue()).build() :
                                    new ItemBuilder(Material.SKULL_ITEM).setDurability(3)
                            .setPlayerHeadBase64(hats.getValue())
                            .setName("§a" + hats.getDisplay())
                            .setLore(StringLoreUtils.formatForLore("§7Setar argumento de descrição\n§7para os chapéus!\n\n" + (!opener.hasGroupPermission(hats.getPermission()) ? "§cRequer o rank " + hats.getPermission().getColoredName() + " §cou superior\n§cAdquira o seu em §e" + Constant.STORE + "\n\n§cVocê não tem acesso!" : "§eClique para selecionar!")))
                                    .setPlayerHeadBase64(hats.getValue())
                                    .build()),
                            (p, inv, type, stack, s) -> {
                        p.closeInventory();
                        if (collectiblePlayer.getHat() != hats) {
                            collectiblePlayer.setHat(hats);
                            p.sendMessage("§aVocê selecionou o chapéu: §e" + hats.getDisplay() + "§a.");
                            p.getInventory().setHelmet(new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3)).setName("§a" + hats.getDisplay()).setPlayerHeadBase64(hats.getValue()).build());
                        } else {
                            p.sendMessage("§c'collectibles': Você já está utilizando este chapéu.");
                        }
                            });
                    if (slot % 9 == 7) {
                        slot += 3;
                        continue;
                    }
                    slot += 1;
                }

                setItem(48, new ItemBuilder(Material.BOOK)
                        .setName("§eComo obter cosméticos?")
                        .setLore("§7Você pode adquirir cosméticos ao",
                                "§7abrir §5Caixas Misteriosas §7e em",
                                "§3Eventos Especiais §7do servidor!",
                                "",
                                "§eClique para acessar nossa loja!")
                        .build());
                setItem(49, new ItemBuilder(Material.ARROW)
                        .setName("§cVoltar")
                        .setLore("§7Para a área: MAIN",
                                "",
                                "§eClique para voltar!").build(),
                        (p, inv, type, stack, s) -> new CollectiblesInventory(opener, cMain.Collectible.MAIN).open(p));
                setItem(50, new ItemBuilder(Material.BARRIER)
                        .setName((collectiblePlayer.getHat() != null ? "§cRemover §e" + collectiblePlayer.getHat().getDisplay() : "§cNenhum chapéu setado."))
                        .setLore("§7Remova o chapéu atual",
                                "§7que você está equipado!",
                                "",
                                "§eClique para remover!")
                                .build(),
                        (p, inv, type, stack, s) -> {
                            if (collectiblePlayer.getHat() != null) {
                                p.closeInventory();
                                p.sendMessage("§aVocê removeu o chapéu: §e" + collectiblePlayer.getHat().getDisplay() + "§a.");
                                collectiblePlayer.setHat(null);
                                p.getInventory().setHelmet(null);
                            } else {
                                p.sendMessage("§c'collectibles': Você não possui nenhum chapéu selecionado no momento.");
                            }
                        });
                break;
            }
            case PARTICLES: {
                setTitle("Cosméticos - Partículas");
                int slot = 10;
                for (Particles particles : Particles.values()) {
                    setItem(slot, new ItemBuilder((!opener.hasGroupPermission(particles.getPermission()) ? new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7) : particles.getItem()))
                                    .setName("§a" + particles.getDisplay())
                                    .setLore(StringLoreUtils.formatForLore("§7Setar argumento de descrição\n§7para as particulas!\n\n" + (!opener.hasGroupPermission(particles.getPermission()) ? "§cRequer o rank " + particles.getPermission().getColoredName() + " §cou superior\n§cAdquira o seu em §e" + Constant.STORE + "\n\n§cVocê não tem acesso!" : "§eClique para selecionar!")))
                                    .build(),
                            (p, inv, type, stack, s) -> {
                                if (collectiblePlayer.getParticle() != particles) {
                                    p.closeInventory();
                                    collectiblePlayer.setParticles(true);
                                    collectiblePlayer.setParticle(particles);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            for (CollectiblePlayer c : cMain.getPlayerLoader().list()) {
                                                if (collectiblePlayer.isParticles() && !collectiblePlayer.isWings()) {
                                                    Particles.play(Bukkit.getPlayer(c.getUniqueId()));
                                                }
                                            }
                                        }
                                    }.runTaskTimerAsynchronously(BukkitMain.getInstance(), 0, 3);
                                    p.sendMessage("§b§l➜ §eVocê selecionou a partícula: §a" + particles.getDisplay() + "§e.");
                                } else {
                                    p.sendMessage("§c'collectibles': Você já está utilizando esta partícula.");
                                }
                            });
                    if (slot % 9 == 7) {
                        slot += 3;
                        continue;
                    }
                    slot += 1;
                }

                setItem(48, new ItemBuilder(Material.BOOK)
                        .setName("§eComo obter cosméticos?")
                        .setLore("§7Você pode adquirir cosméticos ao",
                                "§7abrir §5Caixas Misteriosas §7e em",
                                "§3Eventos Especiais §7do servidor!",
                                "",
                                "§eClique para acessar nossa loja!")
                        .build());
                setItem(49, new ItemBuilder(Material.ARROW)
                                .setName("§cVoltar")
                                .setLore("§7Para a área: MAIN",
                                        "",
                                        "§eClique para voltar!").build(),
                        (p, inv, type, stack, s) -> new CollectiblesInventory(opener, cMain.Collectible.MAIN).open(p));
                setItem(50, new ItemBuilder(Material.BARRIER)
                                .setName((collectiblePlayer.getParticle() != null ? "§cRemover §e" + collectiblePlayer.getParticle().getDisplay() : "§cNenhum chapéu setado."))
                                .setLore("§7Remova o chapéu atual",
                                        "§7que você está equipado!",
                                        "",
                                        "§eClique para remover!")
                                .build(),
                        (p, inv, type, stack, s) -> {
                            if (collectiblePlayer.getParticle() != null) {
                                p.closeInventory();
                                p.sendMessage("§b§l➜ §eVocê removeu a partícula: §a" + collectiblePlayer.getParticle().getDisplay() + "§e.");
                                collectiblePlayer.setParticle(null);
                                collectiblePlayer.setParticles(false);
                            } else {
                                p.sendMessage("§c'collectibles': Você não possui nenhuma partícula selecionado no momento.");
                            }
                        });
                break;
            }
        }
    }
}
