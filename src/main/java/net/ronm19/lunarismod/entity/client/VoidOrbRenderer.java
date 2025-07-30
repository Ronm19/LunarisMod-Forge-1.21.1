package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.VoidOrbEntity;

public class VoidOrbRenderer extends EntityRenderer<VoidOrbEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/void_orb.png");

    public VoidOrbRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.25f;
    }

    @Override
    public void render(VoidOrbEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack,
                       MultiBufferSource buffer, int packedLight) {

        matrixStack.pushPose();
        matrixStack.translate(0, 0.1f, 0);
        float scale = 0.5f;
        matrixStack.scale(scale, scale, scale);

        var pose = matrixStack.last();
        var vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        float r = 1f, g = 1f, b = 1f, a = 1f;

        vertexConsumer.addVertex(pose.pose(), 0.5f, -0.5f, 0f)
                .setColor(r, g, b, a)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(packedLight, packedLight)
                .setNormal(0f, 1f, 0f);

        vertexConsumer.addVertex(pose.pose(), 0.5f, -0.5f, 0f)
                .setColor(r, g, b, a)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(packedLight, packedLight)
                .setNormal(0f, 1f, 0f);

        vertexConsumer.addVertex(pose.pose(), 0.5f, -0.5f, 0f)
                .setColor(r, g, b, a)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(packedLight, packedLight)
                .setNormal(0f, 1f, 0f);

        vertexConsumer.addVertex(pose.pose(), 0.5f, -0.5f, 0f)
                .setColor(r, g, b, a)
                .setUv(1f, 1f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(packedLight, packedLight)
                .setNormal(0f, 1f, 0f);

        matrixStack.popPose();

        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);

        matrixStack.pushPose();

        matrixStack.translate(0, 0.1f, 0);
        scale = 0.5f;
        matrixStack.scale(scale, scale, scale);

        // Rotate quad to face the camera
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }



    @Override
    public ResourceLocation getTextureLocation(VoidOrbEntity entity) {
        return TEXTURE;
    }
}
