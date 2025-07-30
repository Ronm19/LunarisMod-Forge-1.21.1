package net.ronm19.lunarismod.entity.ai.interfaces;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface IFlyingPathfinderMob {
    boolean canBeLeashed( Player player );

    Vec3 getRiderPosition();

    boolean getAddEntityPacket();

    boolean isFlying();

    default double preferredFlightAltitude() {
        return 70.0D;
    }

    default void debugFlyPrint() {
        System.out.println("Flying entity logic active.");
    }

    void tickRiding( Player player );
}
