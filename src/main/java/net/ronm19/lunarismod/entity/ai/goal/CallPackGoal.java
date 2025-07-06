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
    private static final int COOLDOWN = 600; // 30 seconds
    private static final int BUFF_DURATION = 200; // 10 seconds
    private static final double CALL_RADIUS = 40.0D;

    public CallPackGoal(VoidHowlerEntity alpha) {
        this.alpha = alpha;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return alpha.isLeader()
                && alpha.level().getGameTime() - alpha.getLastCallTime() >= COOLDOWN;
    }

    @Override
    public void start() {
        alpha.setLastCallTime(alpha.level().getGameTime());

        // Play howl sound
        alpha.level().playSound(null, alpha.blockPosition(), ModSounds.VOID_HOWLER_HOWL.get(), SoundSource.HOSTILE, 1.5F, 1.0F);

        // Get nearby LunarWolves and apply buff or response
        List<LunarWolfEntity> pack = alpha.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                alpha.getBoundingBox().inflate(CALL_RADIUS),
                wolf -> wolf.isAlive() && !wolf.isBaby()
        );

        for (LunarWolfEntity wolf : pack) {
            // Give them Strength effect (can be customized)
            wolf.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, BUFF_DURATION, 0));
            // Optionally move them toward alpha
            wolf.getNavigation().moveTo(alpha, 1.3D);
        }
    }
}
