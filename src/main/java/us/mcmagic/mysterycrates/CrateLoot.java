package us.mcmagic.mysterycrates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.concurrent.ThreadLocalRandom;

public class CrateLoot {

    public enum Rarity {
        VERY_COMMON(600, "Very Common"),
        COMMON(400, ChatColor.GRAY + "Common"),
        UNCOMMON(200, "Uncommon"),
        UNIQUE(100, "Unique"),
        RARE(50, ChatColor.DARK_PURPLE + "Rare"),
        ULTRA_RARE(25, "Ultra Rare"),
        LEGENDARY(10, "Legendary");
        private int weight;
        private String label;
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

    private Material material;
    private int min, max;
    private Rarity rarity;
    private byte data;
    private String name;
    private int lastStackAmount;

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
    }
    private int generateStackAmount(int min, int max) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        lastStackAmount = random.nextInt(min, max+1);
        return lastStackAmount;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public ItemStack getStack() {
        ItemStack stack = new ItemStack(material, generateStackAmount(min, max), data);
        return stack;
    }

    public String getFoundMessage() {
        return ChatColor.BLUE + "You found " + ChatColor.GOLD + lastStackAmount+ "x " + ChatColor.RESET + getRarity().getLabel() + " " + name + ChatColor.BLUE + "!";
    }
}
