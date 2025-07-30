package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarZombieKingEntity;

public class LunarZombieKingRenderer extends MobRenderer<LunarZombieKingEntity, LunarZombieKingModel> {

    public LunarZombieKingRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new LunarZombieKingModel(pContext.bakeLayer(LunarZombieKingModel.LAYER_LOCATION)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(LunarZombieKingEntity pEntity ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunar_zombie_king/lunar_zombie_king.png");
    }

    @Override
    public void render( LunarZombieKingEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight ) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
