package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarZombieKingEntity;

import java.util.List;

public class FollowKingGoal extends Goal {
    private final Mob follower;
    private final double speed;
    private final float followRange;
    private LunarZombieKingEntity targetKing;

    public FollowKingGoal(Mob follower, double speed, float followRange) {
        this.follower = follower;
        this.speed = speed;
        this.followRange = followRange;
    }

    @Override
    public boolean canUse() {
        List<LunarZombieKingEntity> nearbyKings = follower.level().getEntitiesOfClass(
                LunarZombieKingEntity.class,
                follower.getBoundingBox().inflate(followRange));

        if (!nearbyKings.isEmpty()) {
            targetKing = nearbyKings.getFirst();
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        if (targetKing != null && follower.distanceTo(targetKing) > 2.0F) {
            follower.getNavigation().moveTo(targetKing, speed);
        }
    }
}