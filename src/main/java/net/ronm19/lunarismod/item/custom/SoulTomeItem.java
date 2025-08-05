package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.custom.LunarHerobrineEntity;
import net.ronm19.lunarismod.entity.custom.LunarKnightEntity;

public class SoulTomeItem extends Item {

    private static final int PARTICLE_COUNT = 20;

    public SoulTomeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState state = level.getBlockState(clickedPos);
        Player player = context.getPlayer();

        if (!level.isClientSide) {
            BlockPos spawnPos = clickedPos.above();

            if (state.is(Blocks.SOUL_SOIL)) {
                summonHerobrine(level, spawnPos);
                broadcastMessage(level, "§5The air crackles as Herobrine rises...");
                level.playSound(null, spawnPos, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 0.8F);
                return InteractionResult.SUCCESS;
            }

            if (state.is(ModBlocks.MOONSTONE_BLOCK.get())) {
                summonLunarKnights(level, spawnPos);
                broadcastMessage(level, "§bYour wish is our command, my liege.");
                level.playSound(null, spawnPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.2F);
                return InteractionResult.SUCCESS;
            }
        }

        if (level.isClientSide && player != null) {
            player.displayClientMessage(
                    Component.literal("§7The tome whispers: 'This place is unworthy.'"), true);
        }

        return InteractionResult.FAIL;
    }

    private void summonHerobrine(Level level, BlockPos spawnPos) {
        // Spawn Herobrine entity
        LunarHerobrineEntity herobrine = new LunarHerobrineEntity(ModEntities.LUNAR_HEROBRINE.get(), level);
        herobrine.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
        level.addFreshEntity(herobrine);

        spawnParticles(level, ParticleTypes.SOUL, spawnPos);

        // Place altar structure if available
        if (level instanceof ServerLevelAccessor accessor && level.getServer() != null) {
            StructureTemplate template = level.getServer().getStructureManager()
                    .getOrCreate(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "herobrine_altar"));

            if (template != null) {
                template.placeInWorld(accessor, spawnPos, spawnPos,
                        new StructurePlaceSettings(), level.random, 2);
            }
        }

        strikeLightning(level, spawnPos);
        level.playSound(null, spawnPos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 1.0F, 0.5F);
    }

    private void summonLunarKnights(Level level, BlockPos spawnPos) {
        for (int i = 0; i < 2; i++) {
            LunarKnightEntity knight = new LunarKnightEntity(ModEntities.LUNAR_KNIGHT.get(), level);
            knight.moveTo(spawnPos.getX() + 0.5 + i, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
            knight.setTomeSummoned(true);

            // Prevent the knight from being on fire when spawned
            knight.extinguishFire();

            level.addFreshEntity(knight);
        }

        // Your other effects here
        spawnParticles(level, ParticleTypes.END_ROD, spawnPos);

        // If you want to remove lightning effects (strikeLightning call), do NOT call strikeLightning here

        // Play the voiceline louder
        SoundEvent summonVoice = SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "voice.lunar_knight_summon"));
        level.playSound(null, spawnPos, summonVoice, SoundSource.HOSTILE, 7.0F, 1.0F);

        // Toast/challenge complete sound
        level.playSound(null, spawnPos, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }



    private void spawnParticles( Level level, SimpleParticleType type, BlockPos pos) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double offsetX = level.random.nextGaussian() * 0.2;
            double offsetZ = level.random.nextGaussian() * 0.2;
            level.addParticle((ParticleOptions) type,
                    pos.getX() + 0.5 + offsetX,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5 + offsetZ,
                    0, 0.1, 0);
        }
    }

    private void strikeLightning(Level level, BlockPos pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        if (lightning != null) {
            lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(lightning);
        }
    }

    private void broadcastMessage(Level level, String message) {
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            Component msg = Component.literal(message);
            serverLevel.getPlayers(p -> true).forEach(player -> player.sendSystemMessage(msg));
        }
    }
}
