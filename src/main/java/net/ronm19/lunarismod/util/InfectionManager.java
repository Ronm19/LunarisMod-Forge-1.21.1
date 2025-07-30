package net.ronm19.lunarismod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class InfectionManager {

    private static final HashMap<ServerLevel, InfectionManager> INSTANCES = new HashMap<>();

    public static InfectionManager get( ServerLevel level) {
        return INSTANCES.computeIfAbsent(level, l -> new InfectionManager());

    }

    public void triggerInfection( Player player, BlockPos origin) {
        UUID uuid = player.getUUID();
        System.out.println("[InfectionManager] Infection triggered for " + player.getName().getString() + " at " + origin);

        // Now add your triggerInfection method here
    }

    public static boolean isPlayerNearInfection( Entity entity) {
        if (!(entity instanceof Player player)) return false;
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-10, -2, -10), pos.offset(10, 2, 10))) {
            if (level.getBlockState(nearby).is(ModBlocks.HUSK_STONE_BLOCK.get())) {
                return true;
            }
        }
        return false;
    }

    private static final int BASE_SPREAD_RADIUS = 3;
    private static final int SPAWN_THRESHOLD = 50;

    public static void spreadAndCheck(Level level, BlockPos centerPos, Random random) {
        int infectedCount = 0;

        // Dynamic spread radius grows with infection
        int dynamicSpread = Math.min(6, BASE_SPREAD_RADIUS + infectedCount / 10);

        for (BlockPos pos : BlockPos.betweenClosed(
                centerPos.offset(-dynamicSpread, -1, -dynamicSpread),
                centerPos.offset(dynamicSpread, 1, dynamicSpread))) {

            if (shouldInfect(level, pos)) {
                infectBlock(level, pos);
                infectedCount++;
            }
        }

        if (infectedCount >= SPAWN_THRESHOLD) {
            spawnLunarZombies(level, centerPos, random);
            playThresholdReachedSound(level, centerPos);
        }
    }

    private static boolean shouldInfect(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir(); // Customize infection logic here
    }

    private static void infectBlock(Level level, BlockPos pos) {
        level.setBlock(pos, ModBlocks.HUSK_STONE_BLOCK.get().defaultBlockState(), 3);
    }

    private static void spawnLunarZombies(Level level, BlockPos pos, Random random) {
        for (int i = 0; i < 3; i++) {
            BlockPos spawnPos = pos.offset(random.nextInt(6) - 3, 0, random.nextInt(6) - 3);
            LunarZombieEntity zombie = new LunarZombieEntity(ModEntities.LUNAR_ZOMBIE.get(), level);
            zombie.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
            level.addFreshEntity(zombie);

            // Add spawn particle effect
            level.addParticle(ParticleTypes.SMOKE,
                    spawnPos.getX() + 0.5, spawnPos.getY() + 1, spawnPos.getZ() + 0.5,
                    0, 0.05, 0);
        }
    }

    private static void playThresholdReachedSound(Level level, BlockPos pos) {
        level.playSound(
                null,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.SOUL_ESCAPE,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );
    }
}