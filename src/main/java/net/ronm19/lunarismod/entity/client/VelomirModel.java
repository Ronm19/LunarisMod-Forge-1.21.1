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
import net.ronm19.lunarismod.entity.custom.VelomirEntity;

public class VelomirModel extends HierarchicalModel<VelomirEntity>  {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "velomir"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart front_left_leg;
    private final ModelPart front_right_leg;
    private final ModelPart back_left_leg;
    private final ModelPart back_right_leg;

    public VelomirModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.front_left_leg = root.getChild("front_left_leg");
        this.front_right_leg = root.getChild("front_right_leg");
        this.back_left_leg = root.getChild("back_left_leg");
        this.back_right_leg = root.getChild("back_right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(22, 44).addBox(-3.0F, -11.0F, -1.8F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(22, 32).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(32, 62).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(50, 62).addBox(-1.0F, -11.0F, 5.0F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 39).addBox(0.55F, -14.0F, 3.99F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 51).addBox(-2.55F, -14.0F, 3.99F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-2.05F, -6.0F, -2.0F, 4.0F, 12.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(48, 32).addBox(-2.0F, -11.0F, -4.0F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(0.0F, 2.1F, -9.4F, 0.2705F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -17.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 11.0F, 6.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(46, 44).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 11.0F));

        PartDefinition front_left_leg = partdefinition.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(0, 51).addBox(-3.0F, -1.0F, -1.9F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 14.0F, -9.0F));

        PartDefinition front_right_leg = partdefinition.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(16, 55).addBox(-1.0F, -1.0F, -1.9F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 14.0F, -9.0F));

        PartDefinition back_left_leg = partdefinition.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(60, 32).addBox(-3.0F, -1.0F, -1.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 14.0F, 8.0F));

        PartDefinition back_right_leg = partdefinition.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(60, 47).addBox(-1.0F, -1.0F, -1.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 14.0F, 8.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(VelomirEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        this.ApplyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(VelomirAnimations.ANIM_VELOMIR_WALKING, limbSwing, limbSwingAmount, 2f, 2f);
        this.animate(entity.idleAnimationState, VelomirAnimations.ANIM_VELOMIR_IDLE, ageInTicks, 1f);
    }
    private void ApplyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45f);

        this.head.xRot = headYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        front_left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        front_right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        back_left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        back_right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}

