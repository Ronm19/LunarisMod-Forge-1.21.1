package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarHerobrineEntity;

public class LunarHerobrineRenderer extends MobRenderer<LunarHerobrineEntity, LunarHerobrineModel> {

    public LunarHerobrineRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new LunarHerobrineModel(pContext.bakeLayer(LunarHerobrineModel.LAYER_LOCATION)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation( LunarHerobrineEntity pEntity ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunar_herobrine/lunar_herobrine.png");
    }

    @Override
    public void render( LunarHerobrineEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight ) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
