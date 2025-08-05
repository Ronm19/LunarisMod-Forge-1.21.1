package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VoidWardenEntity;

public class VoidWardenModel extends HierarchicalModel<VoidWardenEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "void_warden"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart arm1;
    private final ModelPart arm2;
    private final ModelPart base;
    private final ModelPart middle_spike;
    private final ModelPart body2;
    private final ModelPart leg1;
    private final ModelPart leg2;

    public VoidWardenModel( ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.arm1 = root.getChild("arm1");
        this.arm2 = root.getChild("arm2");
        this.base = this.arm2.getChild("base");
        this.middle_spike = this.arm2.getChild("middle_spike");
        this.body2 = this.arm2.getChild("body2");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(6, 6).addBox(4.0F, -38.0F, -2.4F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 6).addBox(-5.8F, -38.0F, -2.4F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -31.0F, -5.3F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -24.0F, -4.4F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition arm1 = partdefinition.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(28, 16).addBox(3.0F, -22.2F, -2.5F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition arm2 = partdefinition.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(0, 34).addBox(-5.2F, -22.2F, -2.5F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition base = arm2.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2161F, -3.8586F, -15.3F, 1.5708F, 0.0F, -0.0873F));

        PartDefinition base_r1 = base.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(36, 20).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.4661F, 0.0F));

        PartDefinition middle_spike = arm2.addOrReplaceChild("middle_spike", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2161F, -3.8586F, -19.3F, 1.5708F, 0.0F, -0.0873F));

        PartDefinition middle_spike_r1 = middle_spike.addOrReplaceChild("middle_spike_r1", CubeListBuilder.create().texOffs(16, 34).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.4661F, 0.0F));

        PartDefinition body2 = arm2.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2161F, -3.8586F, -2.3F, 1.5708F, 0.0F, -0.0873F));

        PartDefinition body_r1 = body2.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(24, 16).addBox(-0.5F, -12.5F, -0.5F, 1.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.4661F, 0.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(32, 0).addBox(-2.7F, -12.1F, -2.4F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(8, 34).addBox(1.0F, -12.1F, -2.4F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim( VoidWardenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(VoidWardenAnimations.ANIM_VOID_WARDEN_WALKING, limbSwing, limbSwingAmount, 1f, 2f);
        this.animate(entity.idleAnimationState, VoidWardenAnimations.ANIM_VOID_WARDEN_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        arm1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        arm2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
