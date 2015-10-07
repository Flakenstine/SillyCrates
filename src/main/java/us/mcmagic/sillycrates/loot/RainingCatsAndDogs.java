package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;
import org.bukkit.entity.*;
import us.mcmagic.sillycrates.loot.CrateEntities;
import us.mcmagic.sillycrates.loot.Rarity;
import us.mcmagic.sillycrates.util.CrateRandomUtil;

import java.util.ArrayList;
import java.util.UUID;

public class RainingCatsAndDogs extends CrateEntities {

    public RainingCatsAndDogs(Rarity value) {
        super(value);
    }

    @Override
    public void play(Location location) {
        entities = new ArrayList<>(100);
        for (int i = 0; i < 100; i++)
            if (i % 2 == 0) {
                LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location.add(CrateRandomUtil.between(-2, 2), 15, CrateRandomUtil.between(-2, 2)), EntityType.WOLF);
                this.entities.add(entity);
                Wolf wolf = (Wolf) entity;
                wolf.setOwner(null);
            } else {
                LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location.add(CrateRandomUtil.between(-2, 2), 15, CrateRandomUtil.between(-2, 2)), EntityType.OCELOT);
                this.entities.add(entity);
                Ocelot cat = (Ocelot) entity;
                cat.setOwner(null);
            }
    }
}
