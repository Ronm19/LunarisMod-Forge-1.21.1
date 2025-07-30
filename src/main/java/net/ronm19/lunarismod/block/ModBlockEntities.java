package net.ronm19.lunarismod.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.custom.HuskstoneBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LunarisMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<HuskstoneBlockEntity>> HUSKSTONE =
            BLOCK_ENTITIES.register("huskstone_block_entity",
                    () -> BlockEntityType.Builder.of(HuskstoneBlockEntity::new, ModBlocks.HUSK_STONE_BLOCK.get()).build(null));
}
