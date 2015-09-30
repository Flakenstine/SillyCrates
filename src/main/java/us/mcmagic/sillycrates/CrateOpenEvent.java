package us.mcmagic.sillycrates;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.mcmagic.sillycrates.Crate;

import java.util.UUID;

public class CrateOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final UUID uuid;
    private final Crate crate;
    private boolean cancelled;

    public CrateOpenEvent(Crate crate, UUID opener) {
        this.crate = crate;
        uuid = opener;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Crate getCrate() {
        return crate;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
