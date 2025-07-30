package net.ronm19.lunarismod.entity.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.ronm19.lunarismod.entity.ModEntities;

public class VoidOrbEntity extends ThrowableProjectile {
    private double baseDamage = 6.0D;

    public VoidOrbEntity(EntityType<VoidOrbEntity> type, Level level) {
        super(type, level);
    }

    public VoidOrbEntity(Level level, LivingEntity shooter) {
        super(ModEntities.VOID_ORB.get(), shooter, level);
    }

    public void setBaseDamage(double damage) {
        this.baseDamage = damage;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(target.damageSources().indirectMagic(this, this.getOwner()), (float) this.baseDamage);
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
        }
        playExplosionSound();
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        playExplosionSound();
        this.discard();
    }

    private void playExplosionSound() {
        Level level = this.level();
        if (!level.isClientSide) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // No data to sync
    }
}
