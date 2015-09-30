package us.mcmagic.sillycrates.time;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.util.FileUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class TimeTracker {

    private CopyOnWriteArrayList<TrackedPlayer> players;
    private static final Long TICKS_PER_MINUTE = 1200L;

    public TimeTracker() {
        players = new CopyOnWriteArrayList<>();
        populate();
        updatePlayTime();
    }

    public void updatePlayTime() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (TrackedPlayer player : players) {
                    if (player == null) {
                        continue;
                    }
                    player.addMinute();
                    if (player.getPlayTime() % 15 == 0) {
                        player.addCrate();
                    }
                    try {
                        FileUtil.updateTrackedPlayer(player);
                    } catch (IOException e) {
                        CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not update player:" + player.getUniqueId().toString(), e);
                    }
                }
            }
        }, 10L, TICKS_PER_MINUTE);
    }

    private void populate() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p == null) {
                continue;
            }
            this.addPlayerAsync(p.getUniqueId());
        }
    }

    public void addPlayerAsync(final UUID id) throws NullPointerException {
        (new BukkitRunnable() {
            public void run() {
                for (TrackedPlayer player : players) {
                    if (!player.getUniqueId().equals(id)) {
                        break;
                    }
                }
                if (isFirstJoin(id) && Bukkit.getPlayer(id) != null) {
                    TrackedPlayer p = new TrackedPlayer(id, 1, 0);
                    players.add(p);
                    p.addCrate();
                    try {
                        String name = Bukkit.getPlayer(id).getName();
                        FileUtil.registerTrackedPlayer(p, name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ConfigurationSection player = FileUtil.playerYaml.getConfigurationSection("players." + id.toString());
                    int minutes = (Integer) player.get("minutes");
                    int crates = (Integer) player.get("crates");
                    TrackedPlayer p = new TrackedPlayer(id, minutes, crates);
                    players.add(p);

                }
            }
        }).runTaskAsynchronously(CratesPlugin.getInstance());
    }

    public void removePlayerAsync(final UUID id) {
        (new BukkitRunnable() {
            public void run() {
                for (TrackedPlayer player : players) {
                    if (player.getUniqueId().equals(id)) {
                        players.remove(player);
                        break;
                    }
                }
            }
        }).runTaskAsynchronously(CratesPlugin.getInstance());
    }

    public TrackedPlayer findTrackedPlayer(UUID id) {
        TrackedPlayer trackedPlayer = null;
        for (TrackedPlayer player : players) {
            if (player.getUniqueId().equals(id)) {
                trackedPlayer = player;
            }
        }
        return trackedPlayer;
    }

    public boolean isFirstJoin(UUID id) {
        return !FileUtil.playerYaml.getConfigurationSection("players").contains(id.toString());
    }
}
