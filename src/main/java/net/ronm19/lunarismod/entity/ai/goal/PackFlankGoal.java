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
    private List<LunarWolfEntity> pack;
    private LivingEntity target;

    public PackFlankGoal(LunarWolfEntity wolf, double speed) {
        this.wolf = wolf;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!wolf.isTame() || wolf.getOwner() == null) return false;

        // Find the leader nearby
        VoidHowlerEntity leader = findLeader();

        if (leader == null) return false;

        target = leader.getPackTarget();
        if (target == null || !target.isAlive()) return false;

        // Pack members should flank only if their target matches leader’s target
        return wolf.getTarget() == target;
    }

    @Override
    public void start() {
        // Find all pack members (exclude leader)
        pack = wolf.level().getEntitiesOfClass(LunarWolfEntity.class,
                wolf.getBoundingBox().inflate(40),
                w -> w.isAlive() && w.getTarget() == target && !w.isLeader());

        assignFlankPositions();
    }

    @Override
    public void tick() {
        assignFlankPositions(); // keep updating as target moves
    }

    private void assignFlankPositions() {
        int packSize = pack.size();
        if (packSize == 0) return;

        double radius = 4.0;
        Vec3 targetPos = target.position();

        for (int i = 0; i < packSize; i++) {
            LunarWolfEntity member = pack.get(i);

            double angle = (2 * Math.PI / packSize) * i;
            double offsetX = radius * Math.cos(angle);
            double offsetZ = radius * Math.sin(angle);

            Vec3 flankPos = new Vec3(targetPos.x + offsetX, targetPos.y, targetPos.z + offsetZ);

            member.getNavigation().moveTo(flankPos.x, flankPos.y, flankPos.z, speed);
        }
    }

    private VoidHowlerEntity findLeader() {
        return wolf.level().getEntitiesOfClass(VoidHowlerEntity.class,
                        wolf.getBoundingBox().inflate(40),
                        VoidHowlerEntity::isLeader)
                .stream().findFirst().orElse(null);
    }
}