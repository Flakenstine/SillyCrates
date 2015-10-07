package us.mcmagic.sillycrates.util;

import net.minecraft.server.v1_8_R3.EntityTypes;
import us.mcmagic.sillycrates.loot.entity.CrateEntityType;

import java.lang.reflect.Field;
import java.util.Map;

public class NMSUtil {

    private NMSUtil() { }

    public static void registerEntity(CrateEntityType type) {
        registerEntity(type.getName(), type.getId(), type.getEntityClass());
    }

    private static void registerEntity(String name, int id, Class oclass) {
        ((Map) getPrivateField(EntityTypes.class, "c", null)).put(name, oclass);
        ((Map) getPrivateField(EntityTypes.class, "d", null)).put(oclass, name);
        ((Map) getPrivateField(EntityTypes.class, "f", null)).put(oclass, Integer.valueOf(id));
        ((Map) getPrivateField(EntityTypes.class, "g", null)).put(name, Integer.valueOf(id));
    }

    public static Object getPrivateField(Class clazz, String fieldName, Object object)  {
        Field field;
        Object o;
        try  {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
            return o;
        }
        catch(NoSuchFieldException | IllegalAccessException e)  {
            e.printStackTrace();
            return null;
        }
    }
}
