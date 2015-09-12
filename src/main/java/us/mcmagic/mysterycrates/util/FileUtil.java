package us.mcmagic.mysterycrates.util;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import us.mcmagic.mysterycrates.Crate;
import us.mcmagic.mysterycrates.CratesPlugin;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;

public class FileUtil {

    private FileUtil() {
        ;
    }

    public static final void setupConfig() {
        YamlConfiguration config = (YamlConfiguration) CratesPlugin.config;
        config.createSection("crates");
        if (CratesPlugin.configFile.exists()) {
            return;
        }
        try {
            CratesPlugin.configFile.createNewFile();
            config.save(CratesPlugin.configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not setup config.yml", e);
        }
    }

    public static final void registerCrate(Crate c) {
        YamlConfiguration config = (YamlConfiguration) CratesPlugin.config;
        ConfigurationSection crates = config.getConfigurationSection("crates");
        if (crates.getConfigurationSection(c.getId()) != null) {
            return;
        }
        ConfigurationSection crate  = crates.createSection(c.getId());
        crate.set("location", CratesPlugin.getStringFromLocation(c.getBlock().getLocation()));
        crate.set("owner", c.getOwnerUUID().toString());
        crate.set("hologram", c.getHologramID().toString());
        crate.set("particle", c.getEffect().getName());
        try {
            config.save(CratesPlugin.configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not save crate to " + CratesPlugin.configFile, e);
            CratesPlugin.getInstance().getManager().getCrates().remove(c);
        }
    }

    public static final void removeCrate(Crate c) {
        CratesPlugin.config.set("crates." + c.getId(), null);
        try {
            CratesPlugin.config.save(CratesPlugin.configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could remove crate from " + CratesPlugin.configFile, e);
        }
    }

    public static final void loadCrates() {
        ConfigurationSection root = CratesPlugin.config.getConfigurationSection("crates");
        Iterator i = root.getKeys(false).iterator();
        System.out.println("Test");
        while (i.hasNext()) {
            System.out.println("Loaded one crate");
            String id = (String) i.next();
            Location location  = CratesPlugin.getLocationFromString(root.getString("crates." + id + ".location"));
            UUID owner = UUID.fromString(root.getString("crates." + id + ".owner"));
            UUID hologram = UUID.fromString(root.getString("crates." + id + ".hologram"));
            ParticleEffect effect = ParticleEffect.fromName(root.getString("crates." + id + ".particle"));
            Crate c = new Crate(id, owner, hologram, location, effect);
            CratesPlugin.getInstance().getManager().register(c);
        }
    }
}
