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
import net.ronm19.lunarismod.entity.custom.NoctriumTomahawkProjectileEntity;

public class NoctriumTomahawkProjectileModel extends EntityModel<NoctriumTomahawkProjectileEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "noctrium_tomahawk"), "main");
    private final ModelPart noctrium_tomahawk;

    public NoctriumTomahawkProjectileModel(ModelPart root) {
        this.noctrium_tomahawk = root.getChild("noctrium_tomahawk");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition noctrium_tomahawk = partdefinition.addOrReplaceChild("noctrium_tomahawk", CubeListBuilder.create(), PartPose.offset(0.0F, 16.5F, 0.0F));

        PartDefinition cube_r1 = noctrium_tomahawk.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 7).addBox(1.5F, 2.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -4.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r2 = noctrium_tomahawk.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(7, 9).addBox(0.5F, -1.5F, -0.5F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -5.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r3 = noctrium_tomahawk.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(3, 10).addBox(-2.5F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 5.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r4 = noctrium_tomahawk.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, 4).addBox(-2.5F, -1.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r5 = noctrium_tomahawk.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 1).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 18.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(NoctriumTomahawkProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        noctrium_tomahawk.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
