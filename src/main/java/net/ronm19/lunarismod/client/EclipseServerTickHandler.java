package net.ronm19.lunarismod.client;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.common.EclipseStateManager;

@Mod.EventBusSubscriber
public class EclipseServerTickHandler {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Level level = event.level;
        if (level.isClientSide()) return; // Server only

        // Check if it’s nighttime: dayTime in ticks, nighttime ~ 13000-23000
        long time = level.getDayTime() % 24000;
        boolean isNight = time >= 13000 && time <= 23000;

        // Check if it is full moon: moon phase 0 means full moon
        boolean isFullMoon = level.getMoonPhase() == 0;

        // Your custom eclipse condition
        boolean eclipseActive = isNight && isFullMoon && isPurpleMoonEventActive(level);

        // Update the eclipse state (server-side)
        EclipseStateManager.setEclipseActive(eclipseActive);

        // (Optional) Sync eclipse state to clients here if needed
    }

    // Placeholder for your purple moon event logic
    private static boolean isPurpleMoonEventActive(Level level) {
        // For now, always true to simulate eclipse on full moon nights
        // You can add logic here based on your mod’s calendar, random chance, or config
        return true;
    }
}
