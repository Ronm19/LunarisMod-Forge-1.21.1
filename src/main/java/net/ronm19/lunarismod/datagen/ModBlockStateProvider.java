package net.ronm19.lunarismod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LunarisMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.NOCTRIUM_BLOCK);
        blockWithItem(ModBlocks.RAW_NOCTRIUM_BLOCK);

        blockWithItem(ModBlocks.NOCTRIUM_ORE);
        blockWithItem(ModBlocks.NOCTRIUM_DEEPSLATE_ORE);

        stairsBlock(ModBlocks.NOCTRIUM_STAIRS.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));
        slabBlock(ModBlocks.NOCTRIUM_SLAB.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));

        buttonBlock(ModBlocks.NOCTRIUM_BUTTON.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));
        pressurePlateBlock(ModBlocks.NOCTRIUM_PRESSURE_PLATE.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));

        fenceBlock(ModBlocks.NOCTRIUM_FENCE.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));
        fenceGateBlock(ModBlocks.NOCTRIUM_FENCE_GATE.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));
        wallBlock(ModBlocks.NOCTRIUM_WALL.get(), blockTexture(ModBlocks.NOCTRIUM_BLOCK.get()));

        doorBlockWithRenderType(ModBlocks.NOCTRIUM_DOOR.get(), modLoc("block/noctrium_door_bottom"), modLoc("block/noctrium_door_top"), "cutout");
        trapdoorBlockWithRenderType(ModBlocks.NOCTRIUM_TRAPDOOR.get(), modLoc("block/noctrium_trapdoor"), true, "cutout");

        blockItem(ModBlocks.NOCTRIUM_STAIRS);
        blockItem(ModBlocks.NOCTRIUM_SLAB);
        blockItem(ModBlocks.NOCTRIUM_PRESSURE_PLATE);
        blockItem(ModBlocks.NOCTRIUM_FENCE_GATE);
        blockItem(ModBlocks.NOCTRIUM_TRAPDOOR, "_bottom");
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void blockItem(RegistryObject<? extends Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("lunarismod:block/" +
                ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockItem(RegistryObject<? extends Block> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("lunarismod:block/" +
                ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath() + appendix));
    }
}
