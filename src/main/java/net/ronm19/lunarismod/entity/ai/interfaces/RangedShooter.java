package net.ronm19.lunarismod.entity.ai.interfaces;

import net.minecraft.world.entity.LivingEntity;

public interface RangedShooter {
    void setGlowing( boolean glowing );

    void shootAtTarget( LivingEntity target);
}
