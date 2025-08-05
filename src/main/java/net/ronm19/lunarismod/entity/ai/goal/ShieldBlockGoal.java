package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.custom.LunarKnightEntity;

import java.util.EnumSet;
import java.util.List;

public class ShieldBlockGoal extends Goal {
    private final LunarKnightEntity knight;
    private int blockDuration;
    private static final int MAX_BLOCK_TIME = 40; // 2 seconds
    private boolean soundPlayed = false;

    public ShieldBlockGoal(LunarKnightEntity knight) {
        this.knight = knight;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = knight.getTarget();
        return target != null &&
                knight.distanceTo(target) < 10.0D &&  // Extended range for ranged attackers
                knight.hasLineOfSight(target) &&
                knight.canBlock() &&
                isFacingTarget(target);
    }

    @Override
    public void start() {
        knight.setBlocking(true);
        blockDuration = 0;

        if (!knight.level().isClientSide) {
            knight.level().playSound(null, knight.blockPosition(),
                    SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    public void stop() {
        knight.setBlocking(false);

        if (!knight.level().isClientSide) {
            knight.level().playSound(null, knight.blockPosition(),
                    SoundEvents.SHIELD_BREAK, SoundSource.HOSTILE, 0.6F, 1.2F);
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = knight.getTarget();
        return target != null &&
                knight.distanceTo(target) < 10.0D &&
                knight.hasLineOfSight(target) &&
                blockDuration < MAX_BLOCK_TIME;
    }

    @Override
    public void tick() {
        LivingEntity target = knight.getTarget();
        if (target != null) {
            knight.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (!knight.level().isClientSide && blockDuration % 10 == 0) {
                ((net.minecraft.server.level.ServerLevel) knight.level()).sendParticles(
                        ParticleTypes.ENCHANT,
                        knight.getX(), knight.getY() + 1.0, knight.getZ(),
                        5, 0.2, 0.2, 0.2, 0.01);
            }
        }

        blockDuration++;
    }

    private boolean isFacingTarget(LivingEntity target) {
        double dx = target.getX() - knight.getX();
        double dz = target.getZ() - knight.getZ();
        double angleToTarget = Math.atan2(dz, dx) * (180 / Math.PI);
        double knightYaw = knight.getYRot() % 360;

        double diff = Math.abs(angleToTarget - knightYaw);
        return diff < 75.0D;  // Wider arc for ranged – 75 degrees
    }

    private boolean isProjectileThreatening() {
        if (knight.level().isClientSide) return false;

        // Search for projectiles within 5 blocks
        List<Projectile> projectiles = knight.level().getEntitiesOfClass(Projectile.class, knight.getBoundingBox().inflate(5));

        for (Projectile proj : projectiles) {
            if (proj.isAlive() && proj.getOwner() != knight) {
                Vec3 toKnight = knight.position().subtract(proj.position()).normalize();
                Vec3 projVelocity = proj.getDeltaMovement().normalize();

                double dotProduct = toKnight.dot(projVelocity);
                // If projectile is moving roughly toward knight (dot > 0.8)
                if (dotProduct > 0.8) {
                    return true;
                }
            }
        }

        return false;
    }

}
