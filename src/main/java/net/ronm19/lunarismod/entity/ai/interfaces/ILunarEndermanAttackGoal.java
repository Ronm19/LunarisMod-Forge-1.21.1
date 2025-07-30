package net.ronm19.lunarismod.entity.ai.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface ILunarEndermanAttackGoal {
    void checkAndPerformAttack( LivingEntity pEnemy, double pDistToEnemySqr );
}
