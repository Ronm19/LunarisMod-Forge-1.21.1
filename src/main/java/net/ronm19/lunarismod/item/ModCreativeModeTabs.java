package net.ronm19.lunarismod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.block.ModBlocks;

public class ModCreativeModeTabs {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LunarisMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> NOCTRIUM_ITEMS_TAB = CREATIVE_MODE_TABS.register("noctrium_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.NOCTRIUMGEM.get()))
                    .title(Component.translatable("creativetab.lunarismod.noctrium_items"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.NOCTRIUMGEM.get());
                        pOutput.accept(ModItems.RAW_NOCTRIUM_GEM.get());

                        pOutput.accept(ModItems.NOCTRIUM_BONE.get());

                        pOutput.accept(ModItems.MOON_FRUIT_STEW.get());

                        pOutput.accept(ModItems.NOCTRIUM_SWORD.get());
                        pOutput.accept(ModItems.NOCTRIUM_PICKAXE.get());
                        pOutput.accept(ModItems.NOCTRIUM_SHOVEL.get());
                        pOutput.accept(ModItems.NOCTRIUM_AXE.get());
                        pOutput.accept(ModItems.NOCTRIUM_HOE.get());

                        pOutput.accept(ModItems.NOCTRIUM_HAMMER.get());
                        pOutput.accept(ModItems.NOCTRIUM_TOMAHAWK.get());

                        pOutput.accept(ModItems.NOCTRIUM_HELMET.get());
                        pOutput.accept(ModItems.NOCTRIUM_CHESTPLATE.get());
                        pOutput.accept(ModItems.NOCTRIUM_LEGGINGS.get());
                        pOutput.accept(ModItems.NOCTRIUM_BOOTS.get());

                        pOutput.accept(ModItems.LUNARWOLF_SPAWN_EGG.get());
                        pOutput.accept(ModItems.VOIDHOWLER_SPAWN_EGG.get());




                    }).build());

  public static final RegistryObject<CreativeModeTab> NOCTRIUM_BLOCKS_TAB = CREATIVE_MODE_TABS.register("noctrium_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.NOCTRIUM_BLOCK.get()))
                    .withTabsBefore(NOCTRIUM_ITEMS_TAB.getId())
                    .title(Component.translatable("creativetab.lunarismod.noctrium_blocks"))
                    .displayItems((pParameters, pOutput) -> {
                       pOutput.accept(ModBlocks.NOCTRIUM_BLOCK.get());
                       pOutput.accept(ModBlocks.RAW_NOCTRIUM_BLOCK.get());
                       pOutput.accept(ModBlocks.NOCTRIUM_ORE.get());
                       pOutput.accept(ModBlocks.NOCTRIUM_DEEPSLATE_ORE.get());

                        pOutput.accept(ModBlocks.NOCTRIUM_STAIRS.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_SLAB.get());

                        pOutput.accept(ModBlocks.NOCTRIUM_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_BUTTON.get());

                        pOutput.accept(ModBlocks.NOCTRIUM_FENCE.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_FENCE_GATE.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_WALL.get());

                        pOutput.accept(ModBlocks.NOCTRIUM_DOOR.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_TRAPDOOR.get());

                        pOutput.accept(ModBlocks.NOCTRIUM_LEAVES.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_LOG.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_PLANKS.get());
                        pOutput.accept(ModBlocks.STRIPPED_NOCTRIUM_LOG.get());
                        pOutput.accept(ModBlocks.STRIPPED_NOCTRIUM_WOOD.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_WOOD.get());
                        pOutput.accept(ModBlocks.NOCTRIUM_SAPLING.get());



                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
