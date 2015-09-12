package us.mcmagic.mysterycrates.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import us.mcmagic.mysterycrates.Crate;
import us.mcmagic.mysterycrates.CratesPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class FileUtil {

    private FileUtil() {
        ;
    }

    public static final void setupConfig() {
        YamlConfiguration config = (YamlConfiguration) CratesPlugin.config;
        if (CratesPlugin.configFile.exists()) {
            return;
        }
        try {
            CratesPlugin.configFile.createNewFile();
            config.createSection("crates");
            config.save(CratesPlugin.configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not setup config.yml", e);
        }
    }

    public static final void registerCrate(Crate c) {
        ConfigurationSection crates = CratesPlugin.config.getConfigurationSection("crates");
        if (crates.getConfigurationSection(c.getId()) != null) {
            return;
        }
        ConfigurationSection crate  = crates.createSection(c.getId());
        crate.set("location", CratesPlugin.getStringFromLocation(c.getBlock().getLocation()));
        crate.set("owner", c.getOwnerUUID().toString());
        crate.set("particle", c.getEffect().getName());
        try {
            CratesPlugin.config.save(CratesPlugin.configFile);
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
}
