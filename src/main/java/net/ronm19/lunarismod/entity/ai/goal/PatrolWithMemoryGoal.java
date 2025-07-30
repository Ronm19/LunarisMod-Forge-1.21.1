package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.*;

public class PatrolWithMemoryGoal extends Goal {
    protected final Mob mob;
    protected final Level world;

    private final Map<BlockPos, Long> intrusionMemory = new HashMap<>();
    private static final long DEFAULT_MEMORY_DURATION = 6000L; // 5 minutes
    private static final int DEFAULT_PATH_COOLDOWN_TICKS = 40;

    private long nextAllowedPatrolTick = 0;
    private BlockPos currentTarget = null;

    public PatrolWithMemoryGoal(Mob mob) {
        this.mob = mob;
        this.world = mob.level();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public void rememberIntrusion(BlockPos pos) {
        intrusionMemory.put(pos, world.getGameTime());
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != null) return false;

        long now = world.getGameTime();
        if (now < nextAllowedPatrolTick) return false;

        cleanOldMemories();
        currentTarget = findClosestIntrusion();
        return currentTarget != null;
    }

    @Override
    public void start() {
        if (currentTarget != null) {
            Vec3 center = new Vec3(
                    currentTarget.getX() + 0.5,
                    currentTarget.getY(),
                    currentTarget.getZ() + 0.5
            );

            mob.getNavigation().moveTo(center.x, center.y, center.z, 1.0);
            nextAllowedPatrolTick = world.getGameTime() + getPathCooldownTicks();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return currentTarget != null && mob.getNavigation().isInProgress();
    }

    @Override
    public void tick() {
        if (currentTarget == null) return;

        Vec3 targetVec = new Vec3(
                currentTarget.getX() + 0.5,
                currentTarget.getY(),
                currentTarget.getZ() + 0.5
        );

        if (mob.position().distanceTo(targetVec) < 2.0) {
            intrusionMemory.remove(currentTarget);
            currentTarget = null;
        }
    }

    protected void cleanOldMemories() {
        long now = world.getGameTime();
        intrusionMemory.entrySet().removeIf(entry -> now - entry.getValue() > getMemoryDuration());
    }

    protected BlockPos findClosestIntrusion() {
        BlockPos mobPos = mob.blockPosition();
        return intrusionMemory.keySet().stream()
                .min(Comparator.comparingDouble(pos -> pos.distSqr(mobPos)))
                .orElse(null);
    }

    protected long getMemoryDuration() {
        return DEFAULT_MEMORY_DURATION;
    }

    protected int getPathCooldownTicks() {
        return DEFAULT_PATH_COOLDOWN_TICKS;
    }
}