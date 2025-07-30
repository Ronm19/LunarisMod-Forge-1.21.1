package net.ronm19.lunarismod.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;

public class LunarEffect extends MobEffect {
    public LunarEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            boolean isNight = !entity.level().isDay();

            int duration = 2400; // 2 minutes
            int resistanceLevel = isNight ? 3 : 1;
            int speedLevel = isNight ? 3 : 1;      // Speed IV vs Speed II
            int strengthLevel = isNight ? 3 : 1;   // Strength IV vs Strength II
            int regenLevel = 0;

            if (entity instanceof Player player) {
                if (entity.tickCount % 80 == 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0, true, false));
                }

                if (entity.tickCount % 100 == 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, duration, regenLevel, true, false));
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, resistanceLevel, true, false));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, speedLevel, true, false));
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, strengthLevel, true, false));
                }
            }

            // Spawn moon-like particles (END_ROD)
            ((ServerLevel) entity.level()).sendParticles(
                    ParticleTypes.END_ROD,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    4,
                    0.4, 0.4, 0.4,
                    0.01
            );
        }

        return true;
    }


    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
