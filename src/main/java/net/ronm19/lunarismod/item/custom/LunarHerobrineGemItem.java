package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.ai.CommandMode;
import net.ronm19.lunarismod.entity.custom.LunarKnightEntity;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LunarHerobrineGemItem extends Item {

    private static final Map<UUID, Long> lastCommandTime = new HashMap<>();
    private static final long COMMAND_COOLDOWN_MS = 1000;  // 1 second cooldown

    public LunarHerobrineGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (player == null || level.isClientSide) return InteractionResult.PASS;

        // Apply cooldown (20 ticks = 1 second)
        player.getCooldowns().addCooldown(this, 20);

        long currentTime = System.currentTimeMillis();
        long lastTime = lastCommandTime.getOrDefault(player.getUUID(), 0L);
        if (currentTime - lastTime < COMMAND_COOLDOWN_MS) {
            return InteractionResult.PASS;  // Prevent rapid reuse
        }
        lastCommandTime.put(player.getUUID(), currentTime);

        // Find all tamed Lunar Knights owned by player within 150 blocks
        List<LunarKnightEntity> knights = level.getEntitiesOfClass(LunarKnightEntity.class,
                player.getBoundingBox().inflate(150.0D),
                knight -> knight.isTame() && knight.isOwnedBy(player));

        if (knights.isEmpty()) return InteractionResult.PASS;

        // Determine next command mode based on first knight's current mode
        CommandMode nextMode = knights.get(0).getCommandMode().next();

        // Update all knights with new command mode and refresh their AI goals
        for (LunarKnightEntity knight : knights) {
            knight.setCommandMode(nextMode);
            knight.updateCommandGoals();
        }

        // Feedback to player: message + sound + particles
        player.displayClientMessage(Component.literal("Command set to: " + nextMode.name()), true);

        float pitch;
        ParticleOptions particle;
        int particleCount;

        switch (nextMode) {
            case FOLLOW -> {
                pitch = 1.3F;
                particle = ParticleTypes.HAPPY_VILLAGER;
                particleCount = 10;
            }
            case HOLD -> {
                pitch = 0.8F;
                particle = ParticleTypes.CRIT;
                particleCount = 8;
            }
            case PATROL -> {
                pitch = 1.0F;
                particle = ParticleTypes.SOUL_FIRE_FLAME;
                particleCount = 12;
            }
            default -> {
                pitch = 1.0F;
                particle = ParticleTypes.CLOUD;
                particleCount = 6;
            }
        }

        level.playSound(null, player.blockPosition(), SoundEvents.RAID_HORN.get(), SoundSource.PLAYERS, 1.0F, pitch);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(particle,
                    player.getX(), player.getEyeY(), player.getZ(),
                    particleCount, 0.4D, 0.5D, 0.4D, 0.02D);
        }

        return InteractionResult.SUCCESS;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click to change Lunar Knight commands"));
    }
}
