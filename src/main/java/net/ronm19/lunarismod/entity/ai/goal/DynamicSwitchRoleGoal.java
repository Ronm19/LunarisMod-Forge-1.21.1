package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.ai.PackRole;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

import java.util.EnumSet;
import java.util.List;

public class DynamicSwitchRoleGoal extends Goal {
    private final LunarWolfEntity wolf;
    private long lastSwitchTime = 0;
    private static final int COOLDOWN_TICKS = 600; // 30 seconds
    private static final double PACK_RADIUS = 40.0D;

    public DynamicSwitchRoleGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return wolf.level().getGameTime() - lastSwitchTime >= COOLDOWN_TICKS;
    }

    @Override
    public void start() {
        lastSwitchTime = wolf.level().getGameTime();

        List<LunarWolfEntity> pack = wolf.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                wolf.getBoundingBox().inflate(PACK_RADIUS),
                w -> w.isAlive() && !w.isBaby()
        );

        int leaders = 0, scouts = 0, guardians = 0;

        for (LunarWolfEntity member : pack) {
            switch (member.getPackRole()) {
                case LEADER -> leaders++;
                case SCOUT -> scouts++;
                case GUARDIAN -> guardians++;
            }
        }

        PackRole current = wolf.getPackRole();
        double healthRatio = wolf.getHealth() / wolf.getMaxHealth();
        double speed = wolf.getAttribute(Attributes.MOVEMENT_SPEED).getValue();

        // Leadership check
        if (leaders == 0 && current != PackRole.LEADER && healthRatio > 0.7) {
            wolf.setPackRole(PackRole.LEADER);
            wolf.setLeader(true);
            return;
        }

        // Step down if multiple leaders
        if (current == PackRole.LEADER && leaders > 1) {
            wolf.setPackRole(PackRole.FOLLOWER);
            wolf.setLeader(false);
            return;
        }

        // Scout check
        if (scouts < 2 && current != PackRole.SCOUT && current != PackRole.LEADER && speed > 0.3) {
            wolf.setPackRole(PackRole.SCOUT);
            wolf.setLeader(false);
            return;
        }

        // Guardian check
        if (guardians < 2 && current != PackRole.GUARDIAN && current != PackRole.LEADER && healthRatio > 0.8) {
            wolf.setPackRole(PackRole.GUARDIAN);
            wolf.setLeader(false);
            return;
        }

        // Fallback
        if (current != PackRole.FOLLOWER) {
            wolf.setPackRole(PackRole.FOLLOWER);
            wolf.setLeader(false);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}