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
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;

public class LunarWolfModel<T extends LunarWolfEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunarwolf"), "main");

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart mane;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tail;

    public LunarWolfModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.mane = root.getChild("mane");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(30, 10).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F)
                        .texOffs(32, 29).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F)
                        .texOffs(24, 13).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F)
                        .texOffs(24, 23).addBox(-0.5F, -0.02F, -5.0F, 3.0F, 3.0F, 3.0F),
                PartPose.offset(-1.0F, 13.5F, -7.0F));

        PartDefinition mane = partdefinition.addOrReplaceChild("mane",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F),
                PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("leg1",
                CubeListBuilder.create().texOffs(12, 18)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(-2.5F, 16.0F, 7.0F));

        partdefinition.addOrReplaceChild("leg2",
                CubeListBuilder.create().texOffs(12, 17)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(0.5F, 16.0F, 7.0F));

        partdefinition.addOrReplaceChild("leg3",
                CubeListBuilder.create().texOffs(12, 18)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(-2.5F, 16.0F, -4.0F));

        partdefinition.addOrReplaceChild("leg4",
                CubeListBuilder.create().texOffs(12, 18)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(0.5F, 16.0F, -4.0F));

        partdefinition.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(30, 0)
                        .addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F),
                PartPose.offset(-1.0F, 12.0F, 10.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LunarWolfEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        applyHeadRotation(netHeadYaw, headPitch);

        // Animation triggers
        this.animateWalk(LunarWolfAnimations.ANIM_LUNARWOLF_WALKING, limbSwing, limbSwingAmount, 0.8f, 2.04167f);
        this.animate(entity.idleAnimationState, LunarWolfAnimations.ANIM_LUNARWOLF_IDLE, ageInTicks, 1f);

    }

    private void applyHeadRotation(float netHeadYaw, float headPitch) {
        netHeadYaw = Mth.clamp(netHeadYaw, -30.0F, 30.0F);
        headPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}