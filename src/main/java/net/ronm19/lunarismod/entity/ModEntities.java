package net.ronm19.lunarismod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.NoctriumTomahawkProjectileEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LunarisMod.MOD_ID);

    public static final RegistryObject<EntityType<LunarWolfEntity>> LUNARWOLF =
            ENTITY_TYPES.register("lunarwolf", () -> EntityType.Builder.of(LunarWolfEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 1.0f).build("lunarwolf"));

    public static final RegistryObject<EntityType<VoidHowlerEntity>> VOIDHOWLER =
            ENTITY_TYPES.register("voidhowler", () -> EntityType.Builder.of(VoidHowlerEntity ::new, MobCategory.CREATURE    )
                    .sized(0.7f, 1.3f).build("voidhowler"));

    public static final RegistryObject<EntityType<NoctriumTomahawkProjectileEntity>> NOCTRIUM_TOMAHAWK =
                ENTITY_TYPES.register("noctrium_tomahawk", () -> EntityType.Builder.<NoctriumTomahawkProjectileEntity>of(NoctriumTomahawkProjectileEntity ::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("noctrium_tomahawk"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
