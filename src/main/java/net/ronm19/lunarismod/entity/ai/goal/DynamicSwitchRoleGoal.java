package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.ai.PackRole;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

import java.util.EnumSet;
import java.util.List;

public class DynamicSwitchRoleGoal extends Goal {
    private final LunarWolfEntity wolf;
    private int cooldownTicks = 0;
    private static final int COOLDOWN = 600; // 30 seconds before switching roles again
    private static final double PACK_RADIUS = 40.0D;

    public DynamicSwitchRoleGoal(LunarWolfEntity wolf) {
        this.wolf = wolf;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldownTicks > 0) {
            cooldownTicks--;
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        cooldownTicks = COOLDOWN;

        List<LunarWolfEntity> pack = wolf.level().getEntitiesOfClass(LunarWolfEntity.class,
                wolf.getBoundingBox().inflate(PACK_RADIUS),
                w -> w.isAlive() && !w.isBaby());

        int leaders = 0, scouts = 0, guardians = 0, followers = 0;

        for (LunarWolfEntity member : pack) {
            switch (member.getPackRole()) {
                case LEADER -> leaders++;
                case SCOUT -> scouts++;
                case GUARDIAN -> guardians++;
                case FOLLOWER -> followers++;
            }
        }

        PackRole currentRole = wolf.getPackRole();

        if (leaders == 0 && currentRole != PackRole.LEADER && wolf.getHealth() > wolf.getMaxHealth() * 0.7) {
            wolf.setPackRole(PackRole.LEADER);
            wolf.setLeader(true);
            return;
        }

        if (currentRole == PackRole.LEADER && leaders > 1) {
            wolf.setPackRole(PackRole.FOLLOWER);
            wolf.setLeader(false);
            return;
        }

        if (scouts < 2 && currentRole != PackRole.SCOUT && currentRole != PackRole.LEADER &&
                wolf.getAttribute(Attributes.MOVEMENT_SPEED).getValue() > 0.3) {
            wolf.setPackRole(PackRole.SCOUT);
            wolf.setLeader(false);
            return;
        }

        if (guardians < 2 && currentRole != PackRole.GUARDIAN && currentRole != PackRole.LEADER &&
                wolf.getHealth() > wolf.getMaxHealth() * 0.8) {
            wolf.setPackRole(PackRole.GUARDIAN);
            wolf.setLeader(false);
            return;
        }

        if (currentRole != PackRole.FOLLOWER) {
            wolf.setPackRole(PackRole.FOLLOWER);
            wolf.setLeader(false);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false; // Runs only once per cooldown
    }
}
