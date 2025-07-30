package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarZombieEntity;

public class LunarZombieRenderer extends MobRenderer<LunarZombieEntity, LunarZombieModel> {

    public LunarZombieRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new LunarZombieModel(pContext.bakeLayer(LunarZombieModel.LAYER_LOCATION)), 0.6f);

    }

    @Override
    public ResourceLocation getTextureLocation( LunarZombieEntity pEntity ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunar_zombie/lunar_zombie.png");
    }

    @Override
    public void render( LunarZombieEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight ) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
