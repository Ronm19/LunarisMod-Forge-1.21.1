package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

import java.util.EnumSet;
import java.util.List;

public class FollowAlphaGoal extends Goal {
    private final LunarWolfEntity wolf;
    private LivingEntity alpha;
    private final double speed;
    private int recalcDelay;
    private static final int RECALC_INTERVAL = 10;

    private final float minDistanceSq;
    private final float maxDistanceSq;

    public FollowAlphaGoal(LunarWolfEntity wolf, double speed, float minDist, float maxDist) {
        this.wolf = wolf;
        this.speed = speed;
        this.minDistanceSq = minDist * minDist;
        this.maxDistanceSq = maxDist * maxDist;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (wolf.isLeader()) return false;

        alpha = locateAlpha();
        if (alpha == null) return false;

        double distSq = wolf.distanceToSqr(alpha);
        return distSq >= minDistanceSq && distSq <= maxDistanceSq;
    }

    @Override
    public boolean canContinueToUse() {
        return alpha != null && alpha.isAlive() &&
                wolf.distanceToSqr(alpha) > minDistanceSq &&
                wolf.distanceToSqr(alpha) < maxDistanceSq;
    }

    @Override
    public void start() {
        recalcDelay = 0;
    }

    @Override
    public void stop() {
        alpha = null;
        wolf.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (--recalcDelay <= 0) {
            recalcDelay = RECALC_INTERVAL;

            if (alpha != null && wolf.getNavigation().isDone()) {
                wolf.getNavigation().moveTo(alpha, speed);
            }
        }
    }

    private LivingEntity locateAlpha() {
        List<VoidHowlerEntity> leaders = wolf.level().getEntitiesOfClass(
                VoidHowlerEntity.class,
                wolf.getBoundingBox().inflate(Math.sqrt(maxDistanceSq)),
                e -> e.isLeader() && e.isAlive()
        );
        return leaders.isEmpty() ? null : leaders.get(0);
    }
}