package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.*;
import us.mcmagic.sillycrates.util.SillyCratesMessage;

public class CrateGiant extends EntityGiantZombie {

    public CrateGiant(World world) {
        super(world);
        addAi();
    }

    protected void addAi() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(2.5D);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(0.5D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D*2D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(35D);
        this.setCustomName(SillyCratesMessage.format("&e&lGiant Bob", '&'));
        this.setCustomNameVisible(true);
    }
}
