package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;
import net.ronm19.lunarismod.sound.ModSounds;

import java.util.EnumSet;
import java.util.List;

public class HowlToBuffAlliesGoal extends Goal {
    private final VoidHowlerEntity voidHowler;
    private int cooldownTicks = 0;
    private static final int MAX_COOLDOWN = 600; // 30 seconds
    private static final int BUFF_DURATION = 200; // 10 seconds
    private static final int BUFF_RADIUS = 40; // increased from 20

    public HowlToBuffAlliesGoal(VoidHowlerEntity voidHowler) {
        this.voidHowler = voidHowler;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return false;
        }

        List<LunarWolfEntity> alliesNearby = voidHowler.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                voidHowler.getBoundingBox().inflate(BUFF_RADIUS),
                e -> e.isAlive() && e.isTame()
        );

        return !alliesNearby.isEmpty();
    }

    @Override
    public void start() {
        Level level = voidHowler.level();

        List<LunarWolfEntity> alliesNearby = level.getEntitiesOfClass(
                LunarWolfEntity.class,
                voidHowler.getBoundingBox().inflate(BUFF_RADIUS),
                e -> e.isAlive() && e.isTame()
        );

        // Play howl sound
        level.playSound(null, voidHowler.blockPosition(), ModSounds.VOID_HOWLER_HOWL.get(), SoundSource.HOSTILE, 1.2F, 1.0F);

        // Apply strength buff to each
        for (LunarWolfEntity wolf : alliesNearby) {
            wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, BUFF_DURATION, 1)); // Strength II
        }

        cooldownTicks = MAX_COOLDOWN;
    }
}
