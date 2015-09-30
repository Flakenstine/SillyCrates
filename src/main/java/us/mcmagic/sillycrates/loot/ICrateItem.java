package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;

import java.security.SecureRandom;

public interface ICrateItem extends ICrateRandom {
    void play(Location location);
}

