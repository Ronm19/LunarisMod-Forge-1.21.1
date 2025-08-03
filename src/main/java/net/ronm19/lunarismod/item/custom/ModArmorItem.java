package net.ronm19.lunarismod.item.custom;

import com.google.common.collect.ImmutableMap;
import net.ronm19.lunarismod.item.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class ModArmorItem extends ArmorItem {

    private static final Map<Holder<ArmorMaterial>, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            new ImmutableMap.Builder<Holder<ArmorMaterial>, List<MobEffectInstance>>()
                    .put(ModArmorMaterials.NOCTRIUM_ARMOR_MATERIAL, List.of(
                            new MobEffectInstance(MobEffects.JUMP, 200, 2, false, false),
                            new MobEffectInstance(MobEffects.GLOWING, 200, 1, false, false),
                            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2, false, false),
                            new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 2, false, false),
                            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2, false, false),
                            new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 2, false, false),
                            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 2, false, false),
                            new MobEffectInstance(MobEffects.ABSORPTION, 200, 2, false, false)
                    ))
                    .build();


    private static final Map<Holder<ArmorMaterial>, List<MobEffectInstance>> MOON_MATERIAL_TO_EFFECT_MAP =
            new ImmutableMap.Builder<Holder<ArmorMaterial>, List<MobEffectInstance>>()
                    .put(ModArmorMaterials.MOON_ARMOR_MATERIAL, List.of(
                            new MobEffectInstance(MobEffects.ABSORPTION, 200, 1),
                            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1),
                            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2),
                            new MobEffectInstance(MobEffects.REGENERATION, 200, 2),
                            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 2)
                    ))
                    .build();




    public ModArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if (level.isClientSide) return;
        evaluateArmorEffects(player);
    }

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<Holder<ArmorMaterial>, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            if (hasPlayerCorrectArmorOn(entry.getKey(), player)) {
                applyEffects(player, entry.getValue());
            }
        }

        for (Map.Entry<Holder<ArmorMaterial>, List<MobEffectInstance>> entry : MOON_MATERIAL_TO_EFFECT_MAP.entrySet()) {
            if (hasPlayerCorrectArmorOn(entry.getKey(), player)) {
                applyEffects(player, entry.getValue());
            }
        }
    }

    private void applyEffects(Player player, List<MobEffectInstance> effects) {
        for (MobEffectInstance effect : effects) {
            if (!player.hasEffect(effect.getEffect())) {
                player.addEffect(new MobEffectInstance(
                        effect.getEffect(),
                        effect.getDuration(),
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible()
                ));
            }
        }
    }

    private boolean hasPlayerCorrectArmorOn(Holder<ArmorMaterial> material, Player player) {
        for (ItemStack stack : player.getArmorSlots()) {
            if (!(stack.getItem() instanceof ArmorItem armor)) return false;
            if (armor.getMaterial() != material) return false;
        }
        return true;
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        for (ItemStack armor : player.getArmorSlots()) {
            if (armor.isEmpty()) return false;
        }
        return true;
    }
}