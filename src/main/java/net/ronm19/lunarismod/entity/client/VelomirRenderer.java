package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VelomirEntity;

public class VelomirRenderer extends MobRenderer<VelomirEntity, VelomirModel> {

    public VelomirRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new VelomirModel(pContext.bakeLayer(VelomirModel.LAYER_LOCATION)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation( VelomirEntity pEntity ) {
        if(pEntity.isSaddled()) {
            return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/velomir/velomir_saddled.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/velomir/velomir.png");
        }
    }

    @Override
    public void render( VelomirEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                        MultiBufferSource pBuffer, int pPackedLight ) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(1.2F, 1.2F, 1.2F);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
