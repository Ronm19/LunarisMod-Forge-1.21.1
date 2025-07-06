package net.ronm19.lunarismod.item;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.item.custom.HammerItem;
import net.ronm19.lunarismod.item.custom.ModArmorItem;
import net.ronm19.lunarismod.item.custom.NoctriumTomahawkItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LunarisMod.MOD_ID);

    public static final RegistryObject<Item> NOCTRIUMGEM = ITEMS.register("noctriumgem",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_NOCTRIUM_GEM = ITEMS.register("raw_noctrium_gem",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MOON_FRUIT_STEW = ITEMS.register("moon_fruit_stew",
            () -> new Item(new Item.Properties().food(ModFoodProperties.MOON_FRUIT_STEW)));

    public static final RegistryObject<Item> NOCTRIUM_SWORD = ITEMS.register("noctrium_sword",
            () -> new SwordItem(ModToolTiers.NOCTRIUM, new Item.Properties().fireResistant()
                    .attributes(SwordItem.createAttributes(ModToolTiers.NOCTRIUM, 7, -2.2f))));

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

    public static final RegistryObject<Item> NOCTRIUM_TOMAHAWK = ITEMS.register("noctrium_tomahawk",
            () -> new NoctriumTomahawkItem(new Item.Properties().stacksTo(16)));

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

    public static final RegistryObject<Item> LUNARWOLF_SPAWN_EGG = ITEMS.register("lunarwolf_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LUNARWOLF, 0x2B0F3F, 0x7C72A8, new Item.Properties()));
    public static final RegistryObject<Item> VOIDHOWLER_SPAWN_EGG = ITEMS.register("voidhowler_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.VOIDHOWLER, 0x1A092B, 0xA47CC3, new Item.Properties()));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
