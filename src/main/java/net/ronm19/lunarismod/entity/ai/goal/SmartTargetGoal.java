package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class SmartTargetGoal extends Goal {
    private final LunarWolfEntity wolf;

    public SmartTargetGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (wolf.getTarget() != null || wolf.isRetreating()) return false;

        List<LivingEntity> potentialTargets = wolf.level().getEntitiesOfClass(
                LivingEntity.class,
                wolf.getBoundingBox().inflate(16),
                e -> e instanceof Monster && e.isAlive() && e != wolf
        );

        if (potentialTargets.isEmpty()) return false;

        potentialTargets.sort(Comparator.comparingDouble((LivingEntity e) -> {
            double score = 0;

            if (e instanceof RangedAttackMob) score -= 50; // Highly prioritize ranged attackers
            score += e.distanceToSqr(wolf); // Closer = better
            score += e.getHealth(); // Weaker enemies = higher priority

            return score;
        }));

        LivingEntity best = potentialTargets.get(0);
        wolf.setTarget(best);
        wolf.setPackTarget(best);
        return true;
    }


    @Override
    public boolean canContinueToUse() {
        return false; // Runs once
    }
}
