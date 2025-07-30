package net.ronm19.lunarismod.util.lighting;

public enum LightLevel {
    NONE(0),
    LOW(5),
    MEDIUM(10),
    HIGH(15);

    private final int lightValue;

    LightLevel(int lightValue) {
        this.lightValue = lightValue;
    }

    public int getValue() {
        return lightValue;
    }
}