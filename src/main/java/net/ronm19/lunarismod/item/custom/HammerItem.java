package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class HammerItem extends DiggerItem {
    public HammerItem(Tier pTier, Properties pProperties) {
        super(pTier, BlockTags.MINEABLE_WITH_PICKAXE, pProperties);
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {

            // YEET the direct target
            double dx = target.getX() - attacker.getX();
            double dz = target.getZ() - attacker.getZ();
            double magnitude = Math.sqrt(dx * dx + dz * dz);

            if (magnitude != 0) {
                target.setDeltaMovement((dx / magnitude) * 12.0D, 2.0D, (dz / magnitude) * 12.0D);
                target.hasImpulse = true;
                target.hurtMarked = true;
            }

            // AoE knockback to nearby mobs
            double radius = 5.5D;
            List<LivingEntity> entities = attacker.level().getEntitiesOfClass(LivingEntity.class,
                    target.getBoundingBox().inflate(radius));
            for (LivingEntity entity : entities) {
                if (entity != target && entity != attacker) {
                    double ex = entity.getX() - attacker.getX();
                    double ez = entity.getZ() - attacker.getZ();
                    double dist = Math.sqrt(ex * ex + ez * ez);
                    if (dist != 0) {
                        entity.setDeltaMovement((ex / dist) * 8.0D, 1.5D, (ez / dist) * 8.0D);
                        entity.hasImpulse = true;
                        entity.hurtMarked = true;
                    }
                }
            }

            // STUN effect based on health and knockback resistance
            if (target instanceof LivingEntity livingTarget) {
                float maxHealth = livingTarget.getMaxHealth();
                double resistance = 0;
                if (livingTarget.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null) {
                    resistance = livingTarget.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                }

                int baseDuration = 40; // 2 seconds (20 ticks = 1 sec)
                int scaledDuration = baseDuration + (int) (maxHealth * 2) + (int) (resistance * 60);

                livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, scaledDuration, 4));
                livingTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, scaledDuration, 4));
                livingTarget.addEffect(new MobEffectInstance(MobEffects.GLOWING, scaledDuration, 0));
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(traceResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        if (traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP) {
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY(), initalBlockPos.getZ() + y));
                }
            }
        }

        if(traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY() + y, initalBlockPos.getZ()));
                }
            }
        }

        if(traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY() + y, initalBlockPos.getZ() + x));
                }
            }
        }

        return positions;
    }
}
