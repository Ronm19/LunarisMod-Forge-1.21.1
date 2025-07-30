package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

import java.util.EnumSet;
import java.util.List;

public class PackFlankGoal extends Goal {
    private final LunarWolfEntity wolf;
    private final double speed;

    private List<LunarWolfEntity> flankPack;
    private LivingEntity target;

    public PackFlankGoal(LunarWolfEntity wolf, double speed) {
        this.wolf = wolf;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!wolf.isTame() || wolf.getOwner() == null || wolf.isLeader()) return false;

        VoidHowlerEntity leader = locateLeader();
        if (leader == null) return false;

        LivingEntity packTarget = leader.getPackTarget();
        if (packTarget == null || !packTarget.isAlive()) return false;

        target = packTarget;
        return wolf.getTarget() == target;
    }

    @Override
    public void start() {
        flankPack = wolf.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                wolf.getBoundingBox().inflate(40),
                member -> member.isAlive() && member.getTarget() == target && !member.isLeader()
        );

        assignFlankPositions();
    }

    @Override
    public void tick() {
        if (target != null && target.isAlive()) {
            assignFlankPositions(); // keep syncing with target movement
        }
    }

    private void assignFlankPositions() {
        if (flankPack.isEmpty()) return;

        Vec3 center = target.position();
        double radius = 4.0;

        for (int i = 0; i < flankPack.size(); i++) {
            LunarWolfEntity member = flankPack.get(i);

            double angle = (2 * Math.PI / flankPack.size()) * i;
            double offsetX = radius * Math.cos(angle);
            double offsetZ = radius * Math.sin(angle);

            Vec3 flankSpot = center.add(offsetX, 0, offsetZ);
            member.getNavigation().moveTo(flankSpot.x, flankSpot.y, flankSpot.z, speed);
        }
    }

    private VoidHowlerEntity locateLeader() {
        return wolf.level().getEntitiesOfClass(
                VoidHowlerEntity.class,
                wolf.getBoundingBox().inflate(40),
                VoidHowlerEntity::isLeader
        ).stream().findFirst().orElse(null);
    }
}