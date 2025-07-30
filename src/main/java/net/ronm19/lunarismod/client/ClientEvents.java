package net.ronm19.lunarismod.client;


import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VelomirEntity;
import net.ronm19.lunarismod.entity.custom.VoidPhantomEntity;
import net.ronm19.lunarismod.util.KeyBinding;
import org.checkerframework.checker.units.qual.N;

import static net.ronm19.lunarismod.util.KeyBinding.DESCENDING_KEY;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = LunarisMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput( InputEvent.Key event ) {
            if (DESCENDING_KEY.consumeClick()) {
            }
        }

            @Mod.EventBusSubscriber(modid = LunarisMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
            public static class ClientModBusEvents {
                @SubscribeEvent
                public static void onKeyRegister( RegisterKeyMappingsEvent event ) {
                    event.register(DESCENDING_KEY);
                }
            }
        }
    }
