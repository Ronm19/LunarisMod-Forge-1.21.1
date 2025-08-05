package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.ai.CommandMode;
import net.ronm19.lunarismod.entity.custom.LunarKnightEntity;

import java.util.EnumSet;

public class HoldPositionGoal extends Goal {
    private final LunarKnightEntity knight;

    public HoldPositionGoal(LunarKnightEntity knight) {
        this.knight = knight;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return true; // Always hold position when goal is active
    }

    @Override
    public void tick() {
        knight.getNavigation().stop(); // Stop moving, hold position
    }
}
