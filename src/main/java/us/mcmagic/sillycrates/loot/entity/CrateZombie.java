package us.mcmagic.sillycrates.loot.entity;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import us.mcmagic.sillycrates.CratesPlugin;
import us.mcmagic.sillycrates.util.NMSUtil;
import us.mcmagic.sillycrates.util.SillyCratesMessage;

import java.util.List;

public class CrateZombie extends EntityZombie {

    public CrateZombie(World world) {
        super(world);
        List goalB = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "b", goalSelector);
        goalB.clear();
        List goalC = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "c", goalSelector);
        goalC.clear();
        List targetB = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "b",  targetSelector);
        targetB.clear();
        List targetC = (List) NMSUtil.getPrivateField(PathfinderGoalSelector.class, "c",  targetSelector);
        targetC.clear();
        Bukkit.getScheduler().runTaskLater(CratesPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                initPathfinder();
            }
        }, 100L);
    }

    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(10D);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(0.5D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D*2D);
        this.setCustomName(SillyCratesMessage.format("&e&lBob", '&'));
        this.setCustomNameVisible(true);
        this.setBaby(false);
        this.setVillager(false);
    }

    @Override
    protected String z() {
        return "mob.skeleton.say";
    }

    @Override
    protected String bo() {
        return "mob.skeleton.hurt";
    }

    @Override
    protected String bp() {
        return "mob.pig.death";
    }

    @Override
    protected void a(BlockPosition blockposition, Block block) {
        this.makeSound("mob.slime.big", 0.15F, 1.0F);
    }

    @Override
    protected Item getLoot() {
        return null;
    }


    // Make unpushable
    @Override
    public void g(double x, double y, double z) {
        return;
    }
}
