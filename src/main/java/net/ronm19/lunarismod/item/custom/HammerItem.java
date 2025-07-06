package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.ronm19.lunarismod.sound.ModSounds;

import java.util.ArrayList;
import java.util.List;

public class HammerItem extends DiggerItem {
    public HammerItem(Tier pTier, Properties pProperties) {
        super(pTier, BlockTags.MINEABLE_WITH_PICKAXE, pProperties);
    }

    // 🔊 On hit
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            attacker.level().playSound(
                    null,
                    attacker.getX(), attacker.getY(), attacker.getZ(),
                    ModSounds.NOCTRIUM_HAMMER_SOUNDS.getHitSound(),
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );

            // YEET logic
            double dx = target.getX() - attacker.getX();
            double dz = target.getZ() - attacker.getZ();
            double magnitude = Math.sqrt(dx * dx + dz * dz);

            if (magnitude != 0) {
                target.setDeltaMovement((dx / magnitude) * 12.0D, 2.0D, (dz / magnitude) * 12.0D);
                target.hasImpulse = true;
                target.hurtMarked = true;
            }

            // AoE knockback
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

            // Stun effect
            float maxHealth = target.getMaxHealth();
            double resistance = 0;
            if (target.getAttribute(Attributes.KNOCKBACK_RESISTANCE) != null) {
                resistance = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
            }

            int baseDuration = 40;
            int scaledDuration = baseDuration + (int) (maxHealth * 2) + (int) (resistance * 60);

            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, scaledDuration, 4));
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, scaledDuration, 4));
            target.addEffect(new MobEffectInstance(MobEffects.GLOWING, scaledDuration, 0));



            // 🔊 If about to break, play break sound
            if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
                attacker.level().playSound(
                        null,
                        attacker.getX(), attacker.getY(), attacker.getZ(),
                        ModSounds.NOCTRIUM_HAMMER_SOUNDS.getBreakSound(),
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f
                );
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    // 🔊 On swing
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        entity.level().playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                ModSounds.NOCTRIUM_HAMMER_WHOOSH.get(),
                SoundSource.PLAYERS,
                1.0f,
                1.0f
        );
        return super.onEntitySwing(stack, entity);
    }

    // 🔊 On right-click (charge)
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                ModSounds.NOCTRIUM_HAMMER_CHARGE.get(),
                SoundSource.PLAYERS,
                1.0f,
                1.0f
        );
        return super.use(level, player, hand);
    }

    // 🔊 On block mined
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!level.isClientSide()) {
            level.playSound(
                    null,
                    pos.getX(), pos.getY(), pos.getZ(),
                    ModSounds.NOCTRIUM_HAMMER_FALL.get(),
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
            );
        }
        return super.mineBlock(stack, level, state, pos, miner);
    }

    // AoE mining helper
    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(
                player.getEyePosition(1f),
                player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (traceResult.getType() == HitResult.Type.MISS) {
            return positions;
        }

        Direction direction = traceResult.getDirection();
        if (direction == Direction.DOWN || direction == Direction.UP) {
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(
                            initalBlockPos.getX() + x,
                            initalBlockPos.getY(),
                            initalBlockPos.getZ() + y));
                }
            }
        } else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(
                            initalBlockPos.getX() + x,
                            initalBlockPos.getY() + y,
                            initalBlockPos.getZ()));
                }
            }
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            for (int x = -range; x <= range; x++) {
                for (int y = -range; y <= range; y++) {
                    positions.add(new BlockPos(
                            initalBlockPos.getX(),
                            initalBlockPos.getY() + y,
                            initalBlockPos.getZ() + x));
                }
            }
        }

        return positions;
    }
}
