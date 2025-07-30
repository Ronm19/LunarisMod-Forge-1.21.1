package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.custom.VoidPhantomEntity;

import java.util.EnumSet;

public class PhantomAttackGoal extends Goal {
    private final VoidPhantomEntity phantom;
    private LivingEntity target;
    private int attackCooldown = 0;

    public PhantomAttackGoal(VoidPhantomEntity voidPhantomEntity) {
        this.phantom = voidPhantomEntity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = phantom.getTarget();
        return target != null && target.isAlive() && phantom.distanceTo(target) < 16.0D;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        attackCooldown = 0;
    }

    @Override
    public void stop() {
        target = null;
        phantom.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (target == null) return;

        // Look at the target
        phantom.getLookControl().setLookAt(target, 30.0F, 30.0F);

        double distanceSq = phantom.distanceToSqr(target);
        double attackReach = 4.0D + target.getBbWidth(); // Attack range

        // Move toward target if too far
        if (distanceSq > attackReach * attackReach) {
            Vec3 targetPos = target.position();
            phantom.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0D);
        } else {
            phantom.getNavigation().stop();
            if (attackCooldown <= 0) {
                phantom.swing(phantom.getMainHandItem());
                phantom.doHurtTarget(target);
                attackCooldown = 20; // cooldown ticks between attacks (1 second)
            }
        }

        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }
}
