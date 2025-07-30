package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class MoonlightStaggerGoal extends Goal {
    private final Mob mob;
    private int cooldown = 0;

    public MoonlightStaggerGoal( Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return mob.level().isNight() && mob.level().canSeeSky(mob.blockPosition()) && cooldown <= 0;
    }

    @Override
    public void start() {
        // Stagger behavior: slow down or pause briefly
        mob.setDeltaMovement(mob.getDeltaMovement().scale(0.5));
        cooldown = 200; // ticks until next trigger
    }

    @Override
    public void tick() {
        cooldown--;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}