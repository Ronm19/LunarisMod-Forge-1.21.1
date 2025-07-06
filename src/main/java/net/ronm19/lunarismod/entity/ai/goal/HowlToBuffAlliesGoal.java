package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
    private static final long COOLDOWN_TICKS = 400L; // 20 seconds
    private long nextAllowedHowl = 0;

    public HowlToBuffAlliesGoal(VoidHowlerEntity howler) {
        this.howler = howler;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long currentTick = howler.level().getGameTime();

        // Must be leader, alive, and cooldown expired
        return howler.isLeader()
                && howler.isAlive()
                && currentTick >= nextAllowedHowl;
    }

    @Override
    public void start() {
        long currentTick = howler.level().getGameTime();
        nextAllowedHowl = currentTick + COOLDOWN_TICKS;

        // Play howl sound
        howler.level().playSound(null, howler.blockPosition(), ModSounds.VOID_HOWLER_HOWL.get(), SoundSource.HOSTILE, 1.0F, 1.0F);

        // Spawn particles for effect
        if (howler.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.WITCH, howler.getX(), howler.getY() + 1, howler.getZ(), 30, 1, 1, 1, 0.1);
        }

        // Apply role-based buff to nearby LunarWolves
        AABB area = howler.getBoundingBox().inflate(30.0D);
        List<LunarWolfEntity> allies = howler.level().getEntitiesOfClass(LunarWolfEntity.class, area);

        for (LunarWolfEntity wolf : allies) {
            if (!wolf.isAlive()) continue;

            PackRole role = wolf.getPackRole();
            switch (role) {
                case LEADER -> {
                    wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1, false, true));
                    wolf.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, true));
                }
                case SCOUT -> {
                    wolf.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, true));
                }
                case GUARDIAN -> {
                    wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, true));
                    wolf.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 200, 0, false, true));
                }
            }
        }
    }
}