package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.*;
import us.mcmagic.sillycrates.SillyCratesMessage;

public class CrateZombie extends EntityZombie {

    public CrateZombie(World world) {
        super(world);
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(10D);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D/2D);
        this.setCustomName(SillyCratesMessage.format("&5Crate Zombie", '&'));
        this.setCustomNameVisible(true);
        this.setBaby(false);
        this.setVillager(false);
    }

    @Override
    protected Item getLoot() {
        return Items.DIAMOND;
    }
}
