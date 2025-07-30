package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;

public class LunarPackSyncGoal extends Goal {
    private final LunarZombieEntity zombie;

    public LunarPackSyncGoal( LunarZombieEntity zombie) {
        this.zombie = zombie;
    }

    @Override
    public boolean canUse() {
        long nearbyCount = zombie.level().getEntitiesOfClass(LunarZombieEntity.class, zombie.getBoundingBox().inflate(5.0D)).size();
        return nearbyCount >= 3;
    }

    @Override
    public void start() {
        zombie.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.4D);
    }

    @Override
    public void stop() {
        zombie.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.0D);
    }
}
