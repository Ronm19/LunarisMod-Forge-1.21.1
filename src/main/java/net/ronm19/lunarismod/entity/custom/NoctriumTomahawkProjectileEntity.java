package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.item.ModItems;

import java.util.List;

public class NoctriumTomahawkProjectileEntity extends AbstractArrow {
    private float rotation;
    public Vec2 groundedOffset;

    private static final double EFFECT_RADIUS = 5.0;
    private static final double KNOCKBACK_STRENGTH = 1.5;
    private static final int GLOW_DURATION = 20 * 25; // 25 seconds
    private static final int FIRE_TICKS = 20 * 6;     // 6 seconds

    public NoctriumTomahawkProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public NoctriumTomahawkProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.NOCTRIUM_TOMAHAWK.get(), shooter, level, new ItemStack(ModItems.NOCTRIUM_TOMAHAWK.get()), null);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.NOCTRIUM_TOMAHAWK.get());
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public boolean isGrounded() {
        return inGround;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4);

        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION));

            if (!this.level().isNight()) {
                living.setRemainingFireTicks(FIRE_TICKS);
            }
        }

        if (!this.level().isClientSide) {
            applySpecialEffect(result.getLocation());
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Direction direction = result.getDirection();
        if (direction == Direction.SOUTH) groundedOffset = new Vec2(215f, 180f);
        if (direction == Direction.NORTH) groundedOffset = new Vec2(215f, 0f);
        if (direction == Direction.EAST)  groundedOffset = new Vec2(215f, -90f);
        if (direction == Direction.WEST)  groundedOffset = new Vec2(215f, 90f);
        if (direction == Direction.DOWN)  groundedOffset = new Vec2(115f, 180f);
        if (direction == Direction.UP)    groundedOffset = new Vec2(285f, 180f);

        if (!this.level().isClientSide) {
            applySpecialEffect(result.getLocation());
            this.discard();
        }
    }

    private void applySpecialEffect(Vec3 center) {
        if (this.level().isNight()) {
            spawnLightning(center);
        } else {
            applyKnockback(center);
        }
    }

    private void spawnLightning(Vec3 center) {
        int strikes = 3;
        for (int i = 0; i < strikes; i++) {
            double dx = (this.level().random.nextDouble() * 2 - 1) * EFFECT_RADIUS;
            double dz = (this.level().random.nextDouble() * 2 - 1) * EFFECT_RADIUS;
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
            if (bolt != null) {
                bolt.moveTo(center.x + dx, center.y, center.z + dz);
                this.level().addFreshEntity(bolt);
            }
        }
    }

    private void applyKnockback(Vec3 center) {
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(center.x - EFFECT_RADIUS, center.y - 1, center.z - EFFECT_RADIUS,
                        center.x + EFFECT_RADIUS, center.y + 2, center.z + EFFECT_RADIUS));

        for (LivingEntity target : targets) {
            if (target == this.getOwner()) continue;

            Vec3 force = target.position().subtract(center).normalize().scale(KNOCKBACK_STRENGTH);
            target.push(force.x, 0.5, force.z);

            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION));

            if (!this.level().isNight()) {
                target.setRemainingFireTicks(FIRE_TICKS);
            }
        }
    }
}
