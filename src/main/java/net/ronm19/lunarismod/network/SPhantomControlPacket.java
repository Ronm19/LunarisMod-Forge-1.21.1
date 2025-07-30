package net.ronm19.lunarismod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.ronm19.lunarismod.entity.custom.VoidPhantomEntity;

public class SPhantomControlPacket {
    private final boolean ascend;
    private final boolean descend;

    public SPhantomControlPacket(FriendlyByteBuf buf) {
        this.ascend = buf.readBoolean();
        this.descend = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(ascend);
        buf.writeBoolean(descend);
    }

    public void handle(ServerPlayer player) {
        if (player.getVehicle() instanceof VoidPhantomEntity phantom) {
            phantom.setShouldAscend(ascend);
            phantom.setShouldDescend(descend);

        }
    }
}

