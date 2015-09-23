package us.mcmagic.sillycrates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import us.mcmagic.sillycrates.time.PlayerListener;
import us.mcmagic.sillycrates.time.TimeTracker;
import us.mcmagic.sillycrates.util.FileUtil;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class CratesPlugin extends JavaPlugin implements Filter {

    private static CratesPlugin instance;
    private CratesManager manager;
    private static ItemStack crate;
    private TimeTracker tracker;
    private Logger log = getLogger().getParent();
    public static String CHAT_PREFIX;


    public static String[] moduleLog = {
            "▐■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▐",
            "▐           </SillyCrates>            ▐",
            "▐ Plugin Enabled...                   ▐",
            "▐ Author: Marinated                   ▐",
            "▐ Usage:                              ▐",
            "▐ Place the crate and open for a      ▐",
            "▐ silly surprise!                     ▐",
            "▐■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▮■▐"
    };

    public CratesPlugin() {
        getLogger().setFilter(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        FileUtil.setupConfig();
        FileUtil.setupPlayers();
        manager = new CratesManager();
        manager.populate();
        tracker = new TimeTracker();
        getCommand("crate").setExecutor(new CrateCommand());
        crate = registerItem();
        registerListeners();
        loadConfiguration();
        for (String s : moduleLog) {
            log.info(s);
        }
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() { }

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

    public TimeTracker getTimeTracker() {
        return tracker;
    }

    public void reload() {
        this.reloadConfig();
        FileUtil.configYaml = YamlConfiguration.loadConfiguration(FileUtil.configFile);
        FileUtil.playerYaml = YamlConfiguration.loadConfiguration(FileUtil.playersFile);
        loadConfiguration();
    }

    private void loadConfiguration() {
        CHAT_PREFIX = CratesManager.colorize((String) FileUtil.configYaml.get("prefix"));
    }

    public static String getStringFromLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static Location getLocationFromString(String s) {
        String[] loc = s.split(",");
        return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
    }

    public boolean isLoggable(LogRecord record) {
        String message = record.getMessage();
        String[] states = {"Loading", "Enabling", "Disabling"};
        for (String state : states) {
            if (message.equals("[" + getName() + "] " + state  + " " + getName() + " v" + getDescription().getVersion())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Nope. Nope. Nope.
     * @param φ
     * @return nope.avi
     * @throws Throwable
     */
    protected final synchronized static strictfp boolean $valid_java(boolean φ) throws Throwable {
        do do do do do do do do
            break;
        while (false); while (false); while (false); while (false); while (false); while (false); while (false); while (false);
        return φ |= φ |= φ |= false;
    }{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}
}