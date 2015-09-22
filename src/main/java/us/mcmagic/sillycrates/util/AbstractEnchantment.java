package us.mcmagic.sillycrates.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

/**
 *
 * @author Goblom
 */
public abstract class AbstractEnchantment extends Enchantment {

    public AbstractEnchantment(int id) {
        super(id);

        if (id > 256) {
            throw new IllegalArgumentException("An enchantment id has to be lower then 256!");
        }

        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, Boolean.valueOf(true));
            Enchantment.registerEnchantment(this);
            f.set(null, f.getBoolean(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract boolean canEnchantItem(ItemStack item);

    public abstract boolean conflictsWith(Enchantment enchantment);

    public abstract EnchantmentTarget getItemTarget();

    public abstract int getMaxLevel();

    public abstract int getStartLevel();

    public abstract int getWeight();

    @Override
    public String getName() {
        return "Usages" + this.getId();
    }
}