package us.mcmagic.sillycrates;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import us.mcmagic.sillycrates.util.ParticleEffect;

import java.util.UUID;

public class Crate {

    public static final Material TYPE = Material.CHEST;
    public static final String NAME = CratesManager.colorize("&6&lSteve Co. Supply Crate");

    private final String id;
    private final Block block;
    private final UUID owner;
    private final UUID hologramID;
    private final ParticleEffect effect;

    public Crate(String id, UUID owner, UUID hologramID, Location loc, ParticleEffect effect) {
        this.id = id;
        this.owner = owner;
        this.hologramID = hologramID;
        this.block = loc.getBlock();
        this.effect = effect;
    }

    public Block getBlock() {
        return this.block;
    }

    public UUID getOwnerUUID() {
        return this.owner;
    }

    public UUID getHologramID() {
        return hologramID;
    }

    public Location getCenter() {
        return block.getLocation().add(0.5, 0.5, 0.5);
    }

    public String getId() {
        return id;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return hologramID.toString();
    }
}
