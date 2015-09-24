package us.mcmagic.sillycrates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;

public class CrateLoot {


    public enum Rarity {
        VERY_COMMON(600, "&8Very Common"),
        COMMON(400, "&7Common"),
        UNCOMMON(200, "&2Uncommon"),
        UNIQUE(100, "&aUnique"),
        RARE(50, "&1Rare"),
        ULTRA_RARE(25, "&9Ultra Rare"),
        SUPER_RARE(10, "&dSuper Rare"),
        LEGENDARY(1, "&5Legendary");
        private final int weight;
        private final String label;
        Rarity(int weight, String label) {
            this.weight = weight;
            this.label = label;
        }
        public int getWeight() {
            return this.weight;
        }
        public String getLabel() {
            return this.label;
        }
    }

    private ItemStack stack;
    private Material material;
    private final int min;
    private final int max;
    private final Rarity rarity;
    private byte data;
    private final String name;
    private int stackAmount;
    private static final SecureRandom random = new SecureRandom();

    public CrateLoot(Rarity rarity, Material type, int min, int max) {
        this(rarity, type, (byte) 0x0, min, max);
    }

    public CrateLoot(String name, Rarity rarity, Material type, int min, int max) {
        this(name, rarity, type, (byte) 0x0, min, max);
    }

    public CrateLoot(Rarity rarity, Material type, byte data,  int min, int max) {
        this("Item", rarity, type, data, min, max);
    }

    public CrateLoot(String name, Rarity rarity, Material type, byte data, int min, int max) {
        this.name = CratesManager.colorize(name);
        this.data = 0;
        this.rarity = rarity;
        this.min = min;
        this.max = max;
        this.material = type;
        this.data = data;
        this.stack = new ItemStack(material, 1, data);
    }

    public CrateLoot(String name, Rarity rarity, ItemStack stack, int min, int max) {
        this.name = name;
        this.rarity = rarity;
        this.stack = stack;
        this.min = min;
        this.max = max;
        setupItemStack();
    }

    private int generateStackAmount(int min, int max) {
        return random.nextInt(max-min+1) + min;
    }

    private void setupItemStack() {
        // If Potion, for instance.
        if (stack != null) {

        }
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ItemStack getStack() {
        this.stackAmount = generateStackAmount(min, max);
        stack.setAmount(stackAmount);
        return stack;
    }

    public String[] playerMessage() {
        return new String[] {"", "", ""};
    }
}
