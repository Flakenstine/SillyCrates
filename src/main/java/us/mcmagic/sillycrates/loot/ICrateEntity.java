package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;

public interface ICrateEntity extends ICrateRandom {
    void spawn(Location where);
}
