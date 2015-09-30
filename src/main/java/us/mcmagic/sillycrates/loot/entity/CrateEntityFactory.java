package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Collection;

public class CrateEntityFactory implements ICrateEntityFactory {

    private int playerCheckRadius = 4;

    @Override
    public org.bukkit.entity.Entity spawn(Entity entity, Location location) {
        World minecraftWorld = ((CraftWorld) location.getWorld()).getHandle();
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
        Collection<org.bukkit.entity.Entity> entities = location.getWorld().getNearbyEntities(location, playerCheckRadius, playerCheckRadius, playerCheckRadius);
        Player closest = null;
        for (org.bukkit.entity.Entity e : entities) {
            if (e instanceof Player) {
                closest = (Player) e;
            }
        }
        if (closest != null) {
            Location loc = entity.getBukkitEntity().getLocation();
            loc.setYaw(closest.getLocation().getYaw()-180);
            entity.teleportTo(loc, false);
        }
        minecraftWorld.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawn(Location location, CrateEntityType type) {
        Entity entity = type.getObject(location.getWorld());
        return spawn(entity, location);
    }
}
