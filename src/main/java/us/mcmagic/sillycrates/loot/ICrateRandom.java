package us.mcmagic.sillycrates.loot;

import java.security.SecureRandom;

public interface ICrateRandom {
    SecureRandom random = new SecureRandom();
    Rarity getRarity();
    int generateAmount(int min, int max);
}

