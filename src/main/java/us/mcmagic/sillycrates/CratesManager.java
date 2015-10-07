package us.mcmagic.sillycrates;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
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
import us.mcmagic.sillycrates.loot.*;

import us.mcmagic.sillycrates.loot.entity.CrateEntityFactory;
import us.mcmagic.sillycrates.loot.entity.CrateEntityType;
import us.mcmagic.sillycrates.util.*;

import java.util.*;

public class CratesManager implements Listener {

    private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    private final boolean cooldownEnabled = false;
    private final int cooldownTimer = 5;
    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final HashSet<Crate> crates = new HashSet<>();

    private static final WeightedList<ICrateRandom> loot = new WeightedList<>();

    public CratesManager() {
        Bukkit.getPluginManager().registerEvents(this, CratesPlugin.getInstance());
        registerLoot();
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

    public void registerLoot() {
//        loot.put(new AcmeTrap(Rarity.COMMON, "acme.schematic"), Rarity.COMMON.getWeight());
//        System.out.println(CratesPlugin.getInstance().getDataFolder());
        loot.put(new RainingCatsAndDogs(Rarity.COMMON), Rarity.COMMON.getWeight());
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
        ICrateRandom random = loot.get();
        if (random instanceof CrateItemLoot) {
            if (player.getInventory().firstEmpty() == -1) {
                error(player, "Uh oh! Looks like your inventory is full!");
                event.setCancelled(true);
                return;
            }
            CrateItemLoot loot = (CrateItemLoot) random;
            loot.play(event.getCrate().getCenter());
            CratesPlugin.getInstance().getEntityFactory().spawn(event.getCrate().getCenter(), CrateEntityType.CRATE_ZOMBIE);
        } else if (random instanceof CrateStructure) {
            if (random instanceof AcmeTrap) {
                //TODO Teleport other players away from trap to avoid getting stuck
                random.play(event.getCrate().getCenter().add(-1, -1, -1));
                Location location = event.getCrate().getCenter().add(0, 1, 0);
                player.teleport(location);
            }
        } else if (random instanceof CrateEntities) {
            CrateEntities loot = (CrateEntities) random;
            System.out.println("True");
            loot.play(event.getCrate().getCenter());
        }
//        CratesPlugin.getInstance().getEntityFactory().spawn(event.getCrate().getCenter(), CrateEntityType.CRATE_GIANT);
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
            Vector vector = CrateRandomUtil.getRandomVector().multiply(radius);
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
