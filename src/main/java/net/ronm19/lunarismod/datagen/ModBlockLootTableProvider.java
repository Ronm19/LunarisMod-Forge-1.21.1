package net.ronm19.lunarismod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider pRegistries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.NOCTRIUM_BLOCK.get());
        dropSelf(ModBlocks.RAW_NOCTRIUM_BLOCK.get());

        this.add(ModBlocks.NOCTRIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.NOCTRIUM_ORE.get(), ModItems.RAW_NOCTRIUM_GEM.get()));
        this.add(ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get(), ModItems.RAW_NOCTRIUM_GEM.get(), 2, 6));

        dropSelf(ModBlocks.NOCTRIUM_STAIRS.get());
        this.add(ModBlocks.NOCTRIUM_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.NOCTRIUM_SLAB.get()));

        dropSelf(ModBlocks.NOCTRIUM_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.NOCTRIUM_BUTTON.get());
        dropSelf(ModBlocks.NOCTRIUM_FENCE.get());
        dropSelf(ModBlocks.NOCTRIUM_FENCE_GATE.get());
        dropSelf(ModBlocks.NOCTRIUM_WALL.get());
        dropSelf(ModBlocks.NOCTRIUM_TRAPDOOR.get());

        this.add(ModBlocks.NOCTRIUM_DOOR.get(),
                block -> createDoorTable(ModBlocks.NOCTRIUM_DOOR.get()));

        this.dropSelf(ModBlocks.NOCTRIUM_LOG.get());
        this.dropSelf(ModBlocks.NOCTRIUM_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_NOCTRIUM_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_NOCTRIUM_WOOD.get());
        this.dropSelf(ModBlocks.NOCTRIUM_PLANKS.get());
        this.dropSelf(ModBlocks.NOCTRIUM_SAPLING.get());

        this.add(ModBlocks.NOCTRIUM_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.NOCTRIUM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
                pBlock, this.applyExplosionDecay(
                        pBlock, LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                                .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
