package us.mcmagic.sillycrates;

import com.sun.istack.internal.Nullable;
import me.giinger.sh.Events.HologramInteractEvent;
import me.giinger.sh.SimpleHolograms;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import us.mcmagic.sillycrates.util.FileUtil;
import us.mcmagic.sillycrates.util.ParticleEffect;
import us.mcmagic.sillycrates.util.RandomUtils;
import us.mcmagic.sillycrates.util.WeightedList;

import java.util.*;

public class CratesManager implements Listener {

    private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    private final boolean cooldownEnabled = false;
    private final int cooldownTimer = 5;
    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final HashSet<Crate> crates = new HashSet<>();

    private static final WeightedList<CrateLoot> loot = new WeightedList<CrateLoot>() {{
        put(new CrateLoot("Stone", CrateLoot.Rarity.VERY_COMMON, Material.STONE, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Polished Granite", CrateLoot.Rarity.VERY_COMMON, Material.STONE, (byte) 0x2, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Polished Andesite", CrateLoot.Rarity.VERY_COMMON, Material.STONE, (byte) 0x6,  1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Grass", CrateLoot.Rarity.VERY_COMMON, Material.GRASS, 1, 16), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Dirt", CrateLoot.Rarity.VERY_COMMON, Material.DIRT, 1, 64), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Podzol", CrateLoot.Rarity.UNCOMMON, Material.DIRT, (byte) 0x2, 1, 32), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Cobblestone", CrateLoot.Rarity.VERY_COMMON, Material.COBBLESTONE, 16, 64), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Wood Planks", CrateLoot.Rarity.VERY_COMMON, Material.WOOD, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Spruce Wood Planks", CrateLoot.Rarity.VERY_COMMON, Material.WOOD, (byte) 0x1, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Jungle Wood Planks", CrateLoot.Rarity.VERY_COMMON, Material.WOOD, (byte) 0x3, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Acacia Wood Planks", CrateLoot.Rarity.VERY_COMMON, Material.WOOD, (byte) 0x4, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Dark Oak Wood Planks", CrateLoot.Rarity.VERY_COMMON, Material.WOOD, (byte) 0x5, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Dark Oak Sapling", CrateLoot.Rarity.VERY_COMMON, Material.SAPLING, (byte) 0x5, 1, 10), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Sand", CrateLoot.Rarity.VERY_COMMON, Material.SAND, 5, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Red Sand", CrateLoot.Rarity.VERY_COMMON, Material.SAND, (byte) 0x1, 5, 16), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Gravel", CrateLoot.Rarity.VERY_COMMON, Material.GRAVEL, 1, 32), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Gold Ore", CrateLoot.Rarity.RARE, Material.GOLD_ORE, 1, 16), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Iron Ore", CrateLoot.Rarity.UNIQUE, Material.IRON_ORE, 1, 6), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Coal Ore", CrateLoot.Rarity.UNIQUE, Material.COAL_ORE, 1, 32), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Oak Wood", CrateLoot.Rarity.VERY_COMMON, Material.LOG, 1, 16), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Dark Oak Wood", CrateLoot.Rarity.VERY_COMMON, Material.LOG_2, (byte) 0x1, 1, 16), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Oak Leaves", CrateLoot.Rarity.VERY_COMMON, Material.LEAVES, 1, 6), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Sponge", CrateLoot.Rarity.ULTRA_RARE, Material.SPONGE, 1, 10), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Wet Sponge", CrateLoot.Rarity.ULTRA_RARE, Material.SPONGE, (byte) 0x1, 1, 10), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Glass", CrateLoot.Rarity.UNIQUE, Material.GLASS, 1, 12), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Lapis Lazuli Ore", CrateLoot.Rarity.UNIQUE, Material.LAPIS_ORE, 1, 6), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Lapis Lazuli Block", CrateLoot.Rarity.UNIQUE, Material.LAPIS_BLOCK, 1, 3), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Dispenser", CrateLoot.Rarity.ULTRA_RARE, Material.DISPENSER, 1, 1), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Note Block", CrateLoot.Rarity.SUPER_RARE, Material.NOTE_BLOCK, 1, 2), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Powered Rail", CrateLoot.Rarity.UNIQUE, Material.POWERED_RAIL, 1, 16), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Detector Rail", CrateLoot.Rarity.UNIQUE, Material.DETECTOR_RAIL, 1, 5), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Sticky Piston", CrateLoot.Rarity.UNIQUE, Material.PISTON_STICKY_BASE, 1, 2), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Cobweb", CrateLoot.Rarity.VERY_COMMON, Material.WEB, 1, 5), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Dead Shrub", CrateLoot.Rarity.VERY_COMMON, Material.DEAD_BUSH, 1, 16), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Piston", CrateLoot.Rarity.UNIQUE, Material.PISTON_BASE, 1, 3), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Wool", CrateLoot.Rarity.UNCOMMON, Material.WOOL, 1, 6), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Pink Wool", CrateLoot.Rarity.UNCOMMON, Material.WOOL, (byte) 0x6, 1, 3), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Dandelion", CrateLoot.Rarity.VERY_COMMON, Material.YELLOW_FLOWER, 1, 5), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Poppy", CrateLoot.Rarity.VERY_COMMON, Material.RED_ROSE, 1, 12), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Allium", CrateLoot.Rarity.VERY_COMMON, Material.RED_ROSE, (byte) 0x2, 1, 6), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Brown Mushroom", CrateLoot.Rarity.VERY_COMMON, Material.BROWN_MUSHROOM, 16, 64), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Red Mushroom", CrateLoot.Rarity.VERY_COMMON, Material.RED_MUSHROOM, 16, 64), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Gold Block", CrateLoot.Rarity.ULTRA_RARE, Material.GOLD_BLOCK, 1, 3), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Iron Block", CrateLoot.Rarity.ULTRA_RARE, Material.IRON_BLOCK, 1, 3), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Stone Slab", CrateLoot.Rarity.VERY_COMMON, Material.STONE_SLAB2, 1, 16), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("TNT", CrateLoot.Rarity.SUPER_RARE, Material.TNT, 1, 2), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Bookshelf", CrateLoot.Rarity.ULTRA_RARE, Material.BOOKSHELF, 1, 3), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Mossy Cobblestone", CrateLoot.Rarity.UNIQUE, Material.MOSSY_COBBLESTONE, 16, 32), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Obsidian", CrateLoot.Rarity.RARE, Material.OBSIDIAN, 1, 8), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Torch", CrateLoot.Rarity.UNCOMMON, Material.TORCH, 16, 32), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Mob Spawner", CrateLoot.Rarity.SUPER_RARE, Material.MOB_SPAWNER, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Chest", CrateLoot.Rarity.COMMON, Material.CHEST, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Diamond Ore", CrateLoot.Rarity.RARE, Material.DIAMOND_ORE, 1, 3), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Diamond Block", CrateLoot.Rarity.SUPER_RARE, Material.DIAMOND_BLOCK, 1, 2), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Crafting Table", CrateLoot.Rarity.VERY_COMMON, Material.WORKBENCH, 1, 1), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Furnace", CrateLoot.Rarity.UNCOMMON, Material.FURNACE, 1, 2), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Sign", CrateLoot.Rarity.COMMON, Material.SIGN, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Ladder", CrateLoot.Rarity.COMMON, Material.LADDER, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Rails", CrateLoot.Rarity.COMMON, Material.RAILS, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Lever", CrateLoot.Rarity.COMMON, Material.LEVER, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Stone Pressure Plate", CrateLoot.Rarity.VERY_COMMON, Material.STONE_PLATE, 1, 5), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Redstone Torch", CrateLoot.Rarity.COMMON, Material.REDSTONE_TORCH_ON, 16, 32), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Stone Button", CrateLoot.Rarity.VERY_COMMON, Material.STONE_BUTTON, 1, 6), CrateLoot.Rarity.VERY_COMMON.getWeight());
        put(new CrateLoot("Cactus", CrateLoot.Rarity.COMMON, Material.CACTUS, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Clay", CrateLoot.Rarity.COMMON, Material.CLAY, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Jukebox", CrateLoot.Rarity.SUPER_RARE, Material.JUKEBOX, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Fence", CrateLoot.Rarity.RARE, Material.FENCE, 16, 32), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Pumpkin", CrateLoot.Rarity.UNIQUE, Material.PUMPKIN, 1, 16), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Netherrack", CrateLoot.Rarity.ULTRA_RARE, Material.NETHERRACK, 1, 6), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Soul Sand", CrateLoot.Rarity.ULTRA_RARE, Material.SOUL_SAND, 1, 6), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Glowstone", CrateLoot.Rarity.ULTRA_RARE, Material.GLOWSTONE, 1, 12), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Cake", CrateLoot.Rarity.ULTRA_RARE, Material.CAKE, 1, 3), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Stained Glass", CrateLoot.Rarity.RARE, Material.STAINED_GLASS, (byte) 0x1, 1, 6), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Stained Glass", CrateLoot.Rarity.RARE, Material.STAINED_GLASS, (byte) 0x2, 1, 6), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Stained Glass", CrateLoot.Rarity.RARE, Material.STAINED_GLASS, (byte) 0x3, 1, 6), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Stained Glass", CrateLoot.Rarity.RARE, Material.STAINED_GLASS, (byte) 0x4, 1, 6), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Stained Glass", CrateLoot.Rarity.RARE, Material.STAINED_GLASS, (byte) 0x5, 1, 6), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Trapdoor", CrateLoot.Rarity.UNIQUE, Material.TRAP_DOOR, 1, 3), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Stone Brick", CrateLoot.Rarity.UNIQUE, Material.SMOOTH_BRICK, (byte) 0x3, 1, 4), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Iron Bars", CrateLoot.Rarity.UNCOMMON, Material.IRON_FENCE, 8, 16), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Glass Pane", CrateLoot.Rarity.RARE, Material.THIN_GLASS, 1, 5), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Melon", CrateLoot.Rarity.COMMON, Material.MELON_BLOCK, 1, 2), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Mycelium", CrateLoot.Rarity.RARE, Material.MYCEL, 1, 3), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Nether Wart", CrateLoot.Rarity.ULTRA_RARE, Material.NETHER_WARTS, 1, 3), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Table o' Magic", CrateLoot.Rarity.SUPER_RARE, Material.ENCHANTMENT_TABLE, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("End Stone", CrateLoot.Rarity.SUPER_RARE, Material.ENDER_STONE, 16, 32), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Dragon Egg", CrateLoot.Rarity.LEGENDARY, Material.DRAGON_EGG, 1, 1), CrateLoot.Rarity.LEGENDARY.getWeight());
        put(new CrateLoot("Redstone Lamp", CrateLoot.Rarity.UNIQUE, Material.REDSTONE_LAMP_OFF, 1, 6), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Emerald Ore", CrateLoot.Rarity.RARE, Material.EMERALD_ORE, 1, 3), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Ender Chest", CrateLoot.Rarity.ULTRA_RARE, Material.ENDER_CHEST, 1, 1), CrateLoot.Rarity.ULTRA_RARE.getWeight());
        put(new CrateLoot("Emerald Block", CrateLoot.Rarity.SUPER_RARE, Material.EMERALD_BLOCK, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Beacon", CrateLoot.Rarity.SUPER_RARE, Material.BEACON, 1, 3), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Cobblestone Wall", CrateLoot.Rarity.UNIQUE, Material.COBBLE_WALL, 8, 16), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Skull", CrateLoot.Rarity.SUPER_RARE, Material.SKULL_ITEM, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Wither Head", CrateLoot.Rarity.SUPER_RARE, Material.SKULL_ITEM, (byte) 0x1, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Creeper Head", CrateLoot.Rarity.SUPER_RARE, Material.SKULL_ITEM, (byte) 0x4, 1, 1), CrateLoot.Rarity.SUPER_RARE.getWeight());
        put(new CrateLoot("Anvil", CrateLoot.Rarity.UNIQUE, Material.ANVIL, 1, 1), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Trapped Chest", CrateLoot.Rarity.UNIQUE, Material.TRAPPED_CHEST, 1, 1), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Daylight Sensor", CrateLoot.Rarity.UNIQUE, Material.DAYLIGHT_DETECTOR, 1, 2), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Redstone Block", CrateLoot.Rarity.UNIQUE, Material.REDSTONE_BLOCK, 1, 1), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Hopper", CrateLoot.Rarity.UNIQUE, Material.HOPPER, 1, 3), CrateLoot.Rarity.UNIQUE.getWeight());
        put(new CrateLoot("Quartz Block", CrateLoot.Rarity.RARE, Material.QUARTZ_BLOCK, 1, 16), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Stained Clay", CrateLoot.Rarity.UNCOMMON, Material.STAINED_CLAY, 32, 64), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Stained Clay", CrateLoot.Rarity.UNCOMMON, Material.STAINED_CLAY, (byte) 0x3, 32, 64), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Stained Clay", CrateLoot.Rarity.UNCOMMON, Material.STAINED_CLAY, (byte) 0x11, 32, 64), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Stained Clay", CrateLoot.Rarity.UNCOMMON, Material.STAINED_CLAY, (byte) 0x14, 32, 64), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Prismarine", CrateLoot.Rarity.RARE, Material.PRISMARINE, 10, 20), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Prismarine", CrateLoot.Rarity.RARE, Material.PRISMARINE, (byte) 0x2, 10, 30), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Carpet", CrateLoot.Rarity.UNCOMMON, Material.CARPET, 6, 12), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Carpet", CrateLoot.Rarity.UNCOMMON, Material.CARPET, (byte) 0x1, 6, 12), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Carpet", CrateLoot.Rarity.UNCOMMON, Material.CARPET, (byte) 0x3, 6, 12), CrateLoot.Rarity.UNCOMMON.getWeight());
        put(new CrateLoot("Cake", CrateLoot.Rarity.COMMON, Material.CAKE, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
    }};

    public CratesManager() {
        Bukkit.getPluginManager().registerEvents(this, CratesPlugin.getInstance());
        updateCrates();
    }

    private void updateCrates() {
        scheduler.runTaskTimer(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Crate crate : (HashSet<Crate>) crates.clone()) {
                    drawCuboidEdges(crate.getEffect(), 0.1, crate.getBlock().getLocation(), new Vector(0D, 0D, 0D), new Vector(1D, 1D, 1D));
                }
                if (cooldownEnabled) {
                    for (UUID uuid : new HashSet<>(cooldown.keySet())) {
                        int elapsed = (int) ((System.currentTimeMillis() - cooldown.get(uuid)) / 1000);
                        if (elapsed >= cooldownTimer) {
                            if (Bukkit.getPlayer(uuid) == null) continue;
                            Bukkit.getPlayer(uuid).sendMessage(colorize(CratesPlugin.CHAT_PREFIX + " &9You may now place another " + Crate.NAME + "&r&9."));
                            cooldown.remove(uuid);
                        }
                    }
                }
            }
        }, 0L, 10L);
    }

    public void register(Crate c) {
        crates.add(c);
        if (cooldownEnabled) {
            cooldown.put(c.getOwnerUUID(), System.currentTimeMillis());
        }
        FileUtil.registerCrate(c);
    }

    public void unregister(Crate c) {
        SimpleHolograms.getHologramManager().removeHologram(c.getHologramID().toString(), true);
        c.getBlock().setType(Material.AIR);
        crates.remove(c);
        FileUtil.removeCrate(c);
    }

    public void populate() {
        ConfigurationSection root = FileUtil.configYaml.getConfigurationSection("crates");
        if (root == null) {
            System.out.println("Could not populate crates list in config.yml");
            return;
        }
        for (Object section : root.getKeys(false)) {
            String name = (String) section;
            ConfigurationSection nextSection = root.getConfigurationSection(name);
            Location location = CratesPlugin.getLocationFromString(nextSection.getString("location"));
            UUID owner = UUID.fromString(nextSection.getString("owner"));
            UUID hologram = UUID.fromString(name);
            ParticleEffect effect = ParticleEffect.fromName(nextSection.getString("particle"));
            Crate c = new Crate(name, owner, hologram, location, effect);
            register(c);
        }
    }

    public HashSet<Crate> getCrates() {
        return crates;
    }

    private boolean isCrateNear(Block b) {
        for (Crate crate : crates) {
            if (crate.getBlock().getLocation().distance(b.getLocation()) < 3) {
                return true;
            }
        }
        return false;
    }

    private void error(Player player, @Nullable String message) {
        if (message != null) {
            player.sendMessage(colorize(CratesPlugin.CHAT_PREFIX + " &c" + message));
        }
        player.playSound(player.getLocation(), Sound.BURP, 100, 2);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().isSimilar(CratesPlugin.getCrateItem())) {
            if (cooldownEnabled) {
                if (cooldown.containsKey(event.getPlayer().getUniqueId())) {
                    int remaining = cooldownTimer -  (int) ((System.currentTimeMillis() - cooldown.get(event.getPlayer().getUniqueId()))/1000);
                    error(event.getPlayer(), "Uh oh! Please wait " + String.valueOf(remaining) + " seconds.");
                    event.setCancelled(true);
                    return;
                }
            }
            if (isCrateNear(event.getBlock())) {
                error(event.getPlayer(), "Uh oh! Try placing this somewhere else!");
                event.setCancelled(true);
                return;
            }
            UUID id = UUID.randomUUID();
            Crate c = new Crate(id.toString(), event.getPlayer().getUniqueId(), id, event.getBlock().getLocation(), ParticleEffect.fromName("enchantmenttable"));
            SimpleHolograms.getHologramManager().createHologram(c.getCenter().add(0, -.5, 0), c.getHologramID().toString(), Crate.NAME);
            register(c);
        } else if (event.getItemInHand().isSimilar(new ItemStack(Material.CHEST)) || event.getItemInHand().isSimilar(new ItemStack(Material.HOPPER))) {
            if (isCrateNear(event.getBlock())) {
                error(event.getPlayer(), null);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() == Crate.TYPE) {
            Player p = event.getPlayer();
            //noinspection unchecked
            for (final Crate crate : (HashSet<Crate>) crates.clone()) {
                if (compareBlockLocation(event.getClickedBlock().getLocation(), crate.getBlock().getLocation())) {
                    if (!p.getUniqueId().equals(crate.getOwnerUUID())) {
                        error(p, "Uh oh! This doesn't belong to you!");
                        p.getEyeLocation().toVector().multiply(-1);
                    } else {
                        CrateOpenEvent openEvent = new CrateOpenEvent(crate, event.getPlayer().getUniqueId());
                        Bukkit.getServer().getPluginManager().callEvent(openEvent);
                    }
                    event.setCancelled(true);
                    }
                }
        }
    }

    @EventHandler
    public void onCrateOpen(CrateOpenEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().firstEmpty() == -1) {
            error(player, "Uh oh! Looks like your inventory is full!");
            event.setCancelled(true);
            return;
        }
        CrateLoot cl = loot.get();
        unregister(event.getCrate());
        player.getInventory().addItem(cl.getStack());
        player.updateInventory();
        player.sendMessage(cl.playerMessage());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block b : new ArrayList<>(event.blockList())) {
            for (Crate c : crates) {
                if (compareBlockLocation(b.getLocation(), c.getBlock().getLocation())) {
                    event.blockList().remove(c.getBlock());
                }
            }
        }
    }

    @EventHandler
    public void onHologramInteract(HologramInteractEvent event) {
        if (event.getHologram().getText().get(0).equals(Crate.NAME)) {
            Player p = event.getPlayer();
            UUID id = UUID.fromString(event.getHologram().getId());
            Crate crate;
            if ((crate = getCrateFromHologram(id)) != null) {
                if (!p.getUniqueId().equals(crate.getOwnerUUID())) {
                    error(p, "Uh oh! This doesn't belong to you!");
                    p.getEyeLocation().toVector().multiply(-1);
                } else {
                    CrateOpenEvent openEvent = new CrateOpenEvent(crate, event.getPlayer().getUniqueId());
                    Bukkit.getServer().getPluginManager().callEvent(openEvent);
                }
            }
        }
    }

    public static boolean compareBlockLocation(Location a, Location b) {
        return (a.getBlockX() == b.getBlockX() && a.getBlockY() == b.getBlockY() && a.getBlockZ() == b.getBlockZ());
    }

    public static BlockFace playerFacing(double yaw) {
        while (yaw < 0) {
            yaw += 360;
        }
        BlockFace blockFaces[] = {BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH};
        return blockFaces[(int) Math.round(( yaw % 360) / 45)];
    }

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void playRandomSound(Player player, Location location, float a, float b) {
        player.playSound(location, Sound.values()[((int) (Math.random() * Sound.values().length))], a, b);
    }

    private Crate getCrateFromHologram(UUID uuid) {
        for (Crate crate : getCrates()) {
            if (crate.getHologramID().equals(uuid)) {
                return crate;
            }
        }
        return null;
    }

    public static void sphereAnimation(ParticleEffect effect, Location location, double radius, int particles) {
        for (int i = 0; i < particles; i++) {
            Vector vector = RandomUtils.getRandomVector().multiply(radius);
            location.add(vector);
            effect.display(0, 0, 0, 0F, 1, location, 10D);
            location.subtract(vector);
        }
    }

    public static void drawCuboidEdges(ParticleEffect effect, double spread, Location location, Vector v1, Vector v2) {
        List<Vector> result = new ArrayList<>();

        for (double x = v1.getX(); x <=  v2.getX(); x+=spread) {
            result.add(new Vector(x, v1.getBlockY(), v1.getBlockZ()));
            result.add(new Vector(x, v1.getBlockY(), v2.getBlockZ()));
            result.add(new Vector(x, v2.getBlockY(), v1.getBlockZ()));
            result.add(new Vector(x, v2.getBlockY(), v2.getBlockZ()));
        }
        for (double y = v1.getY(); y < v2.getY(); y+=spread) {
            result.add(new Vector(v1.getBlockX(), y, v1.getBlockZ()));
            result.add(new Vector(v1.getBlockX(), y, v2.getBlockZ()));
            result.add(new Vector(v2.getBlockX(), y, v1.getBlockZ()));
            result.add(new Vector(v2.getBlockX(), y, v2.getBlockZ()));
        }
        for (double z = v1.getZ(); z < v2.getZ(); z+=spread) {
            result.add(new Vector(v1.getBlockX(), v1.getBlockY(), z));
            result.add(new Vector(v1.getBlockX(), v2.getBlockY(), z));
            result.add(new Vector(v2.getBlockX(), v1.getBlockY(), z));
            result.add(new Vector(v2.getBlockX(), v2.getBlockY(), z));
        }

        for (Vector vector : result) {
            location.add(vector);
            effect.display(0, 0, 0, 0F, 1, location, 10D);
            location.subtract(vector);
        }
    }
}
