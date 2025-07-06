package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ai.PackRole;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

import java.util.EnumSet;
import java.util.List;

public class PackRetreatGoal extends Goal {
    private final LunarWolfEntity wolf;
    private int retreatCooldown = 0;

    public PackRetreatGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (retreatCooldown > 0) {
            retreatCooldown--;
            return false;
        }

        if (wolf.getPackRole() == PackRole.LEADER)
            return false; // Leaders don't retreat

        LivingEntity target = wolf.getTarget();
        return target != null && wolf.getHealth() < wolf.getMaxHealth() * 0.2;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = wolf.getTarget();
        return target != null && wolf.getHealth() < wolf.getMaxHealth() * 0.3;
    }

    @Override
    public void start() {
        LivingEntity target = wolf.getTarget();
        if (target == null) return;

        wolf.setRetreating(true);

        // Calculate retreat vector (away from threat)
        Vec3 direction = wolf.position().subtract(target.position()).normalize().scale(10.0);
        Vec3 retreatPos = wolf.position().add(direction);

        wolf.getNavigation().moveTo(retreatPos.x, retreatPos.y, retreatPos.z, 1.5D);

        // Call nearby leaders for assistance
        List<VoidHowlerEntity> nearbyLeaders = wolf.level().getEntitiesOfClass(
                VoidHowlerEntity.class,
                wolf.getBoundingBox().inflate(30),
                leader -> leader.isAlive() && leader.isLeader()
        );

        for (VoidHowlerEntity leader : nearbyLeaders) {
            leader.setPackTarget(target);
            leader.setLastCallTime(0); // force cooldown reset
        }
    }

    @Override
    public void stop() {
        wolf.getNavigation().stop();
        retreatCooldown = 100; // Cooldown before retry
        wolf.setRetreating(false);
    }
}