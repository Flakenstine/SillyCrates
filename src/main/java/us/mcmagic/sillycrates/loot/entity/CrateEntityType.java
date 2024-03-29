package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public enum CrateEntityType {

    CRATE_GIANT("Crate Giant", 53, CrateGiant.class),
    CRATE_ZOMBIE("Crate Zombie", 54, CrateZombie.class);

    private String name;
    private int id;
    private Class<? extends EntityInsentient> entityClass;

    CrateEntityType(String name, int id, Class<? extends EntityInsentient> entityClass) {
        this.name = name;
        this.id = id;
        this.entityClass = entityClass;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Class<? extends EntityInsentient> getEntityClass() {
        return entityClass;
    }

    public Entity getObject(org.bukkit.World world) {
        try {
            World minecraftWorld = ((CraftWorld) world).getHandle();
            Constructor<?> constructor = entityClass.getConstructor(World.class);
            Entity e = (Entity) constructor.newInstance(new Object[] {minecraftWorld});
            return e;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}