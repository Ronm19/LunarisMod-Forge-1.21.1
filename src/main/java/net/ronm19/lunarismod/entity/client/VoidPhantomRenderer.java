package net.ronm19.lunarismod.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VoidPhantomEntity;

public class VoidPhantomRenderer extends MobRenderer<VoidPhantomEntity, VoidPhantomModel<VoidPhantomEntity>> {

    public VoidPhantomRenderer( EntityRendererProvider.Context pContext ) {
        super(pContext, new VoidPhantomModel<>(pContext.bakeLayer(VoidPhantomModel.LAYER_LOCATION)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation( VoidPhantomEntity pEntity ) {
            return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/void_phantom/void_phantom.png");
        }
    }
