package us.mcmagic.sillycrates.loot;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CrateItemStacks implements ICrateRandom {

    private final Rarity rarity;
    private List<ItemStack> items;

    public CrateItemStacks(Rarity value, List<ItemStack> items) {
        this.rarity = value;
        this.items = items;

    }
    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public void play(Location location) {

    }
}
