package net.ronm19.lunarismod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.ronm19.lunarismod.LunarisMod;

public class PacketHandler {
    private static final SimpleChannel INSTANCE = ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "main"))
            .serverAcceptedVersions(( status, version ) -> true)
            .clientAcceptedVersions(( status, version ) -> true)
            .networkProtocolVersion(1)
            .simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(SPhantomControlPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SPhantomControlPacket::encode)
                .decoder(SPhantomControlPacket::new)
                .consumerMainThread((packet, context) -> {
                    ServerPlayer player = context.getSender();  // ✅ this gives you the sender
                    if (player != null) {
                        packet.handle(player);
                    }
                })
                .add();


    }

    public static void sendToServer(Object packet) {
        INSTANCE.send(packet, PacketDistributor.SERVER.noArg());

    }

    public static void sendToPlayer( Object packet, ServerPlayer player ) {
        INSTANCE.send(packet, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToAllClients(Object packet) {
        INSTANCE.send(packet, PacketDistributor.ALL.noArg());
    }
}
