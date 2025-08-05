package net.ronm19.lunarismod.item;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.item.custom.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LunarisMod.MOD_ID);

    public static final RegistryObject<Item> NOCTRIUMGEM = ITEMS.register("noctriumgem",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_NOCTRIUM_GEM = ITEMS.register("raw_noctrium_gem",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOONSTONE = ITEMS.register("moonstone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUNAR_HEROBRINE_GEM = ITEMS.register("lunar_herobrine_gem",
            () -> new LunarHerobrineGemItem(new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> MOON_FRUIT_STEW = ITEMS.register("moon_fruit_stew",
            () -> new Item(new Item.Properties().food(ModFoodProperties.MOON_FRUIT_STEW)));
    public static final RegistryObject<Item> MOONPPLE = ITEMS.register("moonpple",
            () -> new Item(new Item.Properties().food(ModFoodProperties.MOONPPLE)));

    public static final RegistryObject<Item> NOCTRIUM_SWORD = ITEMS.register("noctrium_sword",
            () -> new SwordItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(SwordItem.createAttributes(ModToolTiers.NOCTRIUM, 7, -2.2f))));

    public static final RegistryObject<Item> LUNAR_HEROBRINE_SWORD = ITEMS.register("lunar_herobrine_sword",
            () -> new LunarHerobrineSwordItem(ModToolTiers.LUNAR_HEROBRINE, new Item.Properties().fireResistant().rarity(Rarity.EPIC)
                    .attributes(SwordItem.createAttributes(ModToolTiers.LUNAR_HEROBRINE, 14, -2.1f))));

    public static final RegistryObject<Item> NOCTRIUM_PICKAXE = ITEMS.register("noctrium_pickaxe",
            () -> new PickaxeItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.NOCTRIUM, 1, -2.6f))));

    public static final RegistryObject<Item> NOCTRIUM_SHOVEL = ITEMS.register("noctrium_shovel",
            () -> new ShovelItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(ShovelItem.createAttributes(ModToolTiers.NOCTRIUM, 1.5f, -2.6f))));

    public static final RegistryObject<Item> NOCTRIUM_AXE = ITEMS.register("noctrium_axe",
            () -> new AxeItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(AxeItem.createAttributes(ModToolTiers.NOCTRIUM, 6, -2.9f))));

    public static final RegistryObject<Item> NOCTRIUM_HOE = ITEMS.register("noctrium_hoe",
            () -> new HoeItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(HoeItem.createAttributes(ModToolTiers.NOCTRIUM, 0, -2.8f))));

    public static final RegistryObject<Item> NOCTRIUM_HAMMER = ITEMS.register("noctrium_hammer",
            () -> new HammerItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(PickaxeItem.createAttributes(ModToolTiers.NOCTRIUM, 17, -2.9f))));

    public static final RegistryObject<Item> LUNAR_EDGE = ITEMS.register("lunar_edge",
            () -> new SwordItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant().rarity(Rarity.RARE)
                    .attributes(SwordItem.createAttributes(ModToolTiers.NOCTRIUM, 21, -2.1f))));

    public static final RegistryObject<Item> NOCTRIUM_TOMAHAWK = ITEMS.register("noctrium_tomahawk",
            () -> new NoctriumTomahawkItem(new Item.Properties().stacksTo(16)));



    public static final RegistryObject<Item> LUNAR_SPEAR = ITEMS.register("lunar_spear",
            () -> new LunarSpearItem(new Item.Properties().fireResistant().rarity(Rarity.COMMON)
                    .attributes(LunarSpearItem.createAttributes())));

    public static final RegistryObject<Item> SOUL_TOME = ITEMS.register("soul_tome",
            () -> new SoulTomeItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));


    public static final RegistryObject<Item> VOID_PULSE = ITEMS.register("void_pulse",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE).fireResistant()));

    public static final RegistryObject<Item> NOCTRIUM_HELMET = ITEMS.register("noctrium_helmet",
            () -> new ModArmorItem(ModArmorMaterials.NOCTRIUM_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final RegistryObject<Item> NOCTRIUM_CHESTPLATE = ITEMS.register("noctrium_chestplate",
            () -> new ArmorItem(ModArmorMaterials.NOCTRIUM_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final RegistryObject<Item> NOCTRIUM_LEGGINGS = ITEMS.register("noctrium_leggings",
            () -> new ArmorItem(ModArmorMaterials.NOCTRIUM_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final RegistryObject<Item> NOCTRIUM_BOOTS = ITEMS.register("noctrium_boots",
            () -> new ArmorItem(ModArmorMaterials.NOCTRIUM_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(40))));

    public static final RegistryObject<Item> MOON_HELMET = ITEMS.register("moon_helmet",
            () -> new ModArmorItem(ModArmorMaterials.MOON_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(50))));
    public static final RegistryObject<Item> MOON_CHESTPLATE = ITEMS.register("moon_chestplate",
            () -> new ArmorItem(ModArmorMaterials.MOON_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(50))));
    public static final RegistryObject<Item> MOON_LEGGINGS = ITEMS.register("moon_leggings",
            () -> new ArmorItem(ModArmorMaterials.MOON_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(50))));
    public static final RegistryObject<Item> MOON_BOOTS = ITEMS.register("moon_boots",
            () -> new ArmorItem(ModArmorMaterials.MOON_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(50))));

    public static final RegistryObject<Item> NOCTRIUM_BONE = ITEMS.register("noctrium_bone",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> LUNARWOLF_SPAWN_EGG = ITEMS.register("lunarwolf_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNARWOLF, 0x2B0F3F, 0x7C72A8, new Item.Properties()));

    public static final RegistryObject<Item> VOIDHOWLER_SPAWN_EGG = ITEMS.register("voidhowler_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VOIDHOWLER, 0x1A092B, 0xA47CC3, new Item.Properties()));

    public static final RegistryObject<Item> LUNARSENTINEL_SPAWN_EGG = ITEMS.register("lunarsentinel_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNARSENTINEL, 0x1C1E26, 0x7AD7F0, new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_CREEPER_SPAWN_EGG = ITEMS.register("lunar_creeper_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAR_CREEPER, 0x2A2A40, 0xB3E5FC, new Item.Properties()));

    public static final RegistryObject<Item> VOID_PHANTOM_SPAWN_EGG = ITEMS.register("void_phantom_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VOID_PHANTOM, 0x0B0C1A, 0x6A00FF, new Item.Properties()));

    public static final RegistryObject<Item> VOID_EYE_SPAWN_EGG = ITEMS.register("void_eye_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VOID_EYE, 0xFF281C4E,0xFFFFDFA6, new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_ENDERMAN_SPAWN_EGG = ITEMS.register("lunar_enderman_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAR_ENDERMAN, 0x1A1A2E,0x6C63FF, new Item.Properties()));

    public static final RegistryObject<Item> VELOMIR_SPAWN_EGG = ITEMS.register("velomir_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VELOMIR, 0x2B2D42, 0x8D99AE, new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_ZOMBIE_SPAWN_EGG = ITEMS.register("lunar_zombie_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAR_ZOMBIE,  0x2B0F4F, 0x6E39B9, new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_ZOMBIE_KING_SPAWN_EGG = ITEMS.register("lunar_zombie_king_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAR_ZOMBIE_KING,  0x2A2A44, 0x84E1D5, new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_HEROBRINE_SPAWN_EGG = ITEMS.register("lunar_herobrine_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAR_HEROBRINE, 0x1C1C24,  0xC9D6FF, new Item.Properties()));

    public static final RegistryObject<Item> LUNAREON_SPAWN_EGG = ITEMS.register("lunareon_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAREON, 0x1B1E2A,  0x3D4D74, new Item.Properties()));

    public static final RegistryObject<Item> LUNAR_KNIGHT_SPAWN_EGG = ITEMS.register("lunar_knight_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNAR_KNIGHT, 0x1E1B2F,  0x7DF9FF, new Item.Properties()));

    public static final RegistryObject<Item> VOID_WARDEN_SPAWN_EGG = ITEMS.register("void_warden_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VOID_WARDEN, 0x1A1A1A,  0xB0F0D0, new Item.Properties()));





    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
