package net.ronm19.lunarismod.common;

public class EclipseStateManager {
    private static boolean eclipseActive = false;

    public static void setEclipseActive(boolean active) {
        eclipseActive = active;
    }

    public static boolean isEclipseActive() {
        return eclipseActive;
    }
}
