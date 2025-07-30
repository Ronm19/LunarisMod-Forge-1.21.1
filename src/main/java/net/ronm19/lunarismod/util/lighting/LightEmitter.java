package net.ronm19.lunarismod.util.lighting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LightEmitter {
    private static final Map<UUID, BlockPos> lightTrackers = new HashMap<>();

    public static void updateLight(Player player, LightLevel level) {
        if (player.level().isClientSide()) return;

        int brightness = level.getValue();
        if (brightness <= 0) return;

        BlockPos currentPos = player.blockPosition();

        // If player moved to a new block, refresh light
        BlockPos lastLightPos = lightTrackers.get(player.getUUID());
        if (lastLightPos == null || !lastLightPos.equals(currentPos)) {
            // Remove previous light
            if (lastLightPos != null && player.level().getBlockState(lastLightPos).is(Blocks.LIGHT)) {
                player.level().setBlockAndUpdate(lastLightPos, Blocks.AIR.defaultBlockState());
            }

            // Place new light at current position
            player.level().setBlockAndUpdate(currentPos, Blocks.LIGHT.defaultBlockState()
                    .setValue(BlockStateProperties.LEVEL, brightness));

            // Track new position
            lightTrackers.put(player.getUUID(), currentPos);
        }
    }

    public static void removeTrackedLight(Player player) {
        BlockPos pos = lightTrackers.remove(player.getUUID());
        if (pos != null && player.level().getBlockState(pos).is(Blocks.LIGHT)) {
            player.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }
}