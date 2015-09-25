package us.mcmagic.sillycrates.time;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.SillyCratesMessage;

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
        SillyCratesMessage.send("&6You've been awarded a crate for playing on our server! Use &c/crate unlock &6to redeem.", Bukkit.getPlayer(id));
    }

    private void delCrate() {
        if (availableCrates > 0) {
            availableCrates--;
        } else {
            availableCrates = 0;
        }
    }

    public boolean inventoryFull() {
        Player player = Bukkit.getPlayer(id);
        return player.getInventory().firstEmpty() == -1;
    }

    public void unlockCrate() {
        Player player = Bukkit.getPlayer(id);
        if (availableCrates > 0 && !inventoryFull()) {
            delCrate();
            player.getInventory().addItem(CratesPlugin.getCrateItem());
            player.updateInventory();
            if (availableCrates == 1) {
                SillyCratesMessage.send("&2Crate unlocked! " + availableCrates + "crate remaining.", player);
            } else {
                SillyCratesMessage.send("&2Crate unlocked! " + availableCrates + " crates remaining.", player);
            }
        } else if (inventoryFull()) {
            CratesPlugin.getInstance().getManager().error(player, "Uh oh! Looks like your inventory is full!");
        } else if (availableCrates == 0) {
            CratesPlugin.getInstance().getManager().error(player, "No crates available. Crates are ");
        }
    }
}
