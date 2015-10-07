package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;

import java.security.SecureRandom;

public interface ICrateRandom {
    SecureRandom random = new SecureRandom();
    Rarity getRarity();
    void play(Location location);
}

