package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarKnightEntity;

import java.util.EnumSet;

public class PatrolAroundGoal extends Goal {
    private final LunarKnightEntity knight;
    private BlockPos center;
    private int patrolCooldown;

    public PatrolAroundGoal( LunarKnightEntity knight) {
        this.knight = knight;
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.center = knight.blockPosition(); // initial patrol center
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        if (patrolCooldown <= 0) {
            // Pick a random point around center within radius 5-10 blocks
            BlockPos target = center.offset(knight.getRandom().nextInt(11) - 5, 0, knight.getRandom().nextInt(11) - 5);
            knight.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
            patrolCooldown = 100 + knight.getRandom().nextInt(100);
        } else {
            patrolCooldown--;
        }
    }
}

