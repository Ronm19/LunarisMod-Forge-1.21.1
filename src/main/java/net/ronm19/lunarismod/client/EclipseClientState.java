package net.ronm19.lunarismod.client;

public class EclipseClientState {
    private static boolean eclipseActive = false;

    public static boolean isEclipseActive() {
        return eclipseActive;
    }

    public static void setEclipseActive(boolean active) {
        eclipseActive = active;
    }

    // Optional: tickClient() can handle client-side timers or fade effects if you want
    public static void tickClient() {
        // No-op for now
    }
}
