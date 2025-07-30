package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.state.BlockState;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

import java.util.EnumSet;

public final class PanicAttackGoal extends Goal {
    private final LunarWolfEntity wolf;
    private final double speed;
    private long nextAllowedPanicTick = 0;

    public PanicAttackGoal(LunarWolfEntity wolf, double speed) {
        this.wolf = wolf;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        long currentTick = wolf.level().getGameTime();
        if (currentTick < nextAllowedPanicTick) return false;
        if (wolf.getPackRole() == null || wolf.getPackRole().isLeader()) return false;

        LivingEntity attacker = wolf.getLastHurtByMob();
        boolean selfInDanger = attacker != null && wolf.getHealth() < wolf.getMaxHealth() * 0.3;

        boolean groupInDanger = wolf.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                wolf.getBoundingBox().inflate(20),
                other -> other != wolf && other.isAlive() && !other.isLeader()
                        && other.getHealth() < other.getMaxHealth() * 0.3
        ).size() >= 2;

        return selfInDanger || groupInDanger;
    }

    @Override
    public void start() {
        nextAllowedPanicTick = wolf.level().getGameTime() + 100;

        LivingEntity attacker = wolf.getLastHurtByMob();
        if (attacker != null) {
            var fleePos = DefaultRandomPos.getPosAway(wolf, 16, 7, attacker.position());
            if (fleePos != null && isPositionSafe(BlockPos.containing(fleePos))) {
                wolf.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, speed);
                triggerPanicEffect();
            }
        } else {
            // If group panic triggered but attacker is unknown, flee randomly
            var randomFlee = DefaultRandomPos.getPos(wolf, 8, 6);
            if (randomFlee != null && isPositionSafe(BlockPos.containing(randomFlee))) {
                wolf.getNavigation().moveTo(randomFlee.x, randomFlee.y, randomFlee.z, speed);
                triggerPanicEffect();
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

    private boolean isPositionSafe(BlockPos pos) {
        BlockState state = wolf.level().getBlockState(pos);
        return state.isAir();
    }

    private void triggerPanicEffect() {
        wolf.level().addParticle(ParticleTypes.CLOUD, wolf.getX(), wolf.getY() + 0.5, wolf.getZ(), 0, 0.1, 0);
        wolf.playSound(SoundEvents.WOLF_WHINE, 1.0F, 1.0F);
    }
}