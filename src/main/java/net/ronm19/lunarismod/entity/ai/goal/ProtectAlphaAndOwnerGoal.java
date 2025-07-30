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
    private final LunarWolfEntity wolf;
    private LivingEntity defendTarget;

    public ProtectAlphaAndOwnerGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (wolf.getPackRole() == null || wolf.getPackRole().isLeader()) return false;

        LivingEntity alpha = locateAlpha();
        LivingEntity owner = wolf.getOwner();
        LivingEntity defendEntity = alpha != null ? alpha : owner;

        if (defendEntity == null) return false;

        List<LivingEntity> threats = wolf.level().getEntitiesOfClass(
                LivingEntity.class,
                defendEntity.getBoundingBox().inflate(40.0D),
                e -> (e instanceof Monster || e instanceof Player) &&
                        e.isAlive() && e != defendEntity && e.distanceTo(defendEntity) < 40.0D
        );

        if (threats.isEmpty()) return false;

        defendTarget = threats.stream()
                .min((a, b) -> Double.compare(a.distanceTo(defendEntity), b.distanceTo(defendEntity)))
                .orElse(null);

        return defendTarget != null && defendTarget != wolf.getTarget();
    }

    @Override
    public void start() {
        wolf.setTarget(defendTarget);
    }

    @Override
    public void stop() {
        wolf.setTarget(null);
        defendTarget = null;
    }

    private LivingEntity locateAlpha() {
        return wolf.level().getEntitiesOfClass(
                VoidHowlerEntity.class,
                wolf.getBoundingBox().inflate(15.0D),
                leader -> leader.isLeader() && leader.isAlive()
        ).stream().findFirst().orElse(null);
    }
}