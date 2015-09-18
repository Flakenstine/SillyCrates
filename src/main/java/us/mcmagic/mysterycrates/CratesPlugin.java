package us.mcmagic.mysterycrates;

import me.giinger.sh.SimpleHolograms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import us.mcmagic.mysterycrates.util.AddGlow;
import us.mcmagic.mysterycrates.util.FileUtil;

import java.io.File;
import java.lang.reflect.Field;

public final class CratesPlugin extends JavaPlugin {

    private static CratesPlugin instance;
    private static CratesManager manager;
    private static ItemStack crate;
    public static final File configFile = new File("plugins/MysteryCrates/config.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    @Override
    public void onEnable() {
        instance = this;
        saveConfig();
        FileUtil.setupConfig();
        manager = new CratesManager();
        manager.populate();
        getCommand("crate").setExecutor(new CrateCommand());
        crate = registerItem();
    }

    @Override
    public void onDisable() {
        for (Crate crate : manager.getCrates()) {
            SimpleHolograms.getHologramManager().removeHologram(crate.getHologramID().toString(), false);
        }
    }

    private ItemStack registerItem() {
        ItemStack crate = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = crate.getItemMeta();
        meta.setDisplayName(Crate.NAME);
        crate.setItemMeta(meta);
        AddGlow.makeGlow(crate);
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

    private void reload() {
        this.reloadConfig();
        config = YamlConfiguration.loadConfiguration(configFile);
        manager = new CratesManager();
        manager.populate();
    }

    public static String getStringFromLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static Location getLocationFromString(String s) {
        String[] loc = s.split(",");
        return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
    }
}
