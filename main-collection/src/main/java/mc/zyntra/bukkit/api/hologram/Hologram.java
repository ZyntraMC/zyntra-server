package mc.zyntra.bukkit.api.hologram;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Hologram {

    @Getter
    private Location location;

    private final List<HologramRow> rows = new ArrayList<>();
    private final Set<Player> viewers = new HashSet<>();

    public Hologram(@NonNull Location location) {
        this.location = location;
    }

    public Hologram(@NonNull Location location, @NonNull List<String> lines) {
        this(location);
        setText(lines);
    }

    public World getWorld() {
        return location.getWorld();
    }

    public void teleport(@NonNull Location location) {
        this.location = location;

        if (rows.isEmpty())
            return;

        Location loc = location.clone();

        for (HologramRow row : rows) {
            row.teleport(loc);
            loc = loc.clone().subtract(0, 0.25, 0);
        }

        viewers.forEach(v -> rows.forEach(r -> r.respawn(v)));
    }

    public synchronized void setText(@NonNull List<String> lines) {
        if (rows.isEmpty()) {
            Location loc = location.clone();

            for (String line : lines) {
                rows.add(new HologramRow(loc, line));
                loc = loc.clone().subtract(0, 0.25, 0);
            }

            rows.forEach(row -> viewers.forEach(row::spawn));
            return;
        }

        int diff = lines.size() - rows.size();

        if (diff > 0) { // Adicionar novas linhas
            int index = 0;

            for (HologramRow row : rows) {
                row.setText(lines.get(index++));
            }

            this.updateRows();

            int lastIndex = rows.size() - 1;
            HologramRow lastRow = rows.get(lastIndex);
            Location loc = lastRow.getLocation().clone().subtract(0, 0.25, 0);

            while (diff > 0) {
                HologramRow row = new HologramRow(loc, lines.get(index++));
                viewers.forEach(row::spawn);
                rows.add(row);

                loc = loc.clone().subtract(0, 0.25, 0);
                diff--;
            }
        } else { // Remove linhas e atualiza as restantes
            Iterator<HologramRow> rowIterator = rows.iterator();

            for (int i = 0; rowIterator.hasNext(); i++) {
                HologramRow row = rowIterator.next();

                if (i < lines.size()) {
                    row.setText(lines.get(i));
                } else {
                    viewers.forEach(row::despawn);
                    rowIterator.remove();
                }
            }

            this.updateRows();
        }
    }

    public void setText(int index, String text) {
        HologramRow row = rows.get(index);
        row.setText(text);
        viewers.forEach(row::update);
    }

    public void spawnTo(Player player) {
        if (!viewers.contains(player)) {
            viewers.add(player);
            rows.forEach(line -> line.spawn(player));
        }
    }

    public void despawnTo(Player player) {
        if (viewers.contains(player)) {
            viewers.remove(player);
            rows.forEach(line -> line.despawn(player));
        }
    }

    public void despawnForAll() {
        if (!viewers.isEmpty()) {
            rows.forEach(h -> viewers.forEach(h::despawn));
            viewers.clear();
        }
    }

    private void updateRows() {
        rows.forEach(r -> viewers.forEach(r::update));
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<>();
        for (HologramRow row : rows) {
            lines.add(row.getText());
        }
        return lines;
    }


}
