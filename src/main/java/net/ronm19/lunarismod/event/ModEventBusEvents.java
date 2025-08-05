package net.ronm19.lunarismod.event;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.client.*;
import net.ronm19.lunarismod.entity.custom.*;
import net.ronm19.lunarismod.network.PacketHandler;

import static net.ronm19.lunarismod.entity.ModEntities.*;
import static net.ronm19.lunarismod.entity.ModEntities.VELOMIR;

@Mod.EventBusSubscriber(modid = LunarisMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LunarWolfModel.LAYER_LOCATION, LunarWolfModel::createBodyLayer);
        event.registerLayerDefinition(NoctriumTomahawkProjectileModel.LAYER_LOCATION, NoctriumTomahawkProjectileModel::createBodyLayer);
        event.registerLayerDefinition(VoidHowlerModel.LAYER_LOCATION, VoidHowlerModel::createBodyLayer);
        event.registerLayerDefinition(LunarSentinelModel.LAYER_LOCATION, LunarSentinelModel::createBodyLayer);
        event.registerLayerDefinition(LunarCreeperModel.LAYER_LOCATION, LunarCreeperModel::createBodyLayer);
        event.registerLayerDefinition(VoidPhantomModel.LAYER_LOCATION, VoidPhantomModel::createBodyLayer);
        event.registerLayerDefinition(VoidEyeModel.LAYER_LOCATION, VoidEyeModel::createBodyLayer);
        event.registerLayerDefinition(VoidOrbModel.LAYER_LOCATION, VoidOrbModel::createBodyLayer);
        event.registerLayerDefinition(LunarEndermanModel.LAYER_LOCATION, LunarEndermanModel::createBodyLayer);
        event.registerLayerDefinition(VelomirModel.LAYER_LOCATION, VelomirModel::createBodyLayer);
        event.registerLayerDefinition(LunarZombieModel.LAYER_LOCATION, LunarZombieModel::createBodyLayer);
        event.registerLayerDefinition(LunarHerobrineModel.LAYER_LOCATION, LunarHerobrineModel::createBodyLayer);
        event.registerLayerDefinition(LunarZombieKingModel.LAYER_LOCATION, LunarZombieKingModel::createBodyLayer);
        event.registerLayerDefinition(LunareonModel.LAYER_LOCATION, LunareonModel::createBodyLayer);
        event.registerLayerDefinition(LunarKnightModel.LAYER_LOCATION, LunarKnightModel::createBodyLayer);
        event.registerLayerDefinition(VoidWardenModel.LAYER_LOCATION, VoidWardenModel::createBodyLayer);
        event.registerLayerDefinition(LunarSpearProjectileModel.LAYER_LOCATION, LunarSpearProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.LUNARWOLF.get(),LunarWolfEntity.createAttributes().build());
        event.put(ModEntities.VOIDHOWLER.get(), VoidHowlerEntity.createAttributes().build());
        event.put(ModEntities.LUNARSENTINEL.get(), LunarSentinelEntity.createAttributes().build());
        event.put(ModEntities.LUNAR_CREEPER.get(), LunarCreeperEntity.createAttributes().build());
        event.put(ModEntities.VOID_PHANTOM.get(), VoidPhantomEntity.createAttributes().build());
        event.put(ModEntities.VOID_EYE.get(), VoidEyeEntity.createAttributes().build());
        event.put(ModEntities.LUNAR_ENDERMAN.get(), LunarEndermanEntity.createAttributes().build());
        event.put(VELOMIR.get(), VelomirEntity.createAttributes().build());
        event.put(LUNAR_ZOMBIE.get(), LunarZombieEntity.createAttributes().build());
        event.put(LUNAR_HEROBRINE.get(), LunarHerobrineEntity.createAttributes().build());
        event.put(LUNAR_ZOMBIE_KING.get(), LunarZombieKingEntity.createAttributes().build());
        event.put(LUNAREON.get(), LunareonEntity.createAttributes().build());
        event.put(LUNAR_KNIGHT.get(), LunarKnightEntity.createAttributes().build());
        event.put(VOID_WARDEN.get(), VoidWardenEntity.createAttributes().build());

    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.LUNARWOLF.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ( entityType, world, spawnReason, pos, random ) -> {
                    long time = world.dayTime() % 24000L;
                    boolean isNight = time >= 13000L && time <= 23000L;

                    // You can keep the usual spawn rules for animals too
                    boolean canSpawnHere = TamableAnimal.checkAnimalSpawnRules(entityType, world, spawnReason, pos, random);

                    return isNight && canSpawnHere;
                },
                SpawnPlacementRegisterEvent.Operation.REPLACE);

        event.register(ModEntities.VOIDHOWLER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ( entityType, world, spawnReason, pos, random ) -> {
                    long time = world.dayTime() % 24000L;
                    boolean isNight = time >= 13000L && time <= 23000L;

                    // You can keep the usual spawn rules for animals too
                    boolean canSpawnHere = TamableAnimal.checkAnimalSpawnRules(entityType, world, spawnReason, pos, random);

                    return isNight && canSpawnHere;
                },
                SpawnPlacementRegisterEvent.Operation.REPLACE);

        event.register(
                ModEntities.LUNARSENTINEL.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 1. Time‑of‑day check (night only) ---------- */
                    long dayTime = level.dayTime() % 24_000L;
                    boolean isNight = dayTime >= 13_000L && dayTime <= 23_000L;

                    /* ---------- 2. Delegate to your custom monster rules ---------- */
                    boolean sentinelRules = LunarSentinelEntity.checkMonsterSpawnRules(
                            entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return isNight && sentinelRules;
                },
                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                ModEntities.LUNAR_CREEPER.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 1. Time‑of‑day check (night only) ---------- */
                    long dayTime = level.dayTime() % 24_000L;
                    boolean isNight = dayTime >= 13_000L && dayTime <= 23_000L;

                    /* ---------- 2. Delegate to your custom monster rules ---------- */
                    boolean beastRules = LunarCreeperEntity.checkMonsterSpawnRules(
                            entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return isNight && beastRules;
                },
                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                VOID_PHANTOM.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules = VoidPhantomEntity.checkMonsterSpawnRules(
                            (EntityType<? extends Monster>) entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                VOID_EYE.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules;
                    if (VoidEyeEntity.checkMobSpawnRules(
                            (EntityType<? extends Mob>) entityType, level, spawnReason, pos, random)) {
                        beastRules = true;
                    } else beastRules = false;

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                LUNAR_ENDERMAN.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules = LunarEndermanEntity.checkMonsterSpawnRules(
                            (EntityType<? extends Monster>) entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                VELOMIR.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules = VelomirEntity.checkAnimalSpawnRules(
                            (EntityType<? extends Animal>) entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                LUNAR_ZOMBIE.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules = LunarZombieEntity.checkMobSpawnRules(
                            (EntityType<? extends Monster>) entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                LUNAR_ZOMBIE_KING.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules = LunarZombieKingEntity.checkMobSpawnRules(
                            (EntityType<? extends Monster>) entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE          // ▶ replace default rule
        );

        event.register(
                VOID_WARDEN.get(),                      // ▶ the entity
                SpawnPlacementTypes.ON_GROUND,                       // ▶ spawn on ground
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,            // ▶ standard heightmap
                ( entityType, level, spawnReason, pos, random ) -> {

                    /* ---------- 2. Delegate to your custom animal rules ---------- */
                    boolean beastRules = VoidWardenEntity.checkMobSpawnRules(
                            (EntityType<? extends Monster>) entityType, level, spawnReason, pos, random);

                    /* ---------- 3. Final verdict ---------- */
                    return beastRules;
                },

                SpawnPlacementRegisterEvent.Operation.REPLACE      // ▶ replace default rule
        );
    }






    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler :: register);
    }
}





