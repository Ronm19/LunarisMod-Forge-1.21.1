package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Supplier;

public class PatrolTerritoryGoal<T extends PathfinderMob> extends Goal {
    private final T mob;
    private final double speed;
    private final Supplier<BlockPos> territoryGetter;
    private int patrolCooldown;

    public PatrolTerritoryGoal(T mob, double speed, Supplier<BlockPos> territoryGetter) {
        this.mob = mob;
        this.speed = speed;
        this.territoryGetter = territoryGetter;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos center = territoryGetter.get();
        if (center == null || mob.getTarget() != null) return false;

        if (patrolCooldown > 0) {
            patrolCooldown--;
            return false;
        }

        return mob.getRandom().nextFloat() < 0.02F;
    }

    @Override
    public void start() {
        patrolCooldown = Mth.nextInt(mob.getRandom(), 200, 400);

        BlockPos center = territoryGetter.get();
        if (center == null) return;

        Vec3 centerVec = Vec3.atBottomCenterOf(center);
        Vec3 patrolPos = DefaultRandomPos.getPosTowards(mob, 20, 20, centerVec, 10);
        if (patrolPos != null) {
            mob.getNavigation().moveTo(patrolPos.x, patrolPos.y, patrolPos.z, speed);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone() && mob.getTarget() == null;
    }
}
