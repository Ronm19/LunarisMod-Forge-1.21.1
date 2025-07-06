package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

public class LunarWolfRenderer extends MobRenderer<LunarWolfEntity, LunarWolfModel<LunarWolfEntity>> {
    public LunarWolfRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new LunarWolfModel<>(pContext.bakeLayer(LunarWolfModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(LunarWolfEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunarwolf/lunarwolf.png");
    }

    @Override
    public void render(LunarWolfEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(1.2F, 1.2F, 1.2F);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        int glowColor = pEntity.getEyeGlowColor();

        // Extract RGB components
        float red = ((glowColor >> 16) & 0xFF) / 255f;
        float green = ((glowColor >> 8) & 0xFF) / 255f;
        float blue = (glowColor & 0xFF) / 255f;

    }


}
