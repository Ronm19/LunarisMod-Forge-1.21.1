package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class PatrolWithMemoryGoal extends Goal {
    protected final Mob mob;
    protected final Level world;

    // Stores intrusion points and the game time (in ticks) they were recorded
    protected final Map<BlockPos, Long> intrusionMemory = new HashMap<>();

    // Default constants (can be overridden)
    private static final long DEFAULT_MEMORY_DURATION = 6000L; // 5 minutes
    private static final int DEFAULT_PATH_COOLDOWN_TICKS = 40; // 2 seconds

    private int pathCooldown = 0;
    private BlockPos currentTarget = null;

    public PatrolWithMemoryGoal(Mob mob) {
        this.mob = mob;
        this.world = mob.level();
    }

    // External call to add intrusion point
    public void rememberIntrusion(BlockPos pos) {
        long currentTime = world.getGameTime();
        intrusionMemory.put(pos, currentTime);
    }

    @Override
    public boolean canUse() {
        if (pathCooldown > 0) return false;

        cleanOldMemories();

        if (intrusionMemory.isEmpty()) return false;

        currentTarget = findClosestIntrusion();

        return currentTarget != null;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.getNavigation().isInProgress() && currentTarget != null;
    }

    @Override
    public void start() {
        if (currentTarget != null) {
            mob.getNavigation().moveTo(currentTarget.getX() + 0.5, currentTarget.getY(), currentTarget.getZ() + 0.5, 1.0);
            pathCooldown = getPathCooldownTicks();
        }
    }

    @Override
    public void tick() {
        if (pathCooldown > 0) pathCooldown--;

        if (currentTarget != null && mob.position().distanceTo(
                new net.minecraft.world.phys.Vec3(currentTarget.getX() + 0.5, currentTarget.getY(), currentTarget.getZ() + 0.5)) < 2.0) {
            intrusionMemory.remove(currentTarget);
            currentTarget = null;
        }
    }

    protected void cleanOldMemories() {
        long now = world.getGameTime();
        List<BlockPos> expired = intrusionMemory.entrySet().stream()
                .filter(entry -> now - entry.getValue() > getMemoryDuration())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (BlockPos pos : expired) {
            intrusionMemory.remove(pos);
        }
    }

    protected BlockPos findClosestIntrusion() {
        BlockPos mobPos = mob.blockPosition();
        return intrusionMemory.keySet().stream()
                .min(Comparator.comparingDouble(pos -> pos.distSqr(mobPos)))
                .orElse(null);
    }

    // Methods to override in subclasses for customization

    protected long getMemoryDuration() {
        return DEFAULT_MEMORY_DURATION;
    }

    protected int getPathCooldownTicks() {
        return DEFAULT_PATH_COOLDOWN_TICKS;
    }
}

