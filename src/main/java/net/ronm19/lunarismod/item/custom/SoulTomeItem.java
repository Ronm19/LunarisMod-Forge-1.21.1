package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.custom.LunarHerobrineEntity;
import net.ronm19.lunarismod.event.LunarEventManager;

import static net.ronm19.lunarismod.entity.custom.LunarHerobrineEntity.trySummonLunarHerobrine;

public class SoulTomeItem extends Item {
    public SoulTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState state = level.getBlockState(clickedPos);

        // Check if clicked block is Soul Soil
        if (state.is(Blocks.SOUL_SOIL)) {
            if (!level.isClientSide) {
                BlockPos spawnPos = clickedPos.above(); // Spawn one block above

                summonHerobrine(level, spawnPos);

                // Play Wither spawn sound for drama
                level.playSound(null, spawnPos, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 0.8F);

                // Send ominous message to all players
                Component message = Component.literal("The air crackles as Herobrine rises...");
                for (Player player : level.players()) {
                    player.displayClientMessage(message, false);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            if (level.isClientSide && context.getPlayer() != null) {
                context.getPlayer().displayClientMessage(
                        Component.literal("The tome whispers: 'This place is unworthy.'"), true);
            }
            return InteractionResult.FAIL;
        }
    }

    private void summonHerobrine(Level level, BlockPos spawnPos) {
        // Spawn Herobrine entity
        LunarHerobrineEntity entity = new LunarHerobrineEntity(ModEntities.LUNAR_HEROBRINE.get(), level);
        entity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
        level.addFreshEntity(entity);

        // Add dramatic particles
        for (int i = 0; i < 20; i++) {
            double offsetX = level.random.nextGaussian() * 0.2;
            double offsetZ = level.random.nextGaussian() * 0.2;
            level.addParticle(ParticleTypes.SOUL,
                    spawnPos.getX() + 0.5 + offsetX,
                    spawnPos.getY() + 1.0,
                    spawnPos.getZ() + 0.5 + offsetZ,
                    0, 0.1, 0);
        }

        StructureTemplate template = level.getServer().getStructureManager()
                .getOrCreate(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "herobrine_altar"));

        if (template != null) {
            template.placeInWorld((ServerLevelAccessor) level, spawnPos, spawnPos,
                    new StructurePlaceSettings(), level.random, 2);
        }


        // Lightning strike for epic effect
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning != null) {
            lightning.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            level.addFreshEntity(lightning);
        }

        // Optional roar sound
        level.playSound(null, spawnPos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 1.0F, 0.5F);
    }
}
