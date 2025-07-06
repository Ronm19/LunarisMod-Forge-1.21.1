package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class MoonPhaseBuffGoal extends Goal {
    private final LivingEntity entity;
    private int cooldown;

    // Effect holders (no .get() needed in 1.21.1)
    private static final Holder<MobEffect> DAMAGE = MobEffects.DAMAGE_BOOST;
    private static final Holder<MobEffect> SPEED = MobEffects.MOVEMENT_SPEED;
    private static final Holder<MobEffect> SLOW = MobEffects.MOVEMENT_SLOWDOWN;

    public MoonPhaseBuffGoal(LivingEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.noneOf(Flag.class)); // No movement/target interference
    }

    @Override
    public boolean canUse() {
        return --cooldown <= 0;
    }

    @Override
    public void start() {
        cooldown = 200; // ~10 seconds

        Level level = entity.level();
        float moonBrightness = level.getMoonBrightness();

        if (moonBrightness > 0.9f) {
            applyBuffs();
        } else if (moonBrightness < 0.1f) {
            applyDebuffs();
        } else {
            removeAllEffects();
        }
    }

    private void applyBuffs() {
        if (!entity.hasEffect(DAMAGE)) {
            entity.addEffect(new MobEffectInstance(DAMAGE, 220, 1));
            entity.addEffect(new MobEffectInstance(SPEED, 220, 1));
        }
    }

    private void applyDebuffs() {
        if (!entity.hasEffect(SLOW)) {
            entity.addEffect(new MobEffectInstance(SLOW, 220, 0));
        }
    }

    private void removeAllEffects() {
        entity.removeEffect(DAMAGE);
        entity.removeEffect(SPEED);
        entity.removeEffect(SLOW);
    }
}
