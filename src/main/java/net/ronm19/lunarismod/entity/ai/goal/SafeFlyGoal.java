package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;

public class SafeFlyGoal extends Goal {

    private final Mob mob;
    private final LevelReader level;

    public SafeFlyGoal(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Activate this goal only if the mob is flying (not on ground)
        return !mob.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        // Continue as long as flying
        return !mob.onGround();
    }

    @Override
    public void tick() {
        if (mob.onGround()) return;

        BlockPos posBelow = mob.blockPosition().below();

        // Check for void below (empty space for 3 blocks)
        boolean isVoidBelow = true;
        for (int i = 1; i <= 3; i++) {
            BlockPos checkPos = posBelow.below(i);
            if (!level.isEmptyBlock(checkPos)) {
                isVoidBelow = false;
                break;
            }
        }
        if (isVoidBelow) {
            // Move up to avoid falling into the void
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 2, mob.getZ(), 1.0);
            return;
        }

        // Check for dangerous blocks at current position (lava or fire)
        var currentBlock = level.getBlockState(mob.blockPosition()).getBlock();
        if (currentBlock == Blocks.LAVA || currentBlock == Blocks.FIRE) {
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 2, mob.getZ(), 1.0);
            return;
        }

        // Check for obstacles in front and slightly above
        var lookVec = mob.getLookAngle();
        BlockPos frontPos = mob.blockPosition().offset(
                Math.toIntExact(Math.round(lookVec.x)),
                0,
                Math.toIntExact(Math.round(lookVec.z))
        );
        BlockPos frontUpPos = frontPos.above();

        if (!level.isEmptyBlock(frontPos) || !level.isEmptyBlock(frontUpPos)) {
            // Move up to avoid obstacle ahead
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 1, mob.getZ(), 1.0);
        }
    }
}
