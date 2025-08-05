package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public class CustomFollowOwnerGoal extends FollowOwnerGoal {
    public CustomFollowOwnerGoal( TamableAnimal entity, double followSpeed, float minDist, float maxDist) {
        super(entity, followSpeed, minDist, maxDist);
    }

    @Override
    public boolean canUse() {
        // Add custom conditions or tweak distances here
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }
}
