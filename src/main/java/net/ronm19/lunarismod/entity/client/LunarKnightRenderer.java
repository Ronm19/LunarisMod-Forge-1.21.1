package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarKnightEntity;

public class LunarKnightRenderer extends MobRenderer<LunarKnightEntity, LunarKnightModel> {

    public LunarKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new LunarKnightModel(context.bakeLayer(LunarKnightModel.LAYER_LOCATION)), 0.5f);

        // Use HumanoidModel with LivingEntity because armor layers use those default models
        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()
        ));

        // Fix ItemInHandLayer to use correct generics and context
        this.addLayer(new ItemInHandLayer<>(
                this,
                context.getItemInHandRenderer()
        ));
    }

    @Override
    public ResourceLocation getTextureLocation(LunarKnightEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunar_knight/lunar_knight.png");
    }

    @Override
    public void render(LunarKnightEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}

