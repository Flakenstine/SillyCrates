package us.mcmagic.sillycrates.time;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.mcmagic.sillycrates.CratesPlugin;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent event) {
        CratesPlugin.getInstance().getTimeTracker().addPlayerAsync(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        CratesPlugin.getInstance().getTimeTracker().removePlayerAsync(event.getPlayer().getUniqueId());
    }
}
