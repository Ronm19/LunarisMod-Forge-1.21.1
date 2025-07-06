package net.ronm19.lunarismod.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.client.LunarWolfModel;
import net.ronm19.lunarismod.entity.client.NoctriumTomahawkProjectileModel;
import net.ronm19.lunarismod.entity.client.VoidHowlerModel;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

@Mod.EventBusSubscriber(modid = LunarisMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LunarWolfModel.LAYER_LOCATION, LunarWolfModel::createBodyLayer);
        event.registerLayerDefinition(NoctriumTomahawkProjectileModel.LAYER_LOCATION, NoctriumTomahawkProjectileModel::createBodyLayer);
        event.registerLayerDefinition(VoidHowlerModel.LAYER_LOCATION, VoidHowlerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.LUNARWOLF.get(),LunarWolfEntity.createAttributes().build());
        event.put(ModEntities.VOIDHOWLER.get(), VoidHowlerEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.LUNARWOLF.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, spawnReason, pos, random) -> {
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
                (entityType, world, spawnReason, pos, random) -> {
                    long time = world.dayTime() % 24000L;
                    boolean isNight = time >= 13000L && time <= 23000L;

                    // You can keep the usual spawn rules for animals too
                    boolean canSpawnHere = TamableAnimal.checkAnimalSpawnRules(entityType, world, spawnReason, pos, random);

                    return isNight && canSpawnHere;
                },
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
