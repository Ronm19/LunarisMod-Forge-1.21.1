package net.ronm19.lunarismod.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.block.ModBlocks;

public class ClientEffectManager {

    public static boolean isFullMoon(Level level) {
        return level.getMoonPhase() == 0;
    }

    public static boolean isHalfMoon(Level level) {
        return level.getMoonPhase() == 4;
    }

    public static boolean isNewMoon(Level level) {
        return level.getMoonPhase() == 7;
    }

    public static int getCurrentMoonPhase(Level level) {
        return level.getMoonPhase(); // Ranges from 0 (Full) to 7 (New)
    }

    public static String getMoonPhaseName(Level level) {
        int phase = level.getMoonPhase();
        return switch (phase) {
            case 0 -> "Full Moon";
            case 1, 2 -> "Waning";
            case 3 -> "Waning Half";
            case 4 -> "Half Moon";
            case 5, 6 -> "Waxing";
            case 7 -> "New Moon";
            default -> "Unknown";
        };
    }

    public static boolean isPlayerNearInfection(Player player, int radius) {
        BlockPos pos = player.blockPosition();
        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-radius, -2, -radius), pos.offset(radius, 2, radius))) {
            if (player.level().getBlockState(nearby).is(ModBlocks.HUSK_STONE_BLOCK.get())) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldApplyLunarFog(Player player) {
        Level level = player.level();
        return LunarConditions.isFullMoon(level) && isPlayerNearInfection(player, 10);
    }
}