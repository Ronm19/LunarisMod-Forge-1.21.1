package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VoidHowlerEntity;

public class VoidHowlerRenderer extends MobRenderer<VoidHowlerEntity, VoidHowlerModel<VoidHowlerEntity>> {
    public VoidHowlerRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new VoidHowlerModel<>(pContext.bakeLayer(VoidHowlerModel.LAYER_LOCATION)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation( VoidHowlerEntity pEntity ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/voidhowler/voidhowler.png");
    }

    @Override
    public void render( VoidHowlerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                        MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(1.2F, 1.2F, 1.2F);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
