package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class HammerItem extends DiggerItem {

    public HammerItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide()) {
            applyStunAndKnockback(target, attacker);
            applyAoEKnockback(target, attacker);
        }

        if (attacker instanceof Player player) {
            stack.hurtAndBreak(1, player, null);
        }

        return true;
    }

    private void applyStunAndKnockback(LivingEntity target, LivingEntity attacker) {
        float maxHealth = target.getMaxHealth();
        double resistance = 0.0;
        if (target.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null) {
            resistance = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
        }

        int duration = 40 + (int) (maxHealth * 2) + (int) (resistance * 60);

        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 4));
        target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, duration, 4));
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, duration, 0));

        double dx = target.getX() - attacker.getX();
        double dz = target.getZ() - attacker.getZ();
        double scale = 15.0;

        double length = Math.sqrt(dx * dx + dz * dz);
        if (length != 0) {
            dx /= length;
            dz /= length;
        }

        target.setDeltaMovement(dx * scale, 6.0, dz * scale);
        target.hurtMarked = true;

        ServerLevel serverLevel = (ServerLevel) target.level();
        serverLevel.sendParticles(ParticleTypes.EXPLOSION, target.getX(), target.getY() + 1.0, target.getZ(), 20, 0.5, 1, 0.5, 0.1);
        serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 2.0F, 0.6F + serverLevel.random.nextFloat() * 0.2F);
    }

    private void applyAoEKnockback(LivingEntity centerTarget, LivingEntity attacker) {
        double aoeRadius = 8.0;
        ServerLevel level = (ServerLevel) centerTarget.level();

        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(
                LivingEntity.class,
                centerTarget.getBoundingBox().inflate(aoeRadius),
                e -> e.isAlive() && e != centerTarget && e != attacker
        );

        for (LivingEntity other : nearbyEntities) {
            double dx = other.getX() - attacker.getX();
            double dz = other.getZ() - attacker.getZ();
            double distanceSq = dx * dx + dz * dz;

            if (distanceSq == 0) continue;

            double length = Math.sqrt(distanceSq);
            dx /= length;
            dz /= length;

            double aoeScale = 8.0;
            other.setDeltaMovement(dx * aoeScale, 2.5, dz * aoeScale);
            other.hurtMarked = true;

            other.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
        }
    }

    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos origin, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult trace = player.level().clip(new ClipContext(
                player.getEyePosition(1f),
                player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f)),
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                player
        ));

        if (trace.getType() == HitResult.Type.MISS) return positions;

        Direction face = trace.getDirection();

        if (face == Direction.UP || face == Direction.DOWN) {
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    positions.add(origin.offset(x, 0, z));
                }
            }
        } else if (face == Direction.NORTH || face == Direction.SOUTH) {
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    positions.add(origin.offset(x, y, 0));
                }
            }
        } else if (face == Direction.EAST || face == Direction.WEST) {
            for (int z = -range; z <= range; z++) {
                for (int y = -range; y <= range; y++) {
                    positions.add(origin.offset(0, y, z));
                }
            }
        }

        return positions;
    }
}