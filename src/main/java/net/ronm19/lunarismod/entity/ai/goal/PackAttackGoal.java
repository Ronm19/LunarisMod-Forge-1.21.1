package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import net.ronm19.lunarismod.entity.ai.PackRole;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

import java.util.EnumSet;
import java.util.List;

public class PackAttackGoal extends Goal {
    private final LunarWolfEntity wolf;
    private static final int ATTACK_COOLDOWN_TICKS = 40;

    public PackAttackGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!wolf.isTame() || wolf.getOwner() == null) return false;

        List<VoidHowlerEntity> leaders = wolf.level().getEntitiesOfClass(
                VoidHowlerEntity.class,
                wolf.getBoundingBox().inflate(40),
                alpha -> alpha.isAlive() && alpha.getPackRole() == PackRole.LEADER && alpha.getPackTarget() != null
        );

        for (VoidHowlerEntity alpha : leaders) {
            LivingEntity target = alpha.getPackTarget();
            if (target != null && target.isAlive()) {
                wolf.setPackTarget(target);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = wolf.getPackTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        LivingEntity target = wolf.getPackTarget();
        if (target == null) return;

        wolf.setTarget(target);
        applyRoleEffects();

        if (wolf.getAttackCooldownTicks() <= 0) {
            performAttackAnimation();
            wolf.setAttackCooldownTicks(ATTACK_COOLDOWN_TICKS);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = wolf.getPackTarget();
        if (target != null && wolf.getNavigation().isDone()) {
            wolf.getNavigation().moveTo(target, 1.4D);
        }
    }

    @Override
    public void stop() {
        wolf.setTarget(null);
        wolf.setPackTarget(null);
        wolf.setRetreating(false);
    }

    private void applyRoleEffects() {
        PackRole role = wolf.getPackRole();
        switch (role) {
            case LEADER -> addEffects(
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1),
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1)
            );
            case SCOUT -> addEffects(
                    new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1)
            );
            case GUARDIAN -> addEffects(
                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1),
                    new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0)
            );
        }
    }

    private void addEffects(MobEffectInstance... effects) {
        for (MobEffectInstance effect : effects) {
            wolf.addEffect(effect);
        }
    }

    private void performAttackAnimation() {
        wolf.swing(wolf.getMainHandItem());
        LivingEntity target = wolf.getPackTarget();
        if (target != null) {
            wolf.getNavigation().moveTo(target, 1.4D);
        }

        wolf.level().playSound(null, wolf.blockPosition(), SoundEvents.WOLF_GROWL, SoundSource.HOSTILE, 1.0f, 1.0f);
        wolf.level().addParticle(ParticleTypes.ANGRY_VILLAGER, wolf.getX(), wolf.getY() + 1, wolf.getZ(), 0, 0, 0);
    }
}