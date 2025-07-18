package mc.zyntra.util;

import mc.zyntra.match.generator.loot.ChestController;
import mc.zyntra.match.kit.loader.KitLoader;
import mc.zyntra.match.generator.player.loader.PlayerLoader;
import mc.zyntra.match.perks.loader.PerkLoader;
import mc.zyntra.room.service.loader.RoomLoader;
import mc.zyntra.room.creator.controller.RoomCreatorController;
import mc.zyntra.match.Match;
import lombok.Getter;


public class Platform {

    @Getter
    private static RoomLoader roomLoader = new RoomLoader();

    @Getter
    private static RoomCreatorController roomCreatorController = new RoomCreatorController();

    @Getter
    private static PlayerLoader playerLoader = new PlayerLoader();

    @Getter
    private static KitLoader kitLoader = new KitLoader();

    @Getter
    private static PerkLoader perkLoader = new PerkLoader();

    @Getter
    private static Match Match = new Match();

    @Getter
    private static ChestController chestController;

}
