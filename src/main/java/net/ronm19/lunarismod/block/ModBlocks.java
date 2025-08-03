package net.ronm19.lunarismod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.custom.HuskstoneBlock;
import net.ronm19.lunarismod.block.custom.ModFlammableRotatedPillarBlock;
import net.ronm19.lunarismod.block.custom.ModSapplingBlock;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.worldgen.tree.ModTreeGrowers;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, LunarisMod.MOD_ID);

    public static final RegistryObject<Block> NOCTRIUM_BLOCK = registerBlock("noctrium_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(5f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> RAW_NOCTRIUM_BLOCK = registerBlock("raw_noctrium_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> LUNAR_GRASS_BLOCK = registerBlock("lunar_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.of()
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> LUNAR_DIRT_BLOCK = registerBlock("lunar_dirt_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f).requiresCorrectToolForDrops().sound(SoundType.ROOTED_DIRT)));
    public static final RegistryObject<Block> LUNAR_STONE_BLOCK = registerBlock("lunar_stone_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> MOONSTONE_BLOCK = registerBlock("moonstone_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> HUSK_STONE_BLOCK = registerBlock("husk_stone_block",
            () -> new HuskstoneBlock(BlockBehaviour.Properties.of().randomTicks()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.STONE).isValidSpawn((state, getter, pos, type) ->
                            state.getValue(HuskstoneBlock.ACTIVE) && type == ModEntities.LUNAR_ZOMBIE.get())));

    public static final RegistryObject<Block> NOCTRIUM_ORE = registerBlock("noctrium_ore",
            () -> new DropExperienceBlock(UniformInt.of(2,4),BlockBehaviour.Properties.of()
                    .strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> NOCTRIUM_DEEPSLATE_ORE = registerBlock("noctrium_deepslate_ore",
            () -> new DropExperienceBlock(UniformInt.of(3, 6),BlockBehaviour.Properties.of()
                    .strength(5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<StairBlock> NOCTRIUM_STAIRS = registerBlock("noctrium_stairs",
            () -> new StairBlock(ModBlocks.NOCTRIUM_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<SlabBlock> NOCTRIUM_SLAB = registerBlock("noctrium_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

    public static final RegistryObject<PressurePlateBlock>  NOCTRIUM_PRESSURE_PLATE = registerBlock("noctrium_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.DARK_OAK, BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<ButtonBlock>  NOCTRIUM_BUTTON = registerBlock("noctrium_button",
            () -> new ButtonBlock(BlockSetType.DARK_OAK,20, BlockBehaviour.Properties.of().strength(4f)
                    .requiresCorrectToolForDrops().noCollission()));

    public static final RegistryObject<FenceBlock>  NOCTRIUM_FENCE = registerBlock("noctrium_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<FenceGateBlock>  NOCTRIUM_FENCE_GATE = registerBlock("noctrium_fence_gate",
            () -> new FenceGateBlock(WoodType.ACACIA, BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));
    public static final RegistryObject<WallBlock>  NOCTRIUM_WALL = registerBlock("noctrium_wall",
            () -> new WallBlock(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

    public static final RegistryObject<DoorBlock>  NOCTRIUM_DOOR = registerBlock("noctrium_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<TrapDoorBlock>  NOCTRIUM_TRAPDOOR = registerBlock("noctrium_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().noOcclusion()));

    public static final RegistryObject<RotatedPillarBlock> NOCTRIUM_LOG = registerBlock("noctrium_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> NOCTRIUM_WOOD = registerBlock("noctrium_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_NOCTRIUM_LOG = registerBlock("stripped_noctrium_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_NOCTRIUM_WOOD = registerBlock("stripped_noctrium_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> NOCTRIUM_PLANKS = registerBlock("noctrium_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final RegistryObject<Block> NOCTRIUM_LEAVES = registerBlock("noctrium_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static boolean isTreeBlock(Block block) {
        return block.defaultBlockState().is(BlockTags.LOGS) || block.defaultBlockState().is(BlockTags.LEAVES);
    }

    public static final RegistryObject<Block> NOCTRIUM_SAPLING = registerBlock("noctrium_sapling",
            () -> new ModSapplingBlock(ModTreeGrowers.NOCTRIUM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), ModBlocks.LUNAR_GRASS_BLOCK.get()));

    private static<T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    private static<T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
