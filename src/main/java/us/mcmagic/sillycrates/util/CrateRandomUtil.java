package us.mcmagic.sillycrates.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrateRandomUtil {

    public static final SecureRandom random = new SecureRandom();

    private CrateRandomUtil() {
        // No instance allowed
    }

    public static Location getRandomLocation(Location center, int radius) {
        int x = center.getBlockX() + betweenInclusive(-radius, radius);
        int y = center.getBlockZ() + betweenInclusive(-radius, radius);
        int z = center.getBlockZ() + betweenInclusive(-radius, radius);
        return new Location(center.getWorld(), x, y, z);
    }

    public static Vector getRandomVector() {
        double x, y, z;
        x = random.nextDouble() * 2 - 1;
        y = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector(x, y, z).normalize();
    }

    public static Vector getRandomCircleVector() {
        double rnd, x, z;
        rnd = random.nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);

        return new Vector(x, 0, z);
    }

    public static int betweenInclusive(int min, int max) {
        return random.nextInt(max-min+1) + min;
    }

    public static Material getRandomMaterial(Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2 * Math.PI;
    }

    private ItemStack[] generateRandomArmor() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(randomColor());
        helmet.setItemMeta(helmetMeta);
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) helmet.getItemMeta();
        chestMeta.setColor(randomColor());
        chest.setItemMeta(chestMeta);
        ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta pantsMeta = (LeatherArmorMeta) helmet.getItemMeta();
        pantsMeta.setColor(randomColor());
        pants.setItemMeta(pantsMeta);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) helmet.getItemMeta();
        bootsMeta.setColor(randomColor());
        boots.setItemMeta(bootsMeta);
        return new ItemStack[]{helmet, chest, pants, boots};
    }

    private Color randomColor() {
        return Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static int between(int minimum, int maximum) {
        if (maximum < minimum) {
            maximum = minimum;
        }
        return random.nextInt(maximum - minimum + 1) + minimum;
    }

    public static double between(double minimum, double maximum) {
        if (maximum < minimum) {
            maximum = minimum;
        }
        return minimum + (maximum - minimum) * random.nextDouble();
    }

    public static Location location(Location loc, final int d) {
        double xOff = between(-d, d);
        double zOff = between(-d, d);

        loc = loc.subtract(xOff, 0, zOff);
        loc.setY(loc.getY() - 2);
        if (loc.getBlock().getType() != Material.AIR) {
            return location(loc, d + 1);
        }
        loc.setY(loc.getY() + 1);
        if (loc.getBlock().getType() != Material.AIR) {
            return location(loc, d + 1);
        }
        loc.setY(loc.getY() + 1);
        return loc;
    }

    public static Location unsafe_location(Location loc, final int d) {
        double xOff = between(-d, d);
        double zOff = between(-d, d);

        loc = loc.subtract(xOff, 0, zOff);
        return loc;
    }

    public static Location location(Location loc, final int minRange, final int range) {
        Location og = loc.clone();

        while (loc.distanceSquared(og) < minRange * minRange) {
            loc = location(loc, range);
        }

        return loc;
    }
}
