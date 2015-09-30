package us.mcmagic.sillycrates.loot;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import us.mcmagic.sillycrates.CratesManager;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.SillyCratesMessage;

public class CrateItemLoot implements ICrateItem {

    private ItemStack stack;
    private Material material;
    private final int min;
    private final int max;
    private final Rarity rarity;
    private byte data;
    private final String name;
    private int stackAmount;

    public CrateItemLoot(Rarity rarity, Material type, int min, int max) {
        this(rarity, type, (byte) 0x0, min, max);
    }

    public CrateItemLoot(String name, Rarity rarity, Material type, int min, int max) {
        this(name, rarity, type, (byte) 0x0, min, max);
    }

    public CrateItemLoot(Rarity rarity, Material type, byte data, int min, int max) {
        this("Item", rarity, type, data, min, max);
    }

    public CrateItemLoot(String name, Rarity rarity, Material type, byte data, int min, int max) {
        this.name = CratesManager.colorize(name);
        this.data = 0;
        this.rarity = rarity;
        this.min = min;
        this.max = max;
        this.material = type;
        this.data = data;
        this.stack = new ItemStack(material, 1, data);
    }

    public CrateItemLoot(String name, Rarity rarity, ItemStack stack, int min, int max) {
        this.name = name;
        this.rarity = rarity;
        this.stack = stack;
        this.min = min;
        this.max = max;
        setupItemStack();
    }

    @Override
    public int generateAmount(int min, int max) {
        return random.nextInt(max-min+1) + min;
    }

    private void setupItemStack() {
        //TODO Potions
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

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    public ItemStack getStack() {
        this.stackAmount = generateAmount(min, max);
        stack.setAmount(stackAmount);
        return stack;
    }

    @Override
    public void play(Location loc) {
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
