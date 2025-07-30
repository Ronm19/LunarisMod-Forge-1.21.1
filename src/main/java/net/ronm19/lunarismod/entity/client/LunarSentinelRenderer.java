package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarSentinelEntity;

public class LunarSentinelRenderer extends MobRenderer<LunarSentinelEntity, LunarSentinelModel> {

    public LunarSentinelRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new LunarSentinelModel(pContext.bakeLayer(LunarSentinelModel.LAYER_LOCATION)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation( LunarSentinelEntity lunarSentinelEntity  ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunarsentinel/lunar_sentinel.png");
    }

    @Override
    public void render( LunarSentinelEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight ) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
