package us.mcmagic.mysterycrates.util;

import org.bukkit.Location;
import org.bukkit.Material;

public class RandomPlus {

    static final java.util.Random rn = new java.util.Random();

    public static int between(int minimum, int maximum) {
        if (maximum < minimum) {
            maximum = minimum;
        }
        return rn.nextInt(maximum - minimum + 1) + minimum;
    }

    public static double between(double minimum, double maximum) {
        if (maximum < minimum) {
            maximum = minimum;
        }
        return minimum + (maximum - minimum) * rn.nextDouble();
    }

    public static Location location(Location loc, final int d) {
        double xOff = between(-d, d);
        double zOff = between(-d, d);

        loc = loc.subtract(xOff, 0, zOff);
        loc.setY(loc.getY() - 2);
        if (loc.getBlock().getType() == Material.AIR) {
            return location(loc, d + 1);
        }
        loc.setY(loc.getY() + 1);
        if (loc.getBlock().getType() == Material.AIR) {
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

    public static java.util.Random getRandom() {
        return rn;
    }
}

