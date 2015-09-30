package us.mcmagic.sillycrates.loot;

import net.minecraft.server.EntityZombie;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class CrateEntityLoot implements ICrateEntity {

    private Rarity rarity;

    @Override
    public void spawn(Location where) {
    }

    @Override
    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public int generateAmount(int min, int max) {
        return random.nextInt(max-min+1) + min;
    }
}
