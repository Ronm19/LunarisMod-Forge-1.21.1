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
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VoidPhantomEntity;

public class VoidPhantomModel<T extends VoidPhantomEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "void_phantom"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_wing;
    private final ModelPart left_wing_tip;
    private final ModelPart right_wing;
    private final ModelPart right_wing_tip;
    private final ModelPart tail;
    private final ModelPart tail2;

    public VoidPhantomModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.left_wing = root.getChild("left_wing");
        this.left_wing_tip = root.getChild("left_wing_tip");
        this.right_wing = root.getChild("right_wing");
        this.right_wing_tip = root.getChild("right_wing_tip");
        this.tail = root.getChild("tail");
        this.tail2 = root.getChild("tail2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(-1, 30).addBox(-3.0F, -2.0F, -8.0F, 5.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 19.0F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 31).addBox(-4.0F, -2.0F, -5.0F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 19.25F, -8.0F));

        PartDefinition left_wing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 20).addBox(0.0F, 0.0F, 0.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, 17.0F, -8.0F));

        PartDefinition left_wing_tip = partdefinition.addOrReplaceChild("left_wing_tip", CubeListBuilder.create().texOffs(1, 0).addBox(1.0F, 0.0F, 0.0F, 12.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, 17.0F, -8.0F));

        PartDefinition right_wing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(28, 20).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 17.0F, -8.0F));

        PartDefinition right_wing_tip = partdefinition.addOrReplaceChild("right_wing_tip", CubeListBuilder.create().texOffs(0, 10).addBox(-13.0F, 0.0F, 0.0F, 12.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5F, 17.0F, -8.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(29, 40).addBox(-2.0F, 0.0F, 1.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 17.0F, 1.0F));

        PartDefinition tail2 = partdefinition.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offset(0.5F, 17.5F, 7.0F));

        PartDefinition tail2_r1 = tail2.addOrReplaceChild("tail2_r1", CubeListBuilder.create().texOffs(0, 43).addBox(-0.5F, -6.5F, 7.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 4.9F, -8.3F, -0.2182F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(VoidPhantomEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.ApplyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(VoidPhantomAnimations.ANIM_VOID_PHANTOM_FLYING, limbSwing, limbSwingAmount, 2f, 2f);
        this.animate(entity.idleAnimationState, VoidPhantomAnimations.ANIM_VOID_PHANTOM_IDLE, ageInTicks, 1f);
    }

    private void ApplyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45f);

        this.head.xRot = headYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_wing_tip.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_wing.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_wing_tip.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        tail2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}