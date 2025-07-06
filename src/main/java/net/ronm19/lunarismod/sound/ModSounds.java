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

    public static final RegistryObject<SoundEvent> NOCTRIUM_HAMMER_HIT = registerSoundEvent("noctrium_hammer_hit");
    public static final RegistryObject<SoundEvent> NOCTRIUM_HAMMER_FALL = registerSoundEvent("noctrium_hammer_fall");
    public static final RegistryObject<SoundEvent> NOCTRIUM_HAMMER_BREAK = registerSoundEvent("noctrium_hammer_break");
    public static final RegistryObject<SoundEvent> NOCTRIUM_HAMMER_CHARGE = registerSoundEvent("noctrium_hammer_charge");
    public static final RegistryObject<SoundEvent> NOCTRIUM_HAMMER_WHOOSH = registerSoundEvent("noctrium_hammer_whoosh");

    public static final RegistryObject<SoundEvent> VOID_HOWLER_HOWL = registerSoundEvent("void_howler_howl");



    public static final ForgeSoundType NOCTRIUM_HAMMER_SOUNDS = new ForgeSoundType(1f, 1f,
            ModSounds.NOCTRIUM_HAMMER_HIT, ModSounds.NOCTRIUM_HAMMER_FALL, ModSounds.NOCTRIUM_HAMMER_BREAK,
            ModSounds.NOCTRIUM_HAMMER_CHARGE, ModSounds.NOCTRIUM_HAMMER_WHOOSH);




    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENT.register(name, () ->  SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, name)));
    }



    public static void register(IEventBus eventBus) {
        SOUND_EVENT.register(eventBus);
    }
}
