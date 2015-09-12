package us.mcmagic.mysterycrates;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import us.mcmagic.mysterycrates.utils.ParticleEffect;
import us.mcmagic.mysterycrates.utils.RandomPlus;
import us.mcmagic.mysterycrates.utils.RandomUtils;
import us.mcmagic.mysterycrates.utils.WeightedList;

import java.util.*;

public class CratesManager implements Listener {

    private boolean cooldownEnabled = false;
    private int cooldownTimer = 5;
    private HashMap<UUID, Long> cooldown = new HashMap<>();
    private HashSet<Crate> crates = new HashSet<>();

    private static WeightedList<CrateLoot> loot = new WeightedList<CrateLoot>() {{
//        put(new CrateLoot("Stone", CrateLoot.Rarity.COMMON, Material.STONE, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Polished Granite", CrateLoot.Rarity.COMMON, Material.STONE, (byte) 0x2, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Polished Andesite", CrateLoot.Rarity.COMMON, Material.STONE, (byte) 0x6,  1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Grass", CrateLoot.Rarity.COMMON, Material.GRASS, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Dirt", CrateLoot.Rarity.COMMON, Material.DIRT, 1, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Podzol", CrateLoot.Rarity.COMMON, Material.DIRT, (byte) 0x2, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Cobblestone", CrateLoot.Rarity.COMMON, Material.COBBLESTONE, 16, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Wood Planks", CrateLoot.Rarity.COMMON, Material.WOOD, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Spruce Wood Planks", CrateLoot.Rarity.COMMON, Material.WOOD, (byte) 0x1, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Jungle Wood Planks", CrateLoot.Rarity.COMMON, Material.WOOD, (byte) 0x3, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Acacia Wood Planks", CrateLoot.Rarity.COMMON, Material.WOOD, (byte) 0x4, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Dark Oak Wood Planks", CrateLoot.Rarity.COMMON, Material.WOOD, (byte) 0x5, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Dark Oak Sapling", CrateLoot.Rarity.COMMON, Material.SAPLING, (byte) 0x5, 1, 10), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Sand", CrateLoot.Rarity.COMMON, Material.SAND, 5, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Red Sand", CrateLoot.Rarity.COMMON, Material.SAND, (byte) 0x1, 5, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Gravel", CrateLoot.Rarity.COMMON, Material.GRAVEL, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Gold Ore", CrateLoot.Rarity.COMMON, Material.GOLD_ORE, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Iron Ore", CrateLoot.Rarity.COMMON, Material.IRON_ORE, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Coal Ore", CrateLoot.Rarity.COMMON, Material.COAL_ORE, 1, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Oak Wood", CrateLoot.Rarity.COMMON, Material.LOG, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Dark Oak Wood", CrateLoot.Rarity.COMMON, Material.LOG_2, (byte) 0x1, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Oak Leaves", CrateLoot.Rarity.COMMON, Material.LEAVES, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Sponge", CrateLoot.Rarity.RARE, Material.SPONGE, 1, 10), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot("Wet Sponge", CrateLoot.Rarity.RARE, Material.SPONGE, (byte) 0x1, 1, 10), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot("Glass", CrateLoot.Rarity.COMMON, Material.GLASS, 1, 12), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Lapis Lazuli Ore", CrateLoot.Rarity.COMMON, Material.LAPIS_ORE, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Lapis Lazuli Block", CrateLoot.Rarity.COMMON, Material.LAPIS_BLOCK, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Leaves", CrateLoot.Rarity.COMMON, Material.LEAVES, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Dispenser", CrateLoot.Rarity.COMMON, Material.DISPENSER, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Note Block", CrateLoot.Rarity.COMMON, Material.NOTE_BLOCK, 1, 2), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Powered Rail", CrateLoot.Rarity.COMMON, Material.POWERED_RAIL, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Detector Rail", CrateLoot.Rarity.COMMON, Material.DETECTOR_RAIL, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Sticky Piston", CrateLoot.Rarity.COMMON, Material.PISTON_STICKY_BASE, 1, 2), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot("Cobweb", CrateLoot.Rarity.COMMON, Material.WEB, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.DEAD_BUSH, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.PISTON_BASE, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.WOOL, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.WOOL, (byte) 0x6, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.YELLOW_FLOWER, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.RED_ROSE, 1, 12), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.RED_ROSE, (byte) 0x2, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.BROWN_MUSHROOM, 16, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.RED_MUSHROOM, 16, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.LEAVES, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.GOLD_BLOCK, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.IRON_BLOCK, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STONE_SLAB2, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.TNT, 1, 2), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.BOOKSHELF, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.MOSSY_COBBLESTONE, 16, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.OBSIDIAN, 1, 8), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.TORCH, 16, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.MOB_SPAWNER, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CHEST, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.DIAMOND_ORE, 1, 3), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.DIAMOND_BLOCK, 1, 2), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.WORKBENCH, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.FURNACE, 1, 2), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.SIGN, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.LADDER, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.RAILS, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.LEVER, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STONE_PLATE, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.REDSTONE_TORCH_ON, 16, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STONE_BUTTON, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CACTUS, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CLAY, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.JUKEBOX, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.FENCE, 16, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.PUMPKIN, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.NETHERRACK, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.SOUL_SAND, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.GLOWSTONE, 1, 12), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CAKE, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_GLASS, (byte) 0x1, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_GLASS, (byte) 0x2, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_GLASS, (byte) 0x3, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_GLASS, (byte) 0x4, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_GLASS, (byte) 0x5, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.TRAP_DOOR, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.SMOOTH_BRICK, (byte) 0x3, 1, 4), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.IRON_FENCE, 8, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.THIN_GLASS, 1, 5), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.MELON_BLOCK, 1, 2), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.MYCEL, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.NETHER_WARTS, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.ENCHANTMENT_TABLE, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.ENDER_STONE, 16, 32), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.LEGENDARY, Material.DRAGON_EGG, 1, 1), CrateLoot.Rarity.LEGENDARY.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.REDSTONE_LAMP_OFF, 1, 6), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.EMERALD_ORE, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.ENDER_CHEST, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.EMERALD_BLOCK, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.BEACON, 1, 3), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.COBBLE_WALL, 8, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.SKULL_ITEM, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.SKULL_ITEM, (byte) 0x1, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.RARE, Material.SKULL_ITEM, (byte) 0x4, 1, 1), CrateLoot.Rarity.RARE.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.ANVIL, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.TRAPPED_CHEST, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.DAYLIGHT_DETECTOR, 1, 2), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.REDSTONE_BLOCK, 1, 1), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.HOPPER, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.QUARTZ_BLOCK, 1, 16), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_CLAY, (byte) 0x0, 32, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_CLAY, (byte) 0x3, 32, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_CLAY, (byte) 0x11, 32, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.STAINED_CLAY, (byte) 0x14, 32, 64), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.PRISMARINE, 10, 20), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.PRISMARINE, (byte) 0x2, 10, 30), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CARPET, 6, 12), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CARPET, (byte) 0x1, 6, 12), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CARPET, (byte) 0x3, 6, 12), CrateLoot.Rarity.COMMON.getWeight());
//        put(new CrateLoot(CrateLoot.Rarity.COMMON, Material.CAKE, 1, 3), CrateLoot.Rarity.COMMON.getWeight());
        put(new CrateLoot("Diamond", CrateLoot.Rarity.RARE, Material.DIAMOND, 1, 8), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Emerald", CrateLoot.Rarity.RARE, Material.EMERALD, 1, 8), CrateLoot.Rarity.RARE.getWeight());
        put(new CrateLoot("Gold", CrateLoot.Rarity.RARE, Material.GOLD_INGOT, 1, 8), CrateLoot.Rarity.RARE.getWeight());
    }};

    public CratesManager() {
        Bukkit.getPluginManager().registerEvents(this, CratesPlugin.getInstance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Crate crate : (HashSet<Crate>) crates.clone()) {
                    drawCuboidEdges(crate.getEffect(), 0.1, crate.getBlock().getLocation(), new Vector(0D, 0D, 0D), new Vector(1D, 1D, 1D));
                }

                if (cooldownEnabled) {
                    for (UUID uuid : new HashSet<UUID>(cooldown.keySet())) {
                        int elapsed = (int) ((System.currentTimeMillis() - cooldown.get(uuid)) / 1000);
                        if (elapsed >= cooldownTimer) {
                            if (Bukkit.getPlayer(uuid) == null) continue;
                            Bukkit.getPlayer(uuid).sendMessage(colorize("&9You may now place another " + Crate.NAME + "&r&9."));
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

        //TODO: Save location to file config.yml
    }

    public void unregister(Crate c) {
        SimpleHolograms.getHologramManager().removeHologram(c.getHologramID().toString(), true);
        c.getBlock().setType(Material.AIR);
        crates.remove(c);
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
            player.sendMessage(colorize("&c" + message));
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
            Crate c = new Crate(event.getPlayer().getUniqueId(), UUID.randomUUID(), event.getBlock().getLocation(), ParticleEffect.fromName("enchantmenttable"));
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
            for (final Crate crate : (HashSet<Crate>) crates.clone()) {
                if (compareBlockLocation(event.getClickedBlock().getLocation(), crate.getBlock().getLocation())) {
                    if (!p.getUniqueId().equals(crate.getOwnerUUID())) {
                        error(p, "Uh oh! This doesn't belong to you!");
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

        if (cl.getRarity().equals(CrateLoot.Rarity.LEGENDARY)) {
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 100, 2);
        }

        player.updateInventory();
        player.sendMessage(cl.getFoundMessage());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block b : new ArrayList<>(event.blockList())) {
            for (Crate c : crates) {
                if (compareBlockLocation(b.getLocation(), c.getBlock().getLocation())) {
                    event.blockList().remove(c.getBlock());
                    continue;
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
        player.playSound(location, Sound.values()[(int) Math.random() * Sound.values().length], a, b);
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
