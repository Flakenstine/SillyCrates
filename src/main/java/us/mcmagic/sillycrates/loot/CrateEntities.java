package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;

public class CrateEntities implements ICrateRandom {

    private Rarity rarity;
    protected List<Entity> entities;

    public CrateEntities(Rarity value) {
        this.rarity = value;
    }

    public CrateEntities(Rarity value, List<Entity> entities) {
        this.rarity = value;
        this.entities = entities;
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public void play(Location location) {
    }
}
