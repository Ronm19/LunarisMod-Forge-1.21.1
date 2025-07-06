package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

import java.util.EnumSet;
import java.util.List;

public class FollowAlphaGoal extends Goal {
    private final LunarWolfEntity follower;
    private LivingEntity leader;
    private final double speed;
    private int timeToRecalcPath;
    private final float minDist; // Min distance to start following
    private final float maxDist; // Max distance to stop following

    public FollowAlphaGoal(LunarWolfEntity follower, double speed, float minDist, float maxDist) {
        this.follower = follower;
        this.speed = speed;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (follower.isLeader()) return false; // Leader should not follow anyone else

        leader = findAlpha();
        if (leader == null) return false;

        double distSq = follower.distanceToSqr(leader);
        return distSq >= (minDist * minDist) && distSq <= (maxDist * maxDist);
    }

    @Override
    public boolean canContinueToUse() {
        if (leader == null) return false;
        if (!leader.isAlive()) return false;

        double distSq = follower.distanceToSqr(leader);
        return distSq > (minDist * minDist) && distSq < (maxDist * maxDist);
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        leader = null;
        follower.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (--timeToRecalcPath <= 0) {
            timeToRecalcPath = 10; // Adjust this delay as needed

            if (leader != null) {
                follower.getNavigation().moveTo(leader, speed);
            }
        }
    }

    private LivingEntity findAlpha() {
        List<VoidHowlerEntity> alphas = follower.level().getEntitiesOfClass(VoidHowlerEntity.class,
                follower.getBoundingBox().inflate(maxDist),
                e -> e.isLeader() && e.isAlive());

        return alphas.isEmpty() ? null : alphas.get(0);
    }
}
