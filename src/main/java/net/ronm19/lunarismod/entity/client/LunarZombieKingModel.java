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
import net.ronm19.lunarismod.entity.custom.LunarZombieKingEntity;

public class LunarZombieKingModel extends HierarchicalModel<LunarZombieKingEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "lunar_zombie_king"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart neck;
    private final ModelPart torso;
    private final ModelPart arm1;
    private final ModelPart arm2;
    private final ModelPart leg1;
    private final ModelPart leg2;

    public LunarZombieKingModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.neck = root.getChild("neck");
        this.torso = root.getChild("torso");
        this.arm1 = root.getChild("arm1");
        this.arm2 = root.getChild("arm2");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(50, 14).addBox(-4.0F, -41.1F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(54, 79).addBox(-4.0F, -41.1F, -8.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(78, 79).addBox(1.9F, -46.1F, -8.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(66, 79).addBox(1.9F, -41.1F, -8.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 30).addBox(-4.0F, -46.1F, -8.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 72).addBox(-3.4F, -42.1F, -3.0F, 7.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(80, 72).addBox(-3.4F, -44.1F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 81).addBox(-3.4F, -44.1F, 0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 81).addBox(-1.4F, -44.1F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 83).addBox(-2.4F, -44.1F, 3.0F, -1.0F, 2.0F, -1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 81).addBox(0.4F, -44.1F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 81).addBox(2.5F, -44.1F, 2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 81).addBox(2.6F, -44.1F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(82, 13).addBox(2.6F, -44.1F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(82, 13).addBox(2.6F, -44.1F, -1.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(82, 17).addBox(0.6F, -44.1F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(82, 21).addBox(-1.6F, -44.1F, -3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 81).addBox(-3.4F, -44.1F, -2.9F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.9F, 0.2F));

        PartDefinition neck = partdefinition.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 14).addBox(-7.0F, -40.1F, -5.0F, 14.0F, 15.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-12.0F, -43.1F, -5.0F, 25.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 34.0F, 0.0F));

        PartDefinition arm1 = partdefinition.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(0, 40).addBox(7.0F, -30.0F, -4.0F, 6.0F, 23.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition arm2 = partdefinition.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(30, 40).addBox(-13.2F, -30.0F, -4.0F, 6.0F, 23.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(60, 51).addBox(-5.5F, -15.9F, -3.5F, 5.0F, 13.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 72).addBox(-5.5F, -2.9F, -5.5F, 5.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(60, 30).addBox(1.5F, -15.9F, -3.5F, 5.0F, 13.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(72, 0).addBox(1.5F, -2.9F, -5.5F, 5.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LunarZombieKingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);

        // Animation triggers
        this.animateWalk(LunarZombieKingAnimations.ANIM_LUNAR_ZOMBIE_KING_WALKING, limbSwing, limbSwingAmount, 0.8f, 2f);
        this.animate(entity.idleAnimationState, LunarZombieKingAnimations.ANIM_LUNAR_ZOMBIE_KING_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        neck.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        torso.render(poseStack, vertexConsumer, packedLight, packedOverlay,color);
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
