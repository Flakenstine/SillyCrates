package us.mcmagic.sillycrates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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
import us.mcmagic.sillycrates.loot.CrateEntityLoot;
import us.mcmagic.sillycrates.loot.CrateItemLoot;
import us.mcmagic.sillycrates.loot.ICrateRandom;
import us.mcmagic.sillycrates.loot.Rarity;
import us.mcmagic.sillycrates.loot.entity.CrateEntityType;
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

    private static final WeightedList<ICrateRandom> loot = new WeightedList<ICrateRandom>() {{
//        put(new CrateItemLoot("Stone", Rarity.VERY_COMMON, Material.STONE, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Polished Granite", Rarity.VERY_COMMON, Material.STONE, (byte) 0x2, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Polished Andesite", Rarity.VERY_COMMON, Material.STONE, (byte) 0x6,  1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Grass", Rarity.VERY_COMMON, Material.GRASS, 1, 16), Rarity.VERY_COMMON.getWeight());
        put(new CrateItemLoot("Dirt", Rarity.VERY_COMMON, Material.DIRT, 1, 64), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Podzol", Rarity.UNCOMMON, Material.DIRT, (byte) 0x2, 1, 32), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Cobblestone", Rarity.VERY_COMMON, Material.COBBLESTONE, 16, 64), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Wood Planks", Rarity.VERY_COMMON, Material.WOOD, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Spruce Wood Planks", Rarity.VERY_COMMON, Material.WOOD, (byte) 0x1, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Jungle Wood Planks", Rarity.VERY_COMMON, Material.WOOD, (byte) 0x3, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Acacia Wood Planks", Rarity.VERY_COMMON, Material.WOOD, (byte) 0x4, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Dark Oak Wood Planks", Rarity.VERY_COMMON, Material.WOOD, (byte) 0x5, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Dark Oak Sapling", Rarity.VERY_COMMON, Material.SAPLING, (byte) 0x5, 1, 10), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Sand", Rarity.VERY_COMMON, Material.SAND, 5, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Red Sand", Rarity.VERY_COMMON, Material.SAND, (byte) 0x1, 5, 16), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Gravel", Rarity.VERY_COMMON, Material.GRAVEL, 1, 32), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Gold Ore", Rarity.RARE, Material.GOLD_ORE, 1, 16), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Iron Ore", Rarity.UNIQUE, Material.IRON_ORE, 1, 6), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Coal Ore", Rarity.UNIQUE, Material.COAL_ORE, 1, 32), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Oak Wood", Rarity.VERY_COMMON, Material.LOG, 1, 16), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Dark Oak Wood", Rarity.VERY_COMMON, Material.LOG_2, (byte) 0x1, 1, 16), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Oak Leaves", Rarity.VERY_COMMON, Material.LEAVES, 1, 6), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Sponge", Rarity.ULTRA_RARE, Material.SPONGE, 1, 10), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Wet Sponge", Rarity.ULTRA_RARE, Material.SPONGE, (byte) 0x1, 1, 10), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Glass", Rarity.UNIQUE, Material.GLASS, 1, 12), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Lapis Lazuli Ore", Rarity.UNIQUE, Material.LAPIS_ORE, 1, 6), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Lapis Lazuli Block", Rarity.UNIQUE, Material.LAPIS_BLOCK, 1, 3), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Dispenser", Rarity.ULTRA_RARE, Material.DISPENSER, 1, 1), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Note Block", Rarity.SUPER_RARE, Material.NOTE_BLOCK, 1, 2), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Powered Rail", Rarity.UNIQUE, Material.POWERED_RAIL, 1, 16), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Detector Rail", Rarity.UNIQUE, Material.DETECTOR_RAIL, 1, 5), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Sticky Piston", Rarity.UNIQUE, Material.PISTON_STICKY_BASE, 1, 2), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Cobweb", Rarity.VERY_COMMON, Material.WEB, 1, 5), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Dead Shrub", Rarity.VERY_COMMON, Material.DEAD_BUSH, 1, 16), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Piston", Rarity.UNIQUE, Material.PISTON_BASE, 1, 3), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Wool", Rarity.UNCOMMON, Material.WOOL, 1, 6), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Pink Wool", Rarity.UNCOMMON, Material.WOOL, (byte) 0x6, 1, 3), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Dandelion", Rarity.VERY_COMMON, Material.YELLOW_FLOWER, 1, 5), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Poppy", Rarity.VERY_COMMON, Material.RED_ROSE, 1, 12), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Allium", Rarity.VERY_COMMON, Material.RED_ROSE, (byte) 0x2, 1, 6), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Brown Mushroom", Rarity.VERY_COMMON, Material.BROWN_MUSHROOM, 16, 64), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Red Mushroom", Rarity.VERY_COMMON, Material.RED_MUSHROOM, 16, 64), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Gold Block", Rarity.ULTRA_RARE, Material.GOLD_BLOCK, 1, 3), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Iron Block", Rarity.ULTRA_RARE, Material.IRON_BLOCK, 1, 3), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Stone Slab", Rarity.VERY_COMMON, Material.STONE_SLAB2, 1, 16), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("TNT", Rarity.SUPER_RARE, Material.TNT, 1, 2), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Bookshelf", Rarity.ULTRA_RARE, Material.BOOKSHELF, 1, 3), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Mossy Cobblestone", Rarity.UNIQUE, Material.MOSSY_COBBLESTONE, 16, 32), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Obsidian", Rarity.RARE, Material.OBSIDIAN, 1, 8), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Torch", Rarity.UNCOMMON, Material.TORCH, 16, 32), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Mob Spawner", Rarity.SUPER_RARE, Material.MOB_SPAWNER, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Chest", Rarity.COMMON, Material.CHEST, 1, 1), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Diamond Ore", Rarity.RARE, Material.DIAMOND_ORE, 1, 3), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Diamond Block", Rarity.SUPER_RARE, Material.DIAMOND_BLOCK, 1, 2), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Crafting Table", Rarity.VERY_COMMON, Material.WORKBENCH, 1, 1), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Furnace", Rarity.UNCOMMON, Material.FURNACE, 1, 2), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Sign", Rarity.COMMON, Material.SIGN, 1, 5), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Ladder", Rarity.COMMON, Material.LADDER, 1, 16), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Rails", Rarity.COMMON, Material.RAILS, 1, 16), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Lever", Rarity.COMMON, Material.LEVER, 1, 6), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Stone Pressure Plate", Rarity.VERY_COMMON, Material.STONE_PLATE, 1, 5), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Redstone Torch", Rarity.COMMON, Material.REDSTONE_TORCH_ON, 16, 32), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Stone Button", Rarity.VERY_COMMON, Material.STONE_BUTTON, 1, 6), Rarity.VERY_COMMON.getWeight());
//        put(new CrateItemLoot("Cactus", Rarity.COMMON, Material.CACTUS, 1, 5), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Clay", Rarity.COMMON, Material.CLAY, 1, 16), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Jukebox", Rarity.SUPER_RARE, Material.JUKEBOX, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Fence", Rarity.RARE, Material.FENCE, 16, 32), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Pumpkin", Rarity.UNIQUE, Material.PUMPKIN, 1, 16), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Netherrack", Rarity.ULTRA_RARE, Material.NETHERRACK, 1, 6), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Soul Sand", Rarity.ULTRA_RARE, Material.SOUL_SAND, 1, 6), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Glowstone", Rarity.ULTRA_RARE, Material.GLOWSTONE, 1, 12), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Stained Glass", Rarity.RARE, Material.STAINED_GLASS, (byte) 1, 1, 6), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Stained Glass", Rarity.RARE, Material.STAINED_GLASS, (byte) 2, 1, 6), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Stained Glass", Rarity.RARE, Material.STAINED_GLASS, (byte) 3, 1, 6), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Stained Glass", Rarity.RARE, Material.STAINED_GLASS, (byte) 4, 1, 6), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Stained Glass", Rarity.RARE, Material.STAINED_GLASS, (byte) 5, 1, 6), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Trapdoor", Rarity.UNIQUE, Material.TRAP_DOOR, 1, 3), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Stone Brick", Rarity.UNIQUE, Material.SMOOTH_BRICK, (byte) 0x3, 1, 4), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Iron Bars", Rarity.UNCOMMON, Material.IRON_FENCE, 8, 16), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Glass Pane", Rarity.RARE, Material.THIN_GLASS, 1, 5), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Melon", Rarity.COMMON, Material.MELON_BLOCK, 1, 2), Rarity.COMMON.getWeight());
//        put(new CrateItemLoot("Mycelium", Rarity.RARE, Material.MYCEL, 1, 3), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Nether Wart", Rarity.ULTRA_RARE, Material.NETHER_WARTS, 1, 3), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Table o' Magic", Rarity.SUPER_RARE, Material.ENCHANTMENT_TABLE, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("End Stone", Rarity.SUPER_RARE, Material.ENDER_STONE, 16, 32), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Dragon Egg", Rarity.LEGENDARY, Material.DRAGON_EGG, 1, 1), Rarity.LEGENDARY.getWeight());
//        put(new CrateItemLoot("Redstone Lamp", Rarity.UNIQUE, Material.REDSTONE_LAMP_OFF, 1, 6), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Emerald Ore", Rarity.RARE, Material.EMERALD_ORE, 1, 3), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Ender Chest", Rarity.ULTRA_RARE, Material.ENDER_CHEST, 1, 1), Rarity.ULTRA_RARE.getWeight());
//        put(new CrateItemLoot("Emerald Block", Rarity.SUPER_RARE, Material.EMERALD_BLOCK, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Beacon", Rarity.SUPER_RARE, Material.BEACON, 1, 3), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Cobblestone Wall", Rarity.UNIQUE, Material.COBBLE_WALL, 8, 16), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Skull", Rarity.SUPER_RARE, Material.SKULL_ITEM, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Wither Head", Rarity.SUPER_RARE, Material.SKULL_ITEM, (byte) 0x1, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Creeper Head", Rarity.SUPER_RARE, Material.SKULL_ITEM, (byte) 0x4, 1, 1), Rarity.SUPER_RARE.getWeight());
//        put(new CrateItemLoot("Anvil", Rarity.UNIQUE, Material.ANVIL, 1, 1), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Trapped Chest", Rarity.UNIQUE, Material.TRAPPED_CHEST, 1, 1), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Daylight Sensor", Rarity.UNIQUE, Material.DAYLIGHT_DETECTOR, 1, 2), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Redstone Block", Rarity.UNIQUE, Material.REDSTONE_BLOCK, 1, 1), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Hopper", Rarity.UNIQUE, Material.HOPPER, 1, 3), Rarity.UNIQUE.getWeight());
//        put(new CrateItemLoot("Quartz Block", Rarity.RARE, Material.QUARTZ_BLOCK, 1, 16), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Stained Clay", Rarity.UNCOMMON, Material.STAINED_CLAY, 32, 64), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Stained Clay", Rarity.UNCOMMON, Material.STAINED_CLAY, (byte) 3, 32, 64), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Stained Clay", Rarity.UNCOMMON, Material.STAINED_CLAY, (byte) 11, 32, 64), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Stained Clay", Rarity.UNCOMMON, Material.STAINED_CLAY, (byte) 14, 32, 64), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Prismarine", Rarity.RARE, Material.PRISMARINE, 10, 20), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Prismarine", Rarity.RARE, Material.PRISMARINE, (byte) 0x2, 10, 30), Rarity.RARE.getWeight());
//        put(new CrateItemLoot("Carpet", Rarity.UNCOMMON, Material.CARPET, 6, 12), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Carpet", Rarity.UNCOMMON, Material.CARPET, (byte) 1, 6, 12), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Carpet", Rarity.UNCOMMON, Material.CARPET, (byte) 3, 6, 12), Rarity.UNCOMMON.getWeight());
//        put(new CrateItemLoot("Cake", Rarity.COMMON, Material.CAKE, 1, 3), Rarity.COMMON.getWeight());ut(new CrateEntityLoot(EntityType.PIG_ZOMBIE, Rarity.COMMON, 1, 1), Rarity.COMMON.getWeight());
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
            ParticleEffect effect = ParticleEffect.fromName(nextSection.getString("particle"));
            Crate c = new Crate(name, owner, location, effect);
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

    public void error(Player player, String message) {
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
            if (isCrateNear(event.getBlock()) || isBlacklistedBlockNear(event.getBlock())) {
                error(event.getPlayer(), "Uh oh! Try placing this somewhere else!");
                event.setCancelled(true);
                return;
            }
            UUID id = UUID.randomUUID();
            Crate c = new Crate(id.toString(), event.getPlayer().getUniqueId(), event.getBlock().getLocation(), ParticleEffect.fromName("enchantmenttable"));
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
        ICrateRandom random = loot.get();
        if (random instanceof CrateItemLoot) {
            CrateItemLoot loot = (CrateItemLoot) random;
            player.getInventory().addItem(loot.getStack());
            player.updateInventory();
            loot.play(event.getCrate().getCenter());
            LivingEntity e = (LivingEntity) CratesPlugin.getInstance().getEntityFactory().spawn(event.getCrate().getCenter(), CrateEntityType.CRATE_ZOMBIE);
            e.setFireTicks(0);
        } else if (random instanceof CrateEntityLoot) {
            CrateEntityLoot loot = (CrateEntityLoot) random;
            Location location = event.getCrate().getCenter();
            location.setYaw(playerFacing(player.getLocation().getYaw()).getOppositeFace().ordinal());
            loot.spawn(location);
        }
        unregister(event.getCrate());
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

    //TODO
    private boolean isBlacklistedBlockNear(Block b) {
        return false;
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
