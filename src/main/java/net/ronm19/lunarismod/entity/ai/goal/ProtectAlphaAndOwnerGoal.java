package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

import java.util.EnumSet;
import java.util.List;

public class ProtectAlphaAndOwnerGoal extends Goal {
    private final LunarWolfEntity follower;
    private LivingEntity defendTarget;

    public ProtectAlphaAndOwnerGoal(LunarWolfEntity follower) {
        this.follower = follower;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // Leaders protect themselves by default, followers protect alpha and owner
        if (follower.getPackRole() == null || follower.getPackRole().isLeader()) {
            return false; // Leaders don’t protect others, just themselves
        }

        // Find nearby VoidHowler alpha
        LivingEntity alpha = findAlpha();
        LivingEntity owner = follower.getOwner();

        if (alpha == null && owner == null) {
            return false;
        }

        // Prioritize defending alpha, else owner
        LivingEntity defendEntity = alpha != null ? alpha : owner;

        List<LivingEntity> threats = follower.level().getEntitiesOfClass(
                LivingEntity.class,
                defendEntity.getBoundingBox().inflate(40.0D),
                e -> (e instanceof Monster || e instanceof Player) && e.isAlive() && e.distanceTo(defendEntity) < 40.0D
        );

        if (threats.isEmpty()) {
            return false;
        }

        defendTarget = threats.stream()
                .min((a, b) -> Double.compare(a.distanceTo(defendEntity), b.distanceTo(defendEntity)))
                .orElse(null);

        return defendTarget != null && defendTarget != follower.getTarget();
    }

    @Override
    public void start() {
        follower.setTarget(defendTarget);
    }

    @Override
    public void stop() {
        follower.setTarget(null);
        defendTarget = null;
    }

    private LivingEntity findAlpha() {
        return follower.level().getEntitiesOfClass(VoidHowlerEntity.class,
                        follower.getBoundingBox().inflate(15.0D),
                        e -> e.isLeader() && e.isAlive())
                .stream()
                .findFirst()
                .orElse(null);
    }
}