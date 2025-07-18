package mc.zyntra.collectibles.parser.hats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mc.zyntra.general.account.group.Group;


@Getter
@AllArgsConstructor
public enum Hats {

    SLIME("Slime", "ewogICJ0aW1lc3RhbXAiIDogMTY0NTM3NTY3ODk0NCwKICAicHJvZmlsZUlkIiA6ICJhNzdkNmQ2YmFjOWE0NzY3YTFhNzU1NjYxOTllYmY5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwOEJFRDUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY5ODY0MzExZjg4NTJiZGU4YTQ4OWEyMzdhODE5YjEzNjNjNjAzYTJiODVmYjI0M2VlNGYxYWJhMjU1MWFkZiIKICAgIH0KICB9Cn0=", Group.GOLD),
    CHICKEN("Galinha", "eyJ0aW1lc3RhbXAiOjE1ODU3MzgzNTQzMzEsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJNaW5lU2tpbl9vcmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JiN2NiNDJhMzM0N2RmMDEzZmI2MGRiMGI1NTNmNDViNmNiZjIwYjE1ZDhkMDBkZmQ0YjAyYmVkZjkwZjhkNjIifX19", Group.GOLD),
    IRON("Bloco de Ferro", "ewogICJ0aW1lc3RhbXAiIDogMTY0ODE3ODEwNzQzOSwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjVkN2M3MzdlYTkxZWFkNGNjNTA1NzcxNzQ5NDU3OTMzNzAwNWQ2MjRkMDZhM2QzYzdkNzkyZTZhY2IwYWM3ZiIKICAgIH0KICB9Cn0=", Group.GOLD),
    BEACON("Beacon", "ewogICJ0aW1lc3RhbXAiIDogMTY0MDQwMTcyMTM3NSwKICAicHJvZmlsZUlkIiA6ICIyMWFlMDM2OWJhMDM0NGFkOGY1ZjhlM2JlYTMwOTQ3MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaXJhbF9BbmdlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zODc5YmViMDRkMjA5MjY5ZmJlMDcwNjZjZTY0ZmJiN2FjMTdlZTY2ODFlZmJmZTY0NTFiYThjNWNlZTc3MGMxIgogICAgfQogIH0KfQ==", Group.GOLD),
    PUMPKIN("Abóbora", "eyJ0aW1lc3RhbXAiOjE1NzE0MjUyOTk1NjksInByb2ZpbGVJZCI6IjkxOGEwMjk1NTlkZDRjZTZiMTZmN2E1ZDUzZWZiNDEyIiwicHJvZmlsZU5hbWUiOiJCZWV2ZWxvcGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jZjhiMzM1MTk5Y2FiMjg5MjE2YzE5ZThiN2Q0MGVlYWZiMGYxYWM4MWRlZDRiZjE1MGM3NzYwZWRmNTNiYjAifX19", Group.GOLD),
    CACTUS("Cacto", "ewogICJ0aW1lc3RhbXAiIDogMTYxMjQ0NDIxODM1NSwKICAicHJvZmlsZUlkIiA6ICJlMGVkZjczOWY2YTU0NDBmOTU4ODQ0NjE2ZjQ1NTVkYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYWN0dXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTE1NmZmYzExOWVjY2U3NWYwOTAyM2Y1NWU3NGRlYmZjYzc5OGEzYTBmZDJmNTM4OGMxMjFjZjU0OTdmY2ViYSIKICAgIH0KICB9Cn0=", Group.GOLD),
    EARTH("Terra", "eyJ0aW1lc3RhbXAiOjE1MDI3NTYxODM1OTYsInByb2ZpbGVJZCI6IjIzZjFhNTlmNDY5YjQzZGRiZGI1MzdiZmVjMTA0NzFmIiwicHJvZmlsZU5hbWUiOiIyODA3Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NzNiMTAxOTc2ZmM3NTMyZjc3YTYwNjZhNTZhNWRkZDYzNmRiYzNhNzNmMTU0ZGNmOWE0YWRmYzc2YTIzNzUifX19", Group.GOLD),
    CAKE("Bolo", "ewogICJ0aW1lc3RhbXAiIDogMTYyNTc2NzQ5NTU4NSwKICAicHJvZmlsZUlkIiA6ICIyNDBiYThlMzNlN2M0YzE0ODhiNzJmYmU1Njg2ZjhlNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJQbGF4Q3JhZnRzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzc3NGYyNTRhZGIyY2Q3NDlmYTQ1YjM3MWIyMTZlZGNhZWQ0ZTM5MjZhNGI1YmFiNTkyODAxMmI1ZjFjODgyNTAiCiAgICB9CiAgfQp9", Group.GOLD);

    private String display, value;
    private Group permission;

    public static int getHatsNumber(Group playerGroup) {
        int count = 0;
        for (Hats hat : Hats.values()) {
            if (hat.getPermission() == playerGroup) {
                count++;
            }
        }
        return count;
    }

    public static int getTotalHats() {
        return Hats.values().length;
    }

    public static double getPercentageComplete(Group playerGroup) {
        int totalHatsCount = Hats.values().length;
        int playerHatsCount = getHatsNumber(playerGroup);

        if (totalHatsCount == 0) {
            return 0.0;
        }

        double percentageComplete = (double) playerHatsCount / totalHatsCount * 100.0;
        return percentageComplete;
    }
}
