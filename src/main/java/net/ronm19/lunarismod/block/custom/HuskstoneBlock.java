package net.ronm19.lunarismod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.ronm19.lunarismod.block.ModBlockEntities;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.ai.interfaces.HuskstoneBlockInfection;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.util.InfectionManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HuskstoneBlock extends Block implements HuskstoneBlockInfection {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public HuskstoneBlock(BlockBehaviour.Properties properties) {
        super(properties.randomTicks()); // Enable randomTick
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }



    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<BlockPos> candidates = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            BlockPos adjacentPos = pos.relative(dir);
            BlockState adjacentState = level.getBlockState(adjacentPos);

            if (isInfectable(adjacentState)) {
                candidates.add(adjacentPos);
            }

        }

        if (!candidates.isEmpty()) {
            BlockPos targetPos = candidates.get(random.nextInt(candidates.size()));
            level.setBlockAndUpdate(targetPos, ModBlocks.HUSK_STONE_BLOCK.get().defaultBlockState());

            level.playSound(null,                            // No player — plays globally
                    targetPos.getX() + 0.5,          // X coordinate
                    targetPos.getY() + 0.5,          // Y coordinate
                    targetPos.getZ() + 0.5,          // Z coordinate
                    SoundEvents.SOUL_ESCAPE,         // Sound
                    SoundSource.BLOCKS,              // Category
                    0.8F,                             // Volume
                    1.2F                              // Pitch
            );
            level.sendParticles(ParticleTypes.SMOKE, targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5, 8, 0.3, 0.3, 0.3, 0.01);
        }
    }


    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HuskstoneBlockEntity(pos, state);
    }


    public <T extends BlockEntity> BlockEntityTicker<T> getTicker( Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.HUSKSTONE.get() ?
                (lvl, pos, st, be) -> HuskstoneBlockEntity.tick(lvl, pos, st, (HuskstoneBlockEntity) be) :
                null;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(ACTIVE)) return;

        BlockPos targetPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - 1, random.nextInt(3) - 1);
        BlockState targetState = level.getBlockState(targetPos);

        if (targetState.is(BlockTags.BASE_STONE_OVERWORLD)) {
            level.setBlock(targetPos, ModBlocks.HUSK_STONE_BLOCK.get().defaultBlockState(), 3);
            level.playSound(null, targetPos, SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS, 0.6F, 1.2F);
            level.sendParticles(ParticleTypes.SCULK_SOUL, targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.01);
        }



        // Schedule next spread tick
        level.scheduleTick(pos, this, 20 + random.nextInt(20));
    }

    @Override
    public void setPlacedBy( Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide && placer instanceof Player player) {
            InfectionManager infectionManager = InfectionManager.get((ServerLevel) level);
            infectionManager.triggerInfection(player, pos);
        }

    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            boolean isActive = state.getValue(ACTIVE);

            // Check for correct item in hand
            if (player.getItemInHand(hand).is(ModItems.VOID_PULSE.get())) {
                if (!isActive) {
                    // Activate block
                    level.setBlock(pos, state.setValue(ACTIVE, true), 3);
                    level.playSound(null, pos, SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);

                    player.sendSystemMessage(Component.literal("Huskstone awakened..."));
                    return InteractionResult.SUCCESS;
                } else {
                    player.sendSystemMessage(Component.literal("Already active."));
                    return InteractionResult.SUCCESS;
                }
            } else {
                player.sendSystemMessage(Component.literal("Void Pulse required to awaken Huskstone."));
                return InteractionResult.FAIL;
            }
        }

        LunarZombieEntity lunarZombie = ModEntities.LUNAR_ZOMBIE.get().create(level);
        if (lunarZombie != null) {
            lunarZombie.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, level.random.nextFloat() * 360F, 0);
            level.addFreshEntity(lunarZombie);
        }

        return InteractionResult.SUCCESS;
    }

    private boolean isInfectable(BlockState state) {
        // You can customize this list further
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.SAND);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);

        if (!level.isClientSide && state.getValue(ACTIVE) && state.getBlock() != newState.getBlock()) {
            // Potential retaliation logic or particle burst could go here
        }

    }
}