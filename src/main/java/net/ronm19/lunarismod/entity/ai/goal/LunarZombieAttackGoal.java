package net.ronm19.lunarismod.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;

public class LunarZombieAttackGoal extends MeleeAttackGoal {
    private final LunarZombieEntity lunarZombie;
    private int raiseArmTicks;

    public LunarZombieAttackGoal( LunarZombieEntity pZombie, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen ) {
        super(pZombie, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.lunarZombie = pZombie;
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.lunarZombie.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.lunarZombie.setAggressive(true);
        } else {
            this.lunarZombie.setAggressive(false);
        }
    }
}
