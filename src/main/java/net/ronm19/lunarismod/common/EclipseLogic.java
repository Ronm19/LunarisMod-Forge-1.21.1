package net.ronm19.lunarismod.common;

import net.minecraft.client.Minecraft;

public class EclipseLogic {
    private static boolean isEclipseActive = false;

    public static void tickClient() {
        if (Minecraft.getInstance().level == null) return;

        long day = Minecraft.getInstance().level.getDayTime() / 24000L;
        int moonPhase = (int)(day % 8);

        // You can make this conditional: only every 4th full moon, or use randomness
        isEclipseActive = moonPhase == 0;
    }

    public static boolean shouldRenderEclipse() {
        return isEclipseActive;
    }
}
