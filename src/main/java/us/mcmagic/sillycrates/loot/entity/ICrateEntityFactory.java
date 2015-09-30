package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;

public interface ICrateEntityFactory {
    org.bukkit.entity.Entity spawn(Entity entity, Location location);
    org.bukkit.entity.Entity spawn(Location location, CrateEntityType type);
}
