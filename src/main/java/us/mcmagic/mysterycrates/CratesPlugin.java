package us.mcmagic.mysterycrates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import us.mcmagic.mysterycrates.utils.FileUtil;
import us.mcmagic.mysterycrates.utils.Glow;

import java.io.File;
import java.lang.reflect.Field;

public final class CratesPlugin extends JavaPlugin {

    private static CratesPlugin instance;
    private static CratesManager manager;
    private static ItemStack crate;
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(new File("/plugins/MysteryCrates/config.yml"));

    @Override
    public void onEnable() {
        instance = this;

        saveConfig();
        FileUtil.setupConfig();

        manager = new CratesManager();
        getCommand("crate").setExecutor(new CrateCommand());
        crate = registerItem();
    }

    @Override
    public void onDisable() { }

    private void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(69);
            Enchantment.registerEnchantment(glow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ItemStack registerItem() {
        ItemStack crate = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = crate.getItemMeta();
        meta.setDisplayName(Crate.NAME);
        crate.setItemMeta(meta);
        return crate;
    }


//    private ItemStack registerSkull() {
//        miniCrate = new ItemStack(Material.SKULL, 1);
//        SkullMeta meta = (SkullMeta) miniCrate.getItemMeta();
//        meta.setOwner("enderchest");
//        miniCrate.setItemMeta(meta);
//        return miniCrate;
//    }

    public static ItemStack getCrateItem() {
        return crate;
    }

    public static CratesPlugin getInstance() {
        return instance;
    }

    public CratesManager getManager() {
        return manager;
    }

    public static String getStringFromLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
    }

    public static Location getLocationFromString(String s) {
        String[] loc = s.split(",");
        return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
    }

    /*
     - blaze = Blaze Head
     - cavespider = Cave Spider Head,
     - chicken = Chicken Head
     - cow = Cow Head
     - enderman = Enderman Head
     - ghast = Mini Ghast Head
     - golem = Golem Head
     - lavaslime = Mini Lava Slime
     - mooshroom = Mooshroom Head
     - ocelot = Ocelot Head
     - pig = Pig Head
     - pigman = Pigman Head
     - sheep = Sheep Head
     - slime = Mini Slime
     - spider = Spider Head
     - squid = Squid Head
     - villager = Villager Head
     - cactus = Mini Cactus
     - cake = Mini Cake
     - chest = Mini Chest
     - melon = Mini Melon
     - melon2 = Mini Melon
     - log = Mini Oak Log
     - wood = Mini Oak Log
     - wood2 = Mini Oak Log
     - wood3 = Mini Oak Log
     - oaklog = Mini Oak Log
     - oakwood = Mini Oak Log
     - pumpkin = Mini Pumpkin
     - pumpkin2 = Mini Pumpkin
     - pumpkin3 = Mini Pumpkin
     - tnt = Mini TNT
     - tnt2 = Mini TNT
     - arrowup = Arrow Up
     - arrowdown = Arrow Down
     - arrowleft = Arrow Left
     - arrowright = Arrow Right
     - questionmark = Question Mark
     - exclamation = Exclamation Mark
     - leaves = Mini Leaves
     - planks = Mini Planks
     - stone = Mini Stone
     - mossycobble = Mini Mossy Cobblestone
     - jukebox = Mini Jukebox
     - dispenser = Mini Dispenser
     - stickypiston = Mini Sticky Piston
     - piston = Mini Piston
     - cobble = Mini Cobblestone
     - diamondore = Mini Diamond Ore
     - redstoneore = Mini Redstone Ore
     - emeraldore = Mini Emerald Ore
     - sponge = Mini Sponge
     - quartz = Mini Quartz
     - bookstack = Book Stack
     - bookshelf = Mini Bookshelf
     - haybale = Mini Haybale
     - spawner = Mini Monster Spawner
     - glowstone = Mini Glowstone
     - podzol = Mini Podzol
     - dirt = Mini Dirt
     - diamondblock = Mini Diamond Block
     - goldblock = Mini Gold Block
     - ironblock = Mini Iron Block
     - obsidian = Mini Obsidian
     - bedrock = Mini Bedrock
     - sand = Mini Sand
     - redstonelamp = Mini Redstone Lamp
     - snowman = Mini Redstone Lamp
     - pufferfish = Pufferfish Head
     - clownfish = Clownfish Head
     - popcorn = Popcorn
     - cupcake = Cupcake
     - hamburger = Hamburger
     - globe = Globe
     - netherrack = Mini Netherrack
     - monitor = Monitor
     - tv = TV
     - radio = Radio
     - enderchest = Mini Ender Chest
     - apple = Apple
     - coconut = Coconut
     - masterball = Masterball
     - greatball = Greatball
     - pokeball = Pokéball
     - sugarcane = Mini Sugarcane
     - rubikscube = Rubikscube
     - clock = Nightstand Clock
     - ceilinglights = Ceiling Lights
     - purpleberry = Purple Berry
     - lemon = Lemon
     - lime = Lime
     - cherry = Cherry
     */
}
