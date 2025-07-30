package net.ronm19.lunarismod.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ItemUtils {

    /**
     * Checks if a given ItemStack has a positive Attack Damage modifier in MAINHAND.
     */
    public static boolean canUseItemToAttack(ItemStack stack) {
        if (stack.isEmpty()) return false;

        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        return hasAttackDamageModifier(modifiers, EquipmentSlot.MAINHAND);
    }

    /**
     * Checks if a given Item has a positive Attack Damage modifier in MAINHAND.
     */
    public static boolean canUseItemToAttack(Item item) {
        ItemAttributeModifiers modifiers = item.getDefaultAttributeModifiers();
        return hasAttackDamageModifier(modifiers, EquipmentSlot.MAINHAND);
    }

    /**
     * Evaluates if the attribute modifiers contain positive Attack Damage for a specific slot.
     */
    private static boolean hasAttackDamageModifier(ItemAttributeModifiers modifiers, EquipmentSlot slot) {
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Attribute attribute = (Attribute) entry.attribute();
            AttributeModifier modifier = entry.modifier();
            EquipmentSlotGroup group = entry.slot();

            // Check if the slot group includes the slot
            if (attribute == Attributes.ATTACK_DAMAGE && group.test(slot)) {
                if (modifier.amount() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
