package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.custom.VoidOrbEntity;

import java.util.EnumSet;

public class VoidOrbAttackGoal extends Goal {
    private final Mob mob;
    private final double attackRangeSq;
    private final int attackCooldown;
    private int attackTimer;

    public VoidOrbAttackGoal(Mob mob, double range, int cooldownTicks) {
        this.mob = mob;
        this.attackRangeSq = range * range;
        this.attackCooldown = cooldownTicks;
        this.setFlags(EnumSet.of(Flag.TARGET, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive() && mob.distanceToSqr(target) <= attackRangeSq;
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (--attackTimer <= 0) {
            attackTimer = attackCooldown;

            Vec3 direction = target.position().subtract(mob.position()).normalize();
            VoidOrbEntity orb = new VoidOrbEntity(mob.level(), mob);
            orb.setPos(mob.getX(), mob.getY(0.75D), mob.getZ());
            orb.shoot(direction.x, direction.y + 0.05, direction.z, 1.2f, 0.0f);

            mob.level().addFreshEntity(orb);
        }
    }

    @Override
    public void start() {
        attackTimer = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }
}
