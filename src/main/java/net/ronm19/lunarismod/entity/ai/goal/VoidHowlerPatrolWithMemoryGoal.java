package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.Mob;

public class VoidHowlerPatrolWithMemoryGoal extends PatrolWithMemoryGoal {
    // Customize memory duration and cooldown (in ticks)
    private static final long VOID_HOWLER_MEMORY_DURATION = 12000L; // 10 mins instead of 5
    private static final int VOID_HOWLER_PATH_COOLDOWN = 20; // 1 sec cooldown instead of 2

    public VoidHowlerPatrolWithMemoryGoal(Mob mob) {
        super(mob);
    }

    @Override
    protected long getMemoryDuration() {
        return VOID_HOWLER_MEMORY_DURATION;
    }

    @Override
    protected int getPathCooldownTicks() {
        return VOID_HOWLER_PATH_COOLDOWN;
    }
}