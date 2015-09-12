package us.mcmagic.mysterycrates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import us.mcmagic.mysterycrates.utils.FileUtil;
import us.mcmagic.mysterycrates.utils.Glow;

import java.io.File;
import java.lang.reflect.Field;

public final class CratesPlugin extends JavaPlugin {

    private static CratesPlugin instance;
    private static CratesManager manager;
    private static ItemStack crate;
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(new File("/plugins/MysteryCrates/config.yml"));

    @Override
    public void onEnable() {
        instance = this;

        saveConfig();
        FileUtil.setupConfig();

        manager = new CratesManager();
        getCommand("crate").setExecutor(new CrateCommand());
        crate = registerItem();
    }

    @Override
    public void onDisable() { }

    private void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(69);
            Enchantment.registerEnchantment(glow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ItemStack registerItem() {
        ItemStack crate = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = crate.getItemMeta();
        meta.setDisplayName(Crate.NAME);
        crate.setItemMeta(meta);
        return crate;
    }

    public static ItemStack getCrateItem() {
        return crate;
    }

    public static CratesPlugin getInstance() {
        return instance;
    }

    public CratesManager getManager() {
        return manager;
    }

    public static String getStringFromLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static Location getLocationFromString(String s) {
        String[] loc = s.split(",");
        return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
    }
}
