package net.ronm19.lunarismod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.List;

public class LunarHerobrineSwordItem extends SwordItem {
    public LunarHerobrineSwordItem(Tier tier, Item.Properties properties) {
        super(tier, properties.component(DataComponents.TOOL, createToolProperties()));
    }

    private static Tool createToolProperties() {
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F),
                        Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)
                ),
                1.0F,
                2
        );


    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level level = target.level();
        if (!level.isClientSide && attacker instanceof Player) {
            BlockPos basePos = target.blockPosition();

            // Trap the target
            for (int y = 0; y < 4; y++) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dz == 0) continue;
                        BlockPos cagePos = basePos.offset(dx, y, dz);
                        level.setBlock(cagePos, Blocks.OBSIDIAN.defaultBlockState(), 3);
                    }
                }
            }

            level.setBlock(basePos.below(), Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
            level.setBlock(basePos.above(4), Blocks.OBSIDIAN.defaultBlockState(), 3);
            for (int y = 0; y < 4; y++) {
                level.setBlock(basePos.above(y), Blocks.COBWEB.defaultBlockState(), 3);
            }

            level.playSound(null, basePos, SoundEvents.ZOMBIE_INFECT, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Teleport the attacker away (to nearby safe spot)
            BlockPos escapePos = basePos.offset(3, 0, 3); // offset diagonally
            if (level.isEmptyBlock(escapePos)) {
                attacker.teleportTo(escapePos.getX() + 0.5, escapePos.getY(), escapePos.getZ() + 0.5);
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID,
                                (double) ((float) attackDamage + tier.getAttackDamageBonus()),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID,
                                (double) attackSpeed,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }
}