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
import net.ronm19.lunarismod.entity.custom.LunarCreeperEntity;

public class LunarCreeperModel extends HierarchicalModel<LunarCreeperEntity> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunarcreeper"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public LunarCreeperModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(33, 12).addBox(2.0F, -30.0F, 1.1F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(39, 12).addBox(3.0F, -32.0F, 1.1F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(42, 12).addBox(4.0F, -33.0F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(42, 14).addBox(4.0F, -32.1F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 41).addBox(5.0F, -33.0F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(35, 41).addBox(4.0F, -33.9F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(39, 15).addBox(5.0F, -33.9F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(20, 41).addBox(-0.5F, -29.0F, 1.1F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(26, 41).addBox(-3.0F, -29.0F, 1.1F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(38, 41).addBox(-3.9F, -30.0F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(20, 38).addBox(-4.7F, -32.0F, 1.1F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(41, 41).addBox(-5.7F, -32.0F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(17, 38).addBox(-6.7F, -35.0F, 1.1F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 12).addBox(-4.7F, -35.0F, 1.1F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(20, 34).addBox(-5.7F, -35.0F, 1.1F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(17, 34).addBox(-1.1F, -32.0F, 1.1F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(23, 41).addBox(-0.2F, -34.0F, 1.1F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(29, 41).addBox(0.8F, -34.0F, 1.1F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.65F, 1.25F, -3.5F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.65F, -28.25F, 0.7F, 0.0F, 3.1416F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0F, -3.1241F, 0.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(25, 17).addBox(-4.0F, -7.0F, 2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(25, 29).addBox(0.1F, -7.0F, 2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(33, 0).addBox(-4.1F, -7.0F, -6.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, -7.0F, -6.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LunarCreeperEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(LunarCreeperAnimations.ANIM_LUNAR_CREEPER_WALKING, limbSwing, limbSwingAmount, 0.8f, 2f);
        this.animate(entity.idleAnimationState, LunarCreeperAnimations.ANIM_LUNAR_CREEPER_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg4.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}