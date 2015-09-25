package us.mcmagic.sillycrates.time;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
        SillyCratesMessage.sendWithoutHeader("&e&lYou've earned a &c&l/crate&e&l!", Bukkit.getPlayer(id));
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
                SillyCratesMessage.send("&eCrate unlocked: " + availableCrates + " crate remaining.", player);
            } else {
                SillyCratesMessage.send("&eCrate unlocked: " + availableCrates + " crates remaining.", player);
            }
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_PICKUP, 1F, 1F);
        } else if (inventoryFull()) {
            CratesPlugin.getInstance().getManager().error(player, "Uh oh! Looks like your inventory is full!");
        } else if (availableCrates == 0) {
            SillyCratesMessage.send("&eYou don't own any crates, keep playing to earn more!", player);
        }
    }
}
