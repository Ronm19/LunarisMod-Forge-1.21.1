package net.ronm19.lunarismod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, LunarisMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.NOCTRIUM_BLOCK.get())
                .add(ModBlocks.RAW_NOCTRIUM_BLOCK.get())
                .add(ModBlocks.NOCTRIUM_ORE.get())
                .add(ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get())
                .add(ModBlocks.NOCTRIUM_DOOR.get())
                .add(ModBlocks.NOCTRIUM_FENCE.get())
                .add(ModBlocks.NOCTRIUM_WALL.get())
                .add(ModBlocks.NOCTRIUM_BUTTON.get())
                .add(ModBlocks.NOCTRIUM_TRAPDOOR.get())
                .add(ModBlocks.NOCTRIUM_STAIRS.get())
                .add(ModBlocks.NOCTRIUM_SLAB.get())
                .add(ModBlocks.NOCTRIUM_PRESSURE_PLATE.get())
                .add(ModBlocks.LUNAR_STONE_BLOCK.get())
                .add(ModBlocks.MOONSTONE_BLOCK.get())
                .add(ModBlocks.HUSK_STONE_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Blocks.GRASS_BLOCK)
                .add(ModBlocks.LUNAR_GRASS_BLOCK.get())
                .add(ModBlocks.LUNAR_DIRT_BLOCK.get());




        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get())
                .add(ModBlocks.NOCTRIUM_ORE.get())
                .add(ModBlocks.RAW_NOCTRIUM_BLOCK.get())
                .add(ModBlocks.NOCTRIUM_BLOCK.get())
                .add(ModBlocks.NOCTRIUM_DOOR.get())
                .add(ModBlocks.NOCTRIUM_FENCE.get())
                .add(ModBlocks.NOCTRIUM_WALL.get())
                .add(ModBlocks.NOCTRIUM_BUTTON.get())
                .add(ModBlocks.NOCTRIUM_TRAPDOOR.get())
                .add(ModBlocks.NOCTRIUM_STAIRS.get())
                .add(ModBlocks.NOCTRIUM_SLAB.get())
                .add(ModBlocks.NOCTRIUM_PRESSURE_PLATE.get())
                .add(ModBlocks.LUNAR_STONE_BLOCK.get())
                .add(ModBlocks.MOONSTONE_BLOCK.get())
                .add(ModBlocks.HUSK_STONE_BLOCK.get());



        tag(BlockTags.FENCES).add(ModBlocks.NOCTRIUM_FENCE.get());
        tag(BlockTags.FENCE_GATES).add(ModBlocks.NOCTRIUM_FENCE_GATE.get());
        tag(BlockTags.WALLS).add(ModBlocks.NOCTRIUM_WALL.get());

        tag(ModTags.Blocks.NEEDS_NOCTRIUM_TOOL)
                .add(ModBlocks.RAW_NOCTRIUM_BLOCK.get())
                .add(ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get())
                .add(ModBlocks.NOCTRIUM_ORE.get())
                .add(ModBlocks.RAW_NOCTRIUM_BLOCK.get())
                .add(ModBlocks.NOCTRIUM_BLOCK.get())
                .add(Blocks.OBSIDIAN)
                .add(ModBlocks.NOCTRIUM_DOOR.get())
                .add(ModBlocks.NOCTRIUM_FENCE.get())
                .add(ModBlocks.NOCTRIUM_WALL.get())
                .add(ModBlocks.NOCTRIUM_BUTTON.get())
                .add(ModBlocks.NOCTRIUM_TRAPDOOR.get())
                .add(ModBlocks.NOCTRIUM_STAIRS.get())
                .add(ModBlocks.NOCTRIUM_SLAB.get())
                .add(ModBlocks.NOCTRIUM_PRESSURE_PLATE.get())
                .add(ModBlocks.LUNAR_STONE_BLOCK.get())
                .add(ModBlocks.MOONSTONE_BLOCK.get())
                .add(ModBlocks.HUSK_STONE_BLOCK.get())
                .addTag(BlockTags.NEEDS_DIAMOND_TOOL);

        tag(ModTags.Blocks.INCORRECT_FOR_NOCTRIUM_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL)
                .remove(ModTags.Blocks.NEEDS_NOCTRIUM_TOOL);


        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.NOCTRIUM_LOG.get())
                .add(ModBlocks.NOCTRIUM_WOOD.get())
                .add(ModBlocks.STRIPPED_NOCTRIUM_LOG.get())
                .add(ModBlocks.STRIPPED_NOCTRIUM_WOOD.get());
    }
}