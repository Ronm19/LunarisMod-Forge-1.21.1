package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class FlyingWanderAroundGoal extends Goal {
    private final Mob mob;
    private final double speed;
    private int cooldown;

    public FlyingWanderAroundGoal(Mob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        Vec3 pos = getRandomPosition();
        if (pos != null) {
            ((FlyingPathNavigation) mob.getNavigation()).moveTo(pos.x, pos.y, pos.z, speed);
        }
        cooldown = 40 + mob.getRandom().nextInt(60); // Random delay between 2–5 seconds
    }

    private Vec3 getRandomPosition() {
        Level world = mob.level();
        double x = mob.getX() + (mob.getRandom().nextDouble() * 16 - 8);
        // Fly 3 to 8 blocks higher than current Y
        double y = mob.getY() + 3 + mob.getRandom().nextDouble() * 5;
        // Clamp to world height limits (optional)
        y = Math.min(y, world.getMaxBuildHeight() - 1);
        y = Math.max(y, world.getMinBuildHeight() + 1);
        double z = mob.getZ() + (mob.getRandom().nextDouble() * 16 - 8);

        return new Vec3(x, y, z);
    }

    @Override
    public boolean canContinueToUse() {
        return false; // One-time goal each tick when triggered
    }
}
