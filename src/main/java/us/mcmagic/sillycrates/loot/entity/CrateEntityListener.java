package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.util.SillyCratesMessage;

public class CrateEntityListener implements Listener {

    //TODO Make zombies that look like player
    //TODO Make ownable cats as loot

    private ItemStack head;

    public CrateEntityListener() {
        head = new ItemStack(Material.SKULL_ITEM, 1, (byte) 2);
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(SillyCratesMessage.format("&4&lBob's Head", '&'));
        head.setItemMeta(meta);
        head = addArbitraryTags(head, 807);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void spawn(EntitySpawnEvent event) {
        if(event.getEntity().getType() == EntityType.ZOMBIE || event.getEntity().getType() == EntityType.GIANT) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (entity.getCustomName() != null && (entity.getCustomName().equals(SillyCratesMessage.format("&e&lBob", '&')) || entity.getCustomName().equals(SillyCratesMessage.format("&e&lGiant Bob", '&')))) {
                ItemStack[] armor = new ItemStack[4];
                ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
                armor[3] = helmet;
                ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
                armor[2] = chest;
                ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
                armor[1] = pants;
                ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
                armor[0] = boots;
                ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
                sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
                for (ItemStack stack : armor) {
                    stack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
                }
                entity.getEquipment().setArmorContents(armor);
                entity.getEquipment().setItemInHand(sword);
            }
        }
    }

    @EventHandler
    public void death(EntityDeathEvent event) {
        if(event.getEntity().getType() == EntityType.ZOMBIE) {
            LivingEntity entity = event.getEntity();
            if (entity.getCustomName() != null && entity.getCustomName().equals(SillyCratesMessage.format("&e&lBob", '&'))) {
                event.getDrops().clear();
                event.getDrops().add(head);
            } else if (entity.getCustomName() != null && entity.getCustomName().equals(SillyCratesMessage.format("&e&lGiant Bob", '&'))) {
                event.getDrops().clear();
                event.getDrops().add(head);
            }
        }
    }

    @EventHandler
    public void clickDonkeyOrMule(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Horse && event.getPlayer().getItemInHand().isSimilar(CratesPlugin.getCrateItem())) {
            Horse horse = (Horse) event.getRightClicked();
            if (horse.getVariant() == Horse.Variant.DONKEY || horse.getVariant() == Horse.Variant.MULE) {
                event.setCancelled(true);
                CratesPlugin.getInstance().getManager().error(event.getPlayer(), "Uh oh! You can't place these on horses.");
            }
        }
    }
    private ItemStack addArbitraryTags(ItemStack stack, int amount) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        for (int i = 0; i < amount; i++) {
            tag.setString(String.valueOf(i), String.valueOf(i));
        }
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    private ItemStack addGlow(ItemStack stack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        NBTTagList enchantmnent = new NBTTagList();
        tag.set("ench", enchantmnent);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
