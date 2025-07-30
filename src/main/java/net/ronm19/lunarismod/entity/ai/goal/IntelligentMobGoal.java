package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.util.ItemUtils;

import java.util.EnumSet;
import java.util.List;

public class IntelligentMobGoal extends Goal {

    private final Mob mob;
    private final Level level;
    private final PathNavigation navigation;
    private Player owner;
    private int cooldown;
    private int lookAroundCooldown;

    public IntelligentMobGoal(Mob mob) {
        this.mob = mob;
        this.level = mob.level();
        this.navigation = mob.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return mob instanceof Monster || mob instanceof TamableAnimal;
    }

    @Override
    public void tick() {
        if (++cooldown < 10) return;
        cooldown = 0;

        if (mob.isVehicle()) return;

        boolean isFlying = !mob.onGround() && mob.getBbHeight() > 0.8;
        boolean isTamed = false;

        if (mob instanceof TamableAnimal tamable && tamable.isTame() && tamable.getOwner() instanceof Player player) {
            owner = player;
            isTamed = true;
            handleTamedBehavior(tamable, isFlying);
        } else {
            owner = null;
            isTamed = false;
            handleAggressiveBehavior(isFlying);
        }
    }

    private void handleTamedBehavior(TamableAnimal tamable, boolean isFlying) {
        Vec3 targetPos = null;

        if (mob.getTarget() != null && mob.getTarget().isAlive()) {
            LivingEntity target = mob.getTarget();
            targetPos = target.position();

            double distance = mob.position().distanceTo(target.position());
            if (distance < 2.5 && ItemUtils.canUseItemToAttack(mob.getMainHandItem())) {
                mob.swing(InteractionHand.MAIN_HAND);
                mob.doHurtTarget(target);
            }
        } else {
            mob.setTarget(null);
        }

        // Follow owner if no target
        if (targetPos == null && mob.distanceTo(owner) > 6.0F) {
            targetPos = owner.position().add(0, 0.3, 0);
        }

        // Wander randomly if no other action
        if (targetPos == null && mob.getRandom().nextFloat() < 0.1F) {
            targetPos = mob.position().add(getRandomOffset());
        }

        moveToward(targetPos, isFlying);
    }

    private void handleAggressiveBehavior(boolean isFlying) {
        Vec3 targetPos = null;

        if (mob.getTarget() != null && mob.getTarget().isAlive()) {
            LivingEntity target = mob.getTarget();
            targetPos = target.position();

            double distance = mob.position().distanceTo(target.position());
            if (distance < 2.5 && ItemUtils.canUseItemToAttack(mob.getMainHandItem())) {
                mob.swing(InteractionHand.MAIN_HAND);
                mob.doHurtTarget(target);
            }
        } else {
            mob.setTarget(null);
        }

        // Find new attack target if none
        if (targetPos == null) {
            LivingEntity attackTarget = findNearestAttackable();
            if (attackTarget != null) {
                mob.setTarget(attackTarget);
                targetPos = attackTarget.position();
            }
        }

        // Flee if threatened and no target
        if (targetPos == null) {
            targetPos = findSafePositionFromThreats();
        }

        // Wander randomly
        if (targetPos == null && mob.getRandom().nextFloat() < 0.1F) {
            targetPos = mob.position().add(getRandomOffset());
        }

        moveToward(targetPos, isFlying);
    }

    private void moveToward(Vec3 targetPos, boolean isFlying) {
        if (targetPos == null) {
            lookAroundIdle();
            return;
        }

        double speed = 1.0;
        if (mob.getTarget() != null) speed = 1.4;
        else if (mob.position().distanceTo(targetPos) < 5) speed = 1.2;

        if (isFlying) avoidAirHazardsAndMove(targetPos, speed);
        else avoidGroundHazardsAndMove(targetPos, speed);
    }

    private void lookAroundIdle() {
        if (++lookAroundCooldown > 40) {
            lookAroundCooldown = 0;
            float yaw = mob.getRandom().nextFloat() * 360F;
            float pitch = (mob.getRandom().nextFloat() - 0.5F) * 30F;
            mob.setYRot(yaw);
            mob.yRotO = yaw;
            mob.setXRot(pitch);
            mob.xRotO = pitch;
        }
    }

    private boolean isAggressiveMob() {
        return mob instanceof Monster;
    }

    private LivingEntity findNearestAttackable() {
        double radius = 12.0;
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(radius), e ->
                e != mob && e.isAlive() && (e instanceof Player || e instanceof Monster) && !mob.getType().equals(e.getType())
        );

        LivingEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;
        Vec3 mobPos = mob.position();

        for (LivingEntity entity : entities) {
            double dist = entity.position().distanceToSqr(mobPos);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = entity;
            }
        }

        return nearest;
    }

    private Vec3 findSafePositionFromThreats() {
        double threatRadius = 10.0;
        List<Entity> threats = level.getEntities(mob, mob.getBoundingBox().inflate(threatRadius), e ->
                e != mob && (e instanceof Player || e instanceof Monster)
        );

        if (threats.isEmpty()) return null;

        double avgX = 0, avgY = 0, avgZ = 0;
        int count = 0;

        for (Entity threat : threats) {
            avgX += threat.getX();
            avgY += threat.getY();
            avgZ += threat.getZ();
            count++;
        }

        Vec3 threatCenter = new Vec3(avgX / count, avgY / count, avgZ / count);
        Vec3 fleeDir = mob.position().subtract(threatCenter).normalize();
        Vec3 fleeTarget = mob.position().add(fleeDir.scale(5 + mob.getRandom().nextDouble() * 3));

        BlockPos fleeBlockPos = BlockPos.containing(fleeTarget);
        if (isDangerous(fleeBlockPos) || !level.isEmptyBlock(fleeBlockPos)) {
            fleeTarget = mob.position().add(getRandomOffset().normalize().scale(4));
        }

        return fleeTarget;
    }

    private void avoidGroundHazardsAndMove(Vec3 pos, double speed) {
        BlockPos below = mob.blockPosition().below();
        if (isDangerous(below)) {
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 1.2, mob.getZ(), speed);
            return;
        }

        if (isBlockedInFront()) {
            Vec3 look = mob.getLookAngle();

            Vec3 leftOffset = rotateVector(look, 90).normalize().scale(1);
            Vec3 rightOffset = rotateVector(look, -90).normalize().scale(1);

            Vec3 tryLeft = mob.position().add(leftOffset);
            Vec3 tryRight = mob.position().add(rightOffset);

            if (level.isEmptyBlock(BlockPos.containing(tryLeft))) {
                navigation.moveTo(tryLeft.x, tryLeft.y, tryLeft.z, speed);
            } else if (level.isEmptyBlock(BlockPos.containing(tryRight))) {
                navigation.moveTo(tryRight.x, tryRight.y, tryRight.z, speed);
            } else {
                mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 0.5, mob.getZ(), speed);
            }
            return;
        }

        navigation.moveTo(pos.x, pos.y, pos.z, speed);
    }

    private void avoidAirHazardsAndMove(Vec3 pos, double speed) {
        BlockPos under = mob.blockPosition().below();
        boolean isVoid = true;
        for (int i = 1; i <= 3; i++) {
            if (!level.isEmptyBlock(under.below(i))) {
                isVoid = false;
                break;
            }
        }
        if (isVoid) {
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 1.0, mob.getZ(), speed);
            return;
        }

        BlockPos above = mob.blockPosition().above();
        if (!level.isEmptyBlock(above)) {
            mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY() + 1.0, mob.getZ(), speed);
            return;
        }

        mob.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, speed);
    }

    private Vec3 getRandomOffset() {
        double dx = mob.getRandom().nextDouble() * 8 - 4;
        double dy = mob.getRandom().nextDouble() * 2 - 1;
        double dz = mob.getRandom().nextDouble() * 8 - 4;
        return new Vec3(dx, dy, dz);
    }

    private boolean isDangerous(BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.LAVA) || level.getBlockState(pos).is(Blocks.FIRE);
    }

    private boolean isBlockedInFront() {
        Vec3 look = mob.getLookAngle();
        BlockPos front = mob.blockPosition().offset(
                Mth.floor(look.x),
                0,
                Mth.floor(look.z)
        );
        return !level.isEmptyBlock(front);
    }

    private Vec3 rotateVector(Vec3 vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double newX = vec.x * cos - vec.z * sin;
        double newZ = vec.x * sin + vec.z * cos;
        return new Vec3(newX, vec.y, newZ);
    }
}
