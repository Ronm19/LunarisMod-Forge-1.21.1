package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.TamableAnimal;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VoidEyeEntity;

public class VoidEyeModel<V extends TamableAnimal> extends HierarchicalModel<VoidEyeEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "void_eye"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart halo;

    public VoidEyeModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.halo = root.getChild("halo");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -16.0F, -1.0F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition halo = partdefinition.addOrReplaceChild("halo", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -17.0F, 5.8F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-3.0F, -17.1F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.1F, 0.0F));

        PartDefinition halo_r1 = halo.addOrReplaceChild("halo_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-4.0F, -2.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -15.0F, 4.9F, 0.0F, -1.5708F, 0.0F));

        PartDefinition halo_r2 = halo.addOrReplaceChild("halo_r2", CubeListBuilder.create().texOffs(12, 18).addBox(-4.0F, -2.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -15.0F, 4.9F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim( VoidEyeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.ApplyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(VoidEyeAnimations.ANIM_VOID_EYE_WALKING, limbSwing, limbSwingAmount, 2f, 6.75f);
        this.animate(entity.idleAnimationState, VoidEyeAnimations.ANIM_VOID_EYE_IDLE, ageInTicks, 1f);

    }
    private void ApplyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45f);

        this.head.xRot = headYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        halo.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
