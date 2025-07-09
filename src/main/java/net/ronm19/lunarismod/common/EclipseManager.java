package net.ronm19.lunarismod.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class EclipseManager {
    private static boolean eclipseActive = false;
    private static long lastCheckedDay = -1;
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        if (!(event.level instanceof ServerLevel level)) return;

        long day = level.getDayTime() / 24000L; // Minecraft day count

        if (day == lastCheckedDay) return; // already checked this day

        lastCheckedDay = day;

        int moonPhase = level.getMoonPhase(); // 0 is full moon
        if (moonPhase == 0) {
            // 30% chance for eclipse on this full moon
            boolean shouldEclipse = random.nextFloat() < 0.3;

            setEclipseActive(shouldEclipse);

        } else {
            setEclipseActive(false);
        }
    }

    public static void setEclipseActive(boolean active) {
        eclipseActive = active;
        // Optionally notify clients or update states here
        System.out.println("Eclipse active: " + active);
    }

    public static boolean isEclipseActive() {
        return eclipseActive;
    }
}
