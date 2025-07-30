package net.ronm19.lunarismod.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.*;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LunarisMod.MOD_ID);

    public static final RegistryObject<EntityType<LunarWolfEntity>> LUNARWOLF =
            ENTITY_TYPES.register("lunarwolf", () -> EntityType.Builder.of(LunarWolfEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 1.0f).build("lunarwolf"));

    public static final RegistryObject<EntityType<VoidHowlerEntity>> VOIDHOWLER =
            ENTITY_TYPES.register("voidhowler", () -> EntityType.Builder.of(VoidHowlerEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.3f).build("voidhowler"));

    public static final RegistryObject<EntityType<LunarSentinelEntity>> LUNARSENTINEL =
            ENTITY_TYPES.register("lunarsentinel", () -> EntityType.Builder.of(LunarSentinelEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 5.6f).build("lunarsentinel"));

    public static final RegistryObject<EntityType<LunarCreeperEntity>> LUNAR_CREEPER =
            ENTITY_TYPES.register("lunar_creeper", () -> EntityType.Builder.of(LunarCreeperEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f).build("lunar_creeper"));

    public static final RegistryObject<EntityType<VoidPhantomEntity>> VOID_PHANTOM =
            ENTITY_TYPES.register("void_phantom", () -> EntityType.Builder.of(VoidPhantomEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.8f).build("void_phantom"));

    public static final RegistryObject<EntityType<VoidEyeEntity>> VOID_EYE =
            ENTITY_TYPES.register("void_eye", () -> EntityType.Builder.of(VoidEyeEntity::new, MobCategory.MONSTER)
                    .sized(0.5f, 1.0f).build("void_eye"));

    public static final RegistryObject<EntityType<LunarEndermanEntity>> LUNAR_ENDERMAN =
            ENTITY_TYPES.register("lunar_enderman", () -> EntityType.Builder.of(LunarEndermanEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 3.2f).build("lunar_enderman"));

    public static final RegistryObject<EntityType<VelomirEntity>> VELOMIR =
            ENTITY_TYPES.register("velomir", () -> EntityType.Builder.of(VelomirEntity::new, MobCategory.CREATURE)
                    .sized(1.4f, 1.6f).build("velomir"));

    public static final RegistryObject<EntityType<LunarZombieEntity>> LUNAR_ZOMBIE =
            ENTITY_TYPES.register("lunar_zombie", () -> EntityType.Builder.of(LunarZombieEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.1f).build("lunar_zombie"));

    public static final RegistryObject<EntityType<LunarZombieKingEntity>> LUNAR_ZOMBIE_KING =
            ENTITY_TYPES.register("lunar_zombie_king", () -> EntityType.Builder.of(LunarZombieKingEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 2.7f).build("lunar_zombie_king"));

    public static final RegistryObject<EntityType<LunarHerobrineEntity>> LUNAR_HEROBRINE =
            ENTITY_TYPES.register("lunar_herobrine", () -> EntityType.Builder.of(LunarHerobrineEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 2.8f).build("lunar_herobrine"));

    public static final RegistryObject<EntityType<LunareonEntity>> LUNAREON =
            ENTITY_TYPES.register("lunareon", () -> EntityType.Builder.of(LunareonEntity::new, MobCategory.CREATURE)
                    .sized(0.8f, 1.5f).build("lunareon"));



    public static final RegistryObject<EntityType<NoctriumTomahawkProjectileEntity>> NOCTRIUM_TOMAHAWK =
                ENTITY_TYPES.register("noctrium_tomahawk", () -> EntityType.Builder.<NoctriumTomahawkProjectileEntity>of(NoctriumTomahawkProjectileEntity ::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("noctrium_tomahawk"));

    public static final RegistryObject<EntityType<VoidOrbEntity>> VOID_ORB =
            ENTITY_TYPES.register("void_orb", () -> EntityType.Builder.<VoidOrbEntity>of(VoidOrbEntity::new, MobCategory.MISC)
                            .sized(0.5f, 1.0f).build("void_orb"));





    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
