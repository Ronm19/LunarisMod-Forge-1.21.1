package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import net.ronm19.lunarismod.entity.ai.PackRole;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;
import net.ronm19.lunarismod.sound.ModSounds;

import java.util.EnumSet;
import java.util.List;

public class HowlToBuffAlliesGoal extends Goal {
    private final VoidHowlerEntity howler;
    private static final long COOLDOWN_TICKS = 400L;
    private long nextAllowedHowlTick = 0;

    public HowlToBuffAlliesGoal(VoidHowlerEntity howler) {
        this.howler = howler;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long currentTick = howler.level().getGameTime();
        return howler.isAlive() && howler.isLeader() && currentTick >= nextAllowedHowlTick;
    }

    @Override
    public void start() {
        long currentTick = howler.level().getGameTime();
        nextAllowedHowlTick = currentTick + COOLDOWN_TICKS;

        playHowlEffects();
        buffNearbyWolves();
    }

    private void playHowlEffects() {
        howler.level().playSound(
                null,
                howler.blockPosition(),
                ModSounds.VOID_HOWLER_HOWL.get(),
                SoundSource.HOSTILE,
                1.0F,
                1.0F
        );

        if (howler.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.WITCH,
                    howler.getX(), howler.getY() + 1, howler.getZ(),
                    30, 1, 1, 1, 0.1
            );
        }
    }

    private void buffNearbyWolves() {
        AABB auraRange = howler.getBoundingBox().inflate(30.0D);
        List<LunarWolfEntity> allies = howler.level().getEntitiesOfClass(
                LunarWolfEntity.class, auraRange, wolf -> wolf.isAlive()
        );

        for (LunarWolfEntity wolf : allies) {
            applyRoleBuffs(wolf);
        }
    }

    private void applyRoleBuffs(LunarWolfEntity wolf) {
        PackRole role = wolf.getPackRole();

        // Base effect for all roles (optional):
        // wolf.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0));

        switch (role) {
            case LEADER -> {
                addEffects(wolf,
                        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1, false, true),
                        new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, true),
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, true),
                        new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0, false, true)
                );
            }
            case SCOUT -> {
                addEffects(wolf,
                        new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, true)
                );
            }
            case GUARDIAN -> {
                addEffects(wolf,
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, true),
                        new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0, false, true)
                );
            }
        }
    }

    private void addEffects(LunarWolfEntity wolf, MobEffectInstance... effects) {
        for (MobEffectInstance effect : effects) {
            wolf.addEffect(effect);
        }
    }
}