package net.ronm19.lunarismod.event;

import net.minecraft.world.level.Level;

public class LunarEventManager {

    public static boolean isBloodMoon( Level level) {
        // You can check time, moon phase, and custom conditions
        long time = level.getDayTime() % 24000; // Vanilla day length
        boolean isNight = time >= 13000 && time <= 23000;

        // Add your custom logic for Blood Moon
        return isNight && level.getMoonPhase() == 4 && level.isRaining(); // Example condition
    }
}