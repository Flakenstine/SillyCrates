package us.mcmagic.sillycrates.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import us.mcmagic.sillycrates.Crate;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.time.TrackedPlayer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileUtil {

    private FileUtil() {
    }

    public static final File configFile = new File("plugins/SillyCrates/config.yml");
    public static final File playersFile = new File("plugins/SillyCrates/players.yml");
    public static final File schematics = new File("plugins/SillyCrates/structures");
    public static YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(configFile);
    public static YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playersFile);

    public static void setupConfig() {
        try {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdir();
            }
            if (!schematics.exists()) {
                schematics.mkdir();
            }
            if (!configFile.createNewFile()) {
                return;
            }
            configYaml.createSection("crates");
            configYaml.set("prefix", "&0&l■&b&lSillyCrates&0&l■");
            configYaml.save(configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not setup config.yml");
            Bukkit.getPluginManager().disablePlugin(CratesPlugin.getInstance());
        }
    }

    public static void setupPlayers() {
        try {
            if (playersFile.createNewFile()) {
                playerYaml.createSection("players");
                playerYaml.save(playersFile);
            }
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not setup players.yml");
            Bukkit.getPluginManager().disablePlugin(CratesPlugin.getInstance());
        }
    }

    public static ConfigurationSection getPlayersSection() throws IOException {
        ConfigurationSection section = playerYaml.getConfigurationSection("players");
        if (section == null) {
            playerYaml.createSection("players");
            playerYaml.save(playersFile);
        }
        return section;
    }
    public static void registerTrackedPlayer(TrackedPlayer p, final String name) throws IOException {
        ConfigurationSection section = getPlayersSection();
        if (section == null) {
            return;
        }
        ConfigurationSection player = section.createSection(p.getUniqueId().toString());
        player.set("minutes", p.getPlayTime());
        player.set("crates", p.getAvailableCrates());
        try {
            player.set("name", Bukkit.getPlayer(p.getUniqueId()).getName());
        } catch (NullPointerException e) {
            // Try again next update
        }
    }

    public static void updateTrackedPlayer(TrackedPlayer p) throws IOException {
        ConfigurationSection section = getPlayersSection();
        ConfigurationSection player = section.getConfigurationSection(p.getUniqueId().toString());
        if (player == null) {
            return;
        }
        player.set("minutes", p.getPlayTime());
        player.set("crates", p.getAvailableCrates());
        if (player.get("name") == null) {
            try {
                player.set("name", Bukkit.getPlayer(p.getUniqueId()).getName());
            } catch (NullPointerException e) {
                // Try again next update
            }
        }
        playerYaml.save(playersFile);
    }

    public static void registerCrate(Crate c) {
        ConfigurationSection crates = configYaml.getConfigurationSection("crates");
        if (crates.getConfigurationSection(c.getId()) != null) {
            return;
        }
        ConfigurationSection crate  = crates.createSection(c.getId());
        crate.set("location", CratesPlugin.getStringFromLocation(c.getBlock().getLocation()));
        crate.set("owner", c.getOwnerUUID().toString());
        crate.set("particle", c.getEffect().getName());
        try {
            configYaml.save(configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could not save crate to " + configFile, e);
            CratesPlugin.getInstance().getManager().getCrates().remove(c);
        }
    }

    public static void removeCrate(Crate c) {
        configYaml.set("crates." + c.getId(), null);
        try {
            configYaml.save(configFile);
        } catch (IOException e) {
            CratesPlugin.getInstance().getLogger().log(Level.SEVERE, "Could remove crate from " + configFile, e);
        }
    }
}
