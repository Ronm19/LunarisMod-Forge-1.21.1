package net.ronm19.lunarismod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider,
                              CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, LunarisMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.NOCTRIUM_HELMET.get())
                .add(ModItems.NOCTRIUM_CHESTPLATE.get())
                .add(ModItems.NOCTRIUM_LEGGINGS.get())
                .add(ModItems.NOCTRIUM_BOOTS.get())
                .add(ModItems.MOON_HELMET.get())
                .add(ModItems.MOON_CHESTPLATE.get())
                .add(ModItems.MOON_LEGGINGS.get())
                .add(ModItems.MOON_BOOTS.get());


        tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.NOCTRIUM_LOG.get().asItem())
                .add(ModBlocks.NOCTRIUM_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_NOCTRIUM_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_NOCTRIUM_WOOD.get().asItem());

        tag(ItemTags.PLANKS)
                .add(ModBlocks.NOCTRIUM_PLANKS.get().asItem());
    }
}
