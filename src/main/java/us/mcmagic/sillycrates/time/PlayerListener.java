package us.mcmagic.sillycrates.time;

import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.mcmagic.sillycrates.CratesPlugin;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(final PlayerJoinEvent event) {
        CratesPlugin.getInstance().getTimeTracker().addPlayerAsync(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(event.getPlayer(), "crate amount");
            }
        }, 40L);
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent event) {
        CratesPlugin.getInstance().getTimeTracker().removePlayerAsync(event.getPlayer().getUniqueId());
    }
}
