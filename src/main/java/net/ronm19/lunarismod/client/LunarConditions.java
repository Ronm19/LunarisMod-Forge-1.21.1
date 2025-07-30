package net.ronm19.lunarismod.client;

import net.minecraft.world.level.Level;

public class LunarConditions {

    // Full moon is phase 0 in Minecraft
    public static boolean isFullMoon(Level level) {
        return level.getMoonPhase() == 0;
    }

    public static boolean isHalfMoon(Level level) {
        return level.getMoonPhase() == 4;
    }

    public static boolean isNewMoon(Level level) {
        return level.getMoonPhase() == 7;
    }

    // Optional: more fine-grained control
    public static int getCurrentMoonPhase(Level level) {
        return level.getMoonPhase(); // 0–7
    }

    public static String getMoonPhaseName(Level level) {
        int phase = level.getMoonPhase();
        return switch (phase) {
            case 0 -> "Full Moon";
            case 1, 2 -> "Waning";
            case 3 -> "Half";
            case 4, 5 -> "Waxing";
            case 6 -> "Crescent";
            case 7 -> "New Moon";
            default -> "Unknown";
        };
    }
}