package mc.zyntra.room.regenerate;

import mc.zyntra.Leading;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipInputStream;


public class Regenerate {

    public static void deleteDirectory(Path directory) throws IOException {
        Files.walk(directory)
                .sorted(java.util.Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static void clear() {
        World world = Bukkit.getWorlds().get(0);

        if (world != null) {
            Bukkit.unloadWorld(world, false);
        }

        File pathWorld = world.getWorldFolder();

        try {
            for (File archive : pathWorld.listFiles()) {
                if (archive.isDirectory()) {
                    deleteDirectory(archive.toPath());
                } else {
                    archive.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void regenerate() {
        File pathArcade = Leading.getInstance().getDataFolder().toPath().resolve("arcade/map").toFile();
        File pathWorld = Bukkit.getWorlds().get(0).getWorldFolder();

        try {
            for (File archive : pathArcade.listFiles()) {
                Path destiny = pathWorld.toPath().resolve(archive.getName());
                Files.copy(archive.toPath(), destiny, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.createWorld(WorldCreator.name(Bukkit.getWorlds().get(0).getName()));
    }
}
