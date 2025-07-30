package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;
import net.ronm19.lunarismod.sound.ModSounds;

import java.util.EnumSet;
import java.util.List;

public class CallPackGoal extends Goal {
    private final VoidHowlerEntity alpha;

    private static final int COOLDOWN_TICKS = 600;       // 30 seconds
    private static final int BUFF_DURATION_TICKS = 200;  // 10 seconds
    private static final double CALL_RADIUS = 40.0D;
    private static final double MIN_DISTANCE_TO_CALL = 10.0D;

    public CallPackGoal(VoidHowlerEntity alpha) {
        this.alpha = alpha;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long currentTime = alpha.level().getGameTime();
        return alpha.isLeader() &&
                (currentTime - alpha.getLastCallTime()) >= COOLDOWN_TICKS;
    }

    @Override
    public void start() {
        long callTime = alpha.level().getGameTime();
        alpha.setLastCallTime(callTime);

        // Howl to summon pack
        alpha.level().playSound(null,
                alpha.blockPosition(),
                ModSounds.VOID_HOWLER_HOWL.get(),
                SoundSource.HOSTILE,
                1.5F,
                1.0F
        );

        // Find packmates
        List<LunarWolfEntity> packmates = alpha.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                alpha.getBoundingBox().inflate(CALL_RADIUS),
                wolf -> wolf.isAlive() && !wolf.isBaby()
        );

        for (LunarWolfEntity wolf : packmates) {
            double distance = wolf.distanceTo(alpha);
            if (distance > MIN_DISTANCE_TO_CALL) {
                wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, BUFF_DURATION_TICKS, 0));
                wolf.getNavigation().moveTo(alpha, 1.3D);
            }
        }
    }
}