package net.ronm19.lunarismod.entity.ai.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;

public interface RangedShooter {
    void setGlowing( boolean glowing );

    void onHit( HitResult pResult );

    void shootAtTarget( LivingEntity target);
}
