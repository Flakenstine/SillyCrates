package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CrateEntityFactory implements ICrateEntityFactory {

    @Override
    public org.bukkit.entity.Entity spawn(Entity entity, Location location) {
        World minecraftWorld = ((CraftWorld) location.getWorld()).getHandle();
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) entity.getBukkitEntity()).setRemoveWhenFarAway(false);
        minecraftWorld.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return entity.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity spawn(Location location, CrateEntityType type) {
        Entity entity = type.getObject(location.getWorld());
        return spawn(entity, location);
    }
}
