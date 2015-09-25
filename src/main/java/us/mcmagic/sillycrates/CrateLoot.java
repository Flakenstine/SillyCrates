package us.mcmagic.sillycrates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.lang.reflect.Field;
import java.security.SecureRandom;

public class CrateLoot {


    public enum Rarity {
        VERY_COMMON(600, "&8Very Common", Color.BLACK),
        COMMON(400, "&7Common", Color.GRAY),
        UNCOMMON(200, "&2Uncommon", Color.WHITE),
        UNIQUE(100, "&aUnique", Color.GREEN),
        RARE(50, "&1Rare", Color.BLUE),
        ULTRA_RARE(25, "&9Ultra Rare", Color.fromBGR(55, 0, 0)),
        SUPER_RARE(10, "&dSuper Rare", Color.FUCHSIA),
        LEGENDARY(1, "&5Legendary", Color.PURPLE);
        private final Color color;
        private final int weight;
        private final String label;
        Rarity(int weight, String label, Color color) {
            this.weight = weight;
            this.label = label;
            this.color = color;
        }
        public int getWeight() {
            return this.weight;
        }
        public String getLabel() {
            return this.label;
        }
        public Color getFireworkColor() {
            return color;
        }

        @Override
        public String toString() {
            return SillyCratesMessage.format(label, '&');
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

    public void doFirework(Location loc) {
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
            if (entity instanceof Player) {
                SillyCratesMessage.sendWithoutHeader(rarity.toString(), (Player) entity);
            }
        }
        final Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect fx = FireworkEffect.builder().withColor(rarity.getFireworkColor()).with(FireworkEffect.Type.BALL).build();
        meta.addEffect(fx);
        meta.setPower(1);
        firework.setFireworkMeta(meta);
        Bukkit.getScheduler().runTaskLater(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                firework.detonate();
            }
        }, 3L);
    }
}
