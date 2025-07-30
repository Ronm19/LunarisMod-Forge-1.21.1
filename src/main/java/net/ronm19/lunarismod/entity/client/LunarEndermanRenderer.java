package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarEndermanEntity;

public class LunarEndermanRenderer extends MobRenderer<LunarEndermanEntity, LunarEndermanModel> {

    public LunarEndermanRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new LunarEndermanModel(pContext.bakeLayer(LunarEndermanModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation( LunarEndermanEntity pEntity ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunar_enderman/lunar_enderman.png");
    }

    @Override
    public void render( LunarEndermanEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight ) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
