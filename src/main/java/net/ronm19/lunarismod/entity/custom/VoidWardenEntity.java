package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.ronm19.lunarismod.item.ModItems;

public class VoidWardenEntity extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public VoidWardenEntity( EntityType<? extends Monster> pEntityType, Level pLevel ) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {

        //---- Goals ---- ///
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1f, true));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1f, 2, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 2f, 1f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 10f));

        //----- Target Goals ---- ///
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(VoidWardenEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new MoveTowardsTargetGoal(this, 1f, 3f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300D)
                .add(Attributes.ATTACK_DAMAGE, 9.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1D)
                .add(Attributes.ARMOR, 0.9D)
                .add(Attributes.FOLLOW_RANGE, 80D)
                .add(Attributes.SNEAKING_SPEED, 0.5D);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
        if (level().isClientSide && tickCount % 10 == 0) {
            level().addParticle(ParticleTypes.SCULK_SOUL, getX(), getY() + 1.0, getZ(), 0, 0.1, 0);
        }
    }

    private LivingEntity previousTarget = null; // Add this field to your class

    @Override
    public void setTarget(LivingEntity target) {
        super.setTarget(target);

        if (!this.level().isClientSide && target != null && target != previousTarget) {
            this.playSound(SoundEvents.WARDEN_ROAR, 1.5F, 1.0F);
        }

        this.previousTarget = target; // Update the previous target
    }


    public static boolean checkMonsterSpawnRules(
            EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom
    ) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL
                && (MobSpawnType.ignoresLightRequirements(pSpawnType) || isDarkEnoughToSpawn(pLevel, pPos, pRandom))
                && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    protected void dropCustomDeathLoot( ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);

        // Common drop: 1–2 Moonstones
        int count = this.random.nextInt(1, 3); // Drops 1 or 2
        this.spawnAtLocation(ModItems.MOONSTONE.get(), count);

        // Rare drop: 10% chance for Lunar Spear
        if (this.random.nextFloat() < 0.10F) {
            this.spawnAtLocation(ModItems.LUNAR_SPEAR.get());
        }
    }



    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound( DamageSource pDamageSource) {
        return SoundEvents.WARDEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SCULK_BLOCK_STEP, 0.15F, 1.0F);
    }
}


