package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarSpearProjectileEntity;

public class LunarSpearProjectileModel extends EntityModel<LunarSpearProjectileEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunar_spear"), "main");
    private final ModelPart lunar_spear;

    public LunarSpearProjectileModel( ModelPart root) {
        this.lunar_spear = root.getChild("lunar_spear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition lunar_spear = partdefinition.addOrReplaceChild("lunar_spear", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, -22.0F, -1.0F, 1.0F, 22.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.5F, -23.0F, -1.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(5, 2).addBox(0.0F, -38.0F, -1.0F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim( LunarSpearProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        lunar_spear.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
