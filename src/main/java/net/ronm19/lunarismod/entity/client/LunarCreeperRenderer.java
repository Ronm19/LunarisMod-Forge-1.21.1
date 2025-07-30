package net.ronm19.lunarismod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.ronm19.lunarismod.LunarisMod;
import net.ronm19.lunarismod.entity.custom.LunarCreeperEntity;

import static net.minecraft.world.entity.Mob.checkMobSpawnRules;

public class LunarCreeperRenderer extends MobRenderer<LunarCreeperEntity, LunarCreeperModel> {

    public LunarCreeperRenderer( EntityRendererProvider.Context pContext) {
        super(pContext, new LunarCreeperModel(pContext.bakeLayer(LunarCreeperModel.LAYER_LOCATION)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation( LunarCreeperEntity lunarCreeperEntity ) {
        return ResourceLocation.fromNamespaceAndPath(LunarisMod.MOD_ID, "textures/entity/lunar_creeper/lunar_creeper.png");
    }

    @Override
    public void render( LunarCreeperEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight ) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}