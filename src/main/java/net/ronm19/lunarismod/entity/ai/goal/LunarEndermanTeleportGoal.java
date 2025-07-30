package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.ronm19.lunarismod.entity.custom.LunarEndermanEntity;

import java.util.EnumSet;

public class LunarEndermanTeleportGoal extends Goal {
    private final LunarEndermanEntity enderman;
    private LivingEntity target;
    private int teleportCooldown = 0;

    public LunarEndermanTeleportGoal(LunarEndermanEntity enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity potentialTarget = this.enderman.getTarget();
        if (potentialTarget == null || !potentialTarget.isAlive() || !(potentialTarget instanceof Player)) {
            return false;
        }
        this.target = potentialTarget;
        return true;
    }

    @Override
    public void tick() {
        if (this.target != null && this.target.isAlive() && !this.enderman.isPassenger()) {
            double distanceSqr = this.enderman.distanceToSqr(this.target);

            // Teleport to target if far away
            if (distanceSqr > 100.0D && --teleportCooldown <= 0) { // If farther than 10 blocks
                boolean success = this.enderman.teleportTowards(this.target);
                if (success) {
                    teleportCooldown = 40; // 2 seconds cooldown
                }
            }

            // Blink away if too close (panic teleport)
            if (distanceSqr < 9.0D && --teleportCooldown <= 0) { // Closer than 3 blocks
                this.enderman.customTeleport(16.0D);
                teleportCooldown = 40; // 2 seconds cooldown
            }
        }
    }
}
