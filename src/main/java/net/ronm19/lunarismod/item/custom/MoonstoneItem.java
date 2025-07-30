package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.ritual.PackRitualHandler;

public class MoonstoneItem extends Item {

    public MoonstoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide && context.getPlayer() instanceof ServerPlayer serverPlayer) {
            // Check for nearby Lunar Wolf
            LunarWolfEntity nearestWolf = PackRitualHandler.getNearbyWolf(level, pos, 5);
            if (nearestWolf != null) {
                boolean success = PackRitualHandler.performRitual(serverPlayer, pos);
                return success ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            } else {
                serverPlayer.displayClientMessage(Component.literal("No Lunar Wolf nearby for the ritual."), true);
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }
}
