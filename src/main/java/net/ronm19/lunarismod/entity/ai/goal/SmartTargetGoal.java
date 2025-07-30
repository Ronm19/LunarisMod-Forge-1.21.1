package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.Goal;
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
        if (wolf.isRetreating() || wolf.getTarget() != null) return false;

        List<LivingEntity> potentialTargets = wolf.level().getEntitiesOfClass(
                LivingEntity.class,
                wolf.getBoundingBox().inflate(16.0),
                entity -> entity instanceof Monster && entity.isAlive() && entity != wolf
        );

        if (potentialTargets.isEmpty()) return false;

        LivingEntity chosen = potentialTargets.stream()
                .min(Comparator.comparingDouble(this::calculateThreatScore))
                .orElse(null);

        if (chosen == null) return false;

        wolf.setTarget(chosen);
        wolf.setPackTarget(chosen);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Target once per evaluation
    }

    private double calculateThreatScore(LivingEntity entity) {
        double score = 0.0;

        if (entity instanceof RangedAttackMob) score -= 50.0; // Prioritize ranged
        score += entity.distanceToSqr(wolf);                  // Closer = lower score
        score += entity.getHealth();                          // Weaker = lower score

        return score;
    }
}