package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.block.ModBlocks;

public class HomeToHuskstoneGoal extends Goal {
    private final Mob mob;
    private BlockPos huskstonePos;

    public HomeToHuskstoneGoal(Mob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        huskstonePos = findNearbyHuskstone();
        return huskstonePos != null;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(huskstonePos.getX(), huskstonePos.getY(), huskstonePos.getZ(), 1.0D);
    }

    private BlockPos findNearbyHuskstone() {
        BlockPos mobPos = mob.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(mobPos.offset(-8, -2, -8), mobPos.offset(8, 2, 8))) {
            if (mob.level().getBlockState(pos).is(ModBlocks.HUSK_STONE_BLOCK.get())) {
                return pos.immutable();
            }
        }
        return null;
    }
}
