package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

import java.util.EnumSet;

public final class PanicAttackGoal extends Goal {
    private final LunarWolfEntity wolf;
    private final double speed;

    public PanicAttackGoal(LunarWolfEntity wolf, double speed) {
        this.wolf = wolf;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Only non-leaders panic attack
        if (wolf.getPackRole() == null || wolf.getPackRole().isLeader()) {
            return false;
        }

        LivingEntity attacker = wolf.getLastHurtByMob();
        return attacker != null && wolf.getHealth() < wolf.getMaxHealth() * 0.3;
    }

    @Override
    public void start() {
        LivingEntity attacker = wolf.getLastHurtByMob();
        if (attacker != null) {
            var fleePos = DefaultRandomPos.getPosAway(wolf, 16, 7, attacker.position());
            if (fleePos != null) {
                wolf.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, speed);
                wolf.level().addParticle(ParticleTypes.CLOUD, wolf.getX(), wolf.getY() + 0.5, wolf.getZ(), 0, 0.1, 0);
                wolf.playSound(SoundEvents.WOLF_WHINE, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !wolf.getNavigation().isDone();
    }

    @Override
    public void stop() {
        wolf.setLastHurtByMob(null);
    }
}
