package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.*;
import us.mcmagic.sillycrates.SillyCratesMessage;
import us.mcmagic.sillycrates.util.NMSUtil;

import java.util.List;

public class CrateZombie extends EntityZombie {

    public CrateZombie(World world) {
        super(world);
        // Clears pathfinder goal/target selectors from this zombie.
        List goalB = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "b", goalSelector); goalB.clear();
        List goalC = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "c", goalSelector); goalC.clear();
        List targetB = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "b",  targetSelector); targetB.clear();
        List targetC = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "c",  targetSelector); targetC.clear();
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
