package net.ronm19.lunarismod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.ronm19.lunarismod.block.ModBlockEntities;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;

import java.util.List;

public class HuskstoneBlockEntity extends BlockEntity {

    private int infectionLevel = 0;
    private int cooldown = 20;

    public HuskstoneBlockEntity( BlockPos pos, BlockState state) {
        super(ModBlockEntities.HUSKSTONE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, HuskstoneBlockEntity blockEntity, BlockState state) {
        if (level.isClientSide) return;

        blockEntity.cooldown--;

        if (blockEntity.cooldown <= 0) {
            blockEntity.cooldown = 200; // Spawn every 10 seconds (200 ticks)

            LunarZombieEntity zombie = ModEntities.LUNAR_ZOMBIE.get().create(level);
            if (zombie != null) {
                zombie.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                level.addFreshEntity(zombie);

                level.playSound(null, pos, SoundEvents.ZOMBIE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                ((ServerLevel) level).sendParticles(ParticleTypes.SCULK_SOUL, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.01);
            }
        }
    }

    private void spreadInfection() {
        for (Direction direction : Direction.values()) {
            BlockPos targetPos = worldPosition.relative(direction);
            BlockState targetState = level.getBlockState(targetPos);

            if (targetState.isAir()) {
                level.setBlock(targetPos, ModBlocks.HUSK_STONE_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }

    private void summonMob() {
        LunarZombieEntity zombie = ModEntities.LUNAR_ZOMBIE.get().create(level);
        if (zombie != null) {
            zombie.setPos(
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1,
                    worldPosition.getZ() + 0.5
            );
            level.addFreshEntity(zombie);
        }
    }

    public static void tick( Level level, BlockPos pos, BlockState state, HuskstoneBlockEntity blockEntity) {
        blockEntity.tick(); // where your logic lives
        pos = blockEntity.getBlockPos();
        level.playSound(null, pos, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 0.8f, 1.4f); {
        }
    }

    private void tick() {
    }

    private void applyDebuffToPlayersNearby() {
        AABB area = new AABB(worldPosition).inflate(5);
        List<Player> players = level.getEntitiesOfClass(Player.class, area);

        for (Player player : players) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
        }
    }

    private void spawnParticles() {
        if (infectionLevel >= 1 && level instanceof ServerLevel serverLevel) {
            double x = worldPosition.getX() + 0.5;
            double y = worldPosition.getY() + 1.1;
            double z = worldPosition.getZ() + 0.5;

            serverLevel.sendParticles(
                    ParticleTypes.SCULK_SOUL,
                    x, y, z,
                    5,
                    0.25, 0.25, 0.25,
                    0.01
            );
        }
    }
}