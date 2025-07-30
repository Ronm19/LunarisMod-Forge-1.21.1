package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.ai.interfaces.RangedShooter;

import java.util.EnumSet;

public class LongRangeAttackGoal<T extends Mob & RangedShooter> extends Goal {
    private final T mob;
    private final double speed;
    private final int attackCooldownTicks;
    private final float maxRange;
    private int cooldown;

    public LongRangeAttackGoal(T mob, double speed, int attackCooldownTicks, float maxRange) {
        this.mob = mob;
        this.speed = speed;
        this.attackCooldownTicks = attackCooldownTicks;
        this.maxRange = maxRange;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive() && mob.distanceTo(target) <= maxRange;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive() && mob.distanceTo(target) <= maxRange;
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        double distanceSq = mob.distanceToSqr(target);
        if (distanceSq <= maxRange * maxRange && cooldown <= 0) {
            mob.shootAtTarget(target);
            cooldown = attackCooldownTicks;
        } else {
            cooldown--;
        }
    }

    @Override
    public void start() {
        cooldown = 0;
    }

    @Override
    public void stop() {
        mob.setTarget(null);
    }
}
