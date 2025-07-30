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
    private static final int RETREAT_COOLDOWN_TICKS = 100;
    private long nextRetreatAllowedTick = 0;

    public PackRetreatGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        long currentTick = wolf.level().getGameTime();
        if (currentTick < nextRetreatAllowedTick) return false;
        if (wolf.getPackRole() == PackRole.LEADER) return false;

        LivingEntity target = wolf.getTarget();
        return target != null && wolf.getHealth() < wolf.getMaxHealth() * 0.2;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = wolf.getTarget();
        return target != null && target.isAlive() && wolf.getHealth() < wolf.getMaxHealth() * 0.3;
    }

    @Override
    public void start() {
        wolf.setRetreating(true);
        LivingEntity threat = wolf.getTarget();
        if (threat == null) return;

        retreatFrom(threat);
        requestAlphaSupport(threat);
    }

    @Override
    public void stop() {
        wolf.setRetreating(false);
        wolf.getNavigation().stop();
        nextRetreatAllowedTick = wolf.level().getGameTime() + RETREAT_COOLDOWN_TICKS;
    }

    private void retreatFrom(LivingEntity threat) {
        Vec3 retreatDirection = wolf.position().subtract(threat.position()).normalize().scale(10.0);
        Vec3 retreatPos = wolf.position().add(retreatDirection);

        if (wolf.getNavigation().isDone()) {
            wolf.getNavigation().moveTo(retreatPos.x, retreatPos.y, retreatPos.z, 1.5D);
        }
    }

    private void requestAlphaSupport(LivingEntity threat) {
        List<VoidHowlerEntity> nearbyLeaders = wolf.level().getEntitiesOfClass(
                VoidHowlerEntity.class,
                wolf.getBoundingBox().inflate(30),
                leader -> leader.isAlive() && leader.isLeader()
        );

        for (VoidHowlerEntity alpha : nearbyLeaders) {
            alpha.setPackTarget(threat);
            alpha.setLastCallTime(0); // Override cooldown to rally
        }
    }
}