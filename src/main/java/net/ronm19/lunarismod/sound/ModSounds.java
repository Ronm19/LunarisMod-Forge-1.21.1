package net.ronm19.lunarismod.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LunarisMod.MOD_ID);


    public static final RegistryObject<SoundEvent> VOID_HOWLER_HOWL = registerSoundEvent("void_howler_howl");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENT.register(name, () ->  SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, name)));
    }



    public static void register(IEventBus eventBus) {
        SOUND_EVENT.register(eventBus);
    }
}
