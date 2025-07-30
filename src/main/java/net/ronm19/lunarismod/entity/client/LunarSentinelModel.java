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
import net.ronm19.lunarismod.entity.custom.LunarSentinelEntity;

public class LunarSentinelModel extends HierarchicalModel<LunarSentinelEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunarsentinel"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart arm1;
    private final ModelPart arm2;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tail;
    private final ModelPart lunaredge;

    public LunarSentinelModel( ModelPart root ) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.arm1 = root.getChild("arm1");
        this.arm2 = root.getChild("arm2");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");
        this.lunaredge = root.getChild("lunaredge");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(59, 3).addBox(-4.0F, -4.0F, -3.5F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.0F, -1.5F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(66, 73).addBox(-3.0F, -8.25F, -7.25F, 6.0F, 13.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(56, 26).addBox(-3.0F, -1.25F, -2.25F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 11.25F, 3.25F));

        PartDefinition arm1 = partdefinition.addOrReplaceChild("arm1", CubeListBuilder.create(), PartPose.offset(6.1129F, 7.3476F, -1.4F));

        PartDefinition arm1_r1 = arm1.addOrReplaceChild("arm1_r1", CubeListBuilder.create().texOffs(28, 51).addBox(0.0F, -5.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 3.1078F, 0.0F, 0.0F, 0.0F, -0.2443F));

        PartDefinition arm1_r2 = arm1.addOrReplaceChild("arm1_r2", CubeListBuilder.create().texOffs(52, 17).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4784F, -1.8956F, 0.0F, 0.0F, 0.0F, -0.2443F));

        PartDefinition arm2 = partdefinition.addOrReplaceChild("arm2", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.1173F, 7.5806F, -1.4F, -0.3927F, 0.0F, 0.0F));

        PartDefinition arm2_r1 = arm2.addOrReplaceChild("arm2_r1", CubeListBuilder.create().texOffs(36, 51).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5297F, 2.1342F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition arm2_r2 = arm2.addOrReplaceChild("arm2_r2", CubeListBuilder.create().texOffs(64, 17).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5297F, -2.1342F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(28, 62).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 20.0F, -3.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(36, 62).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 20.0F, -3.0F));

        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(44, 63).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 20.0F, 14.0F));

        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(52, 63).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 20.0F, 14.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 23).addBox(-1.0F, -4.225F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 63).addBox(-1.0F, -3.425F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 68).addBox(-1.0F, -0.425F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 63).addBox(-1.0F, 2.575F, 0.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 14.225F, 18.0F));

        PartDefinition lunaredge = partdefinition.addOrReplaceChild("lunaredge", CubeListBuilder.create(), PartPose.offset(-5.6F, 11.1845F, -5.2492F));

        PartDefinition lunaredge_r1 = lunaredge.addOrReplaceChild("lunaredge_r1", CubeListBuilder.create().texOffs(42, 59).addBox(0.0F, -3.5F, -4.0F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.7845F, -4.0508F, -0.6196F, 0.0F, 0.0F));

        PartDefinition lunaredge_r2 = lunaredge.addOrReplaceChild("lunaredge_r2", CubeListBuilder.create().texOffs(41, 58).addBox(0.0F, -6.5F, -4.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -2.0845F, -4.0508F, -0.6196F, 0.0F, 0.0F));

        PartDefinition lunaredge_r3 = lunaredge.addOrReplaceChild("lunaredge_r3", CubeListBuilder.create().texOffs(107, 35).addBox(1.0F, -4.0F, -12.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6F, -9.2845F, -19.5508F, -0.6108F, 0.005F, 0.0071F));

        PartDefinition lunaredge_r4 = lunaredge.addOrReplaceChild("lunaredge_r4", CubeListBuilder.create().texOffs(68, 12).addBox(1.0F, -4.0F, -12.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6F, -10.4845F, -18.7508F, -0.6108F, 0.005F, 0.0071F));

        PartDefinition lunaredge_r5 = lunaredge.addOrReplaceChild("lunaredge_r5", CubeListBuilder.create().texOffs(66, 10).addBox(1.0F, -4.0F, -12.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6F, -8.0845F, -20.2508F, -0.6108F, 0.005F, 0.0071F));

        PartDefinition lunaredge_r6 = lunaredge.addOrReplaceChild("lunaredge_r6", CubeListBuilder.create().texOffs(46, 31).addBox(1.0F, -4.0F, -16.0F, 1.0F, 3.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6F, -6.0845F, -13.2508F, -0.6108F, 0.005F, 0.0071F));

        PartDefinition lunaredge_r7 = lunaredge.addOrReplaceChild("lunaredge_r7", CubeListBuilder.create().texOffs(15, 52).addBox(0.0F, -4.0F, -16.0F, 0.0F, 3.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, -6.0845F, -13.2508F, -0.6108F, 0.005F, 0.0071F));

        PartDefinition lunaredge_r8 = lunaredge.addOrReplaceChild("lunaredge_r8", CubeListBuilder.create().texOffs(30, 72).addBox(0.0F, -2.0F, -8.0F, 1.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.5155F, -0.4508F, -0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim( LunarSentinelEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch ) {
            this.root.getAllParts().forEach(ModelPart::resetPose);
            this.ApplyHeadRotation(netHeadYaw, headPitch);

            this.animateWalk(LunarSentinelAnimations.ANIM_LUNARSENTINEL_WALKING, limbSwing, limbSwingAmount, 2f, 1.5f);
            this.animate(entity.idleAnimationState, LunarSentinelAnimations.ANIM_LUNARSENTINEL_IDLE, ageInTicks, 1f);
        this.animate(entity.attackAnimationState, LunarSentinelAnimations.ANIM_LUNARSENTINEL_ATTACKING, ageInTicks, 1f);
    }

    private void ApplyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45f);

        this.head.xRot = headYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color ) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        arm1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        arm2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        lunaredge.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
