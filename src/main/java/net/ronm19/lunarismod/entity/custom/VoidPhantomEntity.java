package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ai.goal.FlyingWanderAroundGoal;
import net.ronm19.lunarismod.entity.ai.goal.PhantomAttackGoal;
import net.ronm19.lunarismod.entity.ai.goal.SafeFlyGoal;

public class VoidPhantomEntity extends Monster implements FlyingAnimal {

    private boolean shouldAscend = false;
    private boolean shouldDescend = false;

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public VoidPhantomEntity(EntityType<? extends VoidPhantomEntity> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.FLYING_SPEED, 0.8D)
                .add(Attributes.ARMOR, 0.1D)
                .add(Attributes.STEP_HEIGHT, 1.5F);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.0F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new PhantomAttackGoal(this));
        this.goalSelector.addGoal(6, new SafeFlyGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new FlyingWanderAroundGoal(this, 3.0D));
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, 2.0D));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 2.0D));
        this.goalSelector.addGoal(11, new HurtByTargetGoal(this).setAlertOthers(VoidPhantomEntity.class));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    protected void tickRidden(Player rider, Vec3 travelVector) {
        super.tickRidden(rider, travelVector);
        this.setRot(rider.getYRot(), rider.getXRot() * 0.5F);
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public static boolean checkMonsterSpawnRules(
            EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom
    ) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL
                && (MobSpawnType.ignoresLightRequirements(pSpawnType) || isDarkEnoughToSpawn(pLevel, pPos, pRandom))
                && checkMobSpawnRules((EntityType<? extends Mob>) pType, (LevelAccessor) pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity directEntity = source.getDirectEntity();

        // Immune to fire and lightning
        if (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypes.LIGHTNING_BOLT)) {
            return false;
        }

        // Prevent damage from the player riding it
        if (directEntity != null && directEntity == this.getControllingPassenger()) {
            return false;
        }

        return super.hurt(source, amount);
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }


    public static boolean canSpawn(EntityType<VoidPhantomEntity> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isAir() || spawnType == MobSpawnType.SPAWNER; // basic airborne check
    }


    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // Never despawn if far
    }


    public void setShouldAscend(boolean ascend) {
        this.shouldAscend = ascend;
        if (ascend) this.shouldDescend = false;
    }

    public void setShouldDescend(boolean descend) {
        this.shouldDescend = descend;
        if (descend) this.shouldAscend = false;
    }

    public boolean isAscending() {
        return this.shouldAscend;
    }

    public boolean isDescending() {
        return this.shouldDescend;
    }


    private void setupAnimationStates() {
        if (this.idleAnimationTimeout-- <= 0) {
            this.idleAnimationTimeout = 80 + this.random.nextInt(40);
            this.idleAnimationState.start(this.tickCount);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    public void swing( ItemStack mainHandItem ) {
    }
}
