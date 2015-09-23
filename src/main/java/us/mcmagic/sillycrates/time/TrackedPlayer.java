package us.mcmagic.sillycrates.time;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TrackedPlayer {

    private final UUID id;
    private int playTime;
    private int availableCrates;

    public TrackedPlayer(UUID uuid, int time, int crates) {
        id = uuid;
        playTime = time;
        availableCrates = crates;
    }

    public UUID getUniqueId() {
        return id;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void addMinute() {
        playTime++;
    }

    public int getAvailableCrates() {
        return availableCrates;
    }

    public void addCrate() {
        availableCrates++;
    }

    public void delCrate() {
        availableCrates--;
    }

    public boolean inventoryFull() {
        Player player = Bukkit.getPlayer(id);
        return player.getInventory().firstEmpty() == -1;
    }
}
