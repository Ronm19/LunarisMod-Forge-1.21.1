package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.ai.goal.FlyingWanderAroundGoal;
import net.ronm19.lunarismod.entity.ai.goal.SafeFlyGoal;
import net.ronm19.lunarismod.item.ModItems;

import javax.annotation.Nullable;

public class LunareonEntity extends TamableAnimal implements FlyingAnimal {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private boolean isSaddled = false;
    private int moonveilCooldown = 0;
    private int aegisCooldown = 0;
    private int lunarRoarCooldown = 0;

    public LunareonEntity( EntityType<? extends TamableAnimal> type, Level level ) {
        super(type, level);
        this.setNoAi(false);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.8D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    protected PathNavigation createNavigation( Level level ) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        nav.setCanPassDoors(true);
        return nav;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 2.0F));
        this.goalSelector.addGoal(3, new FlyingWanderAroundGoal(this, 2f));  // Higher priority
        this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new SafeFlyGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, false));
        this.targetSelector.addGoal(3, new OwnerHurtByTargetGoal(this));  // Attack whoever hurts the owner
        this.targetSelector.addGoal(4, new OwnerHurtTargetGoal(this));    // Attack whoever the owner attacks
    }

    private void setAnimationStates() {
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
        if (this.level().isClientSide()) {
            this.setAnimationStates();
        }

        if (!this.onGround() && !this.isPassenger()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.add(0, 0.01D, 0));
        }
    }


    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isAlive()) {
            boolean isNight = !this.level().isDay();
            boolean isFullMoon = this.level().getMoonPhase() == 0;

            if (isNight) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, isFullMoon ? 1 : 0));
                this.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, isFullMoon ? 1 : 0));
            }

            if (isNight && this.getHealth() < this.getMaxHealth() && this.tickCount % 40 == 0) {
                this.heal(isFullMoon ? 2.0F : 1.0F);
                this.level().playSound(null, this.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.HOSTILE, 0.5F, 1.5F);
            }

            if (isFullMoon && this.isFlying()) {
                this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(1.2D);
                if (moonveilCooldown == 0) {
                    this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 0.5F);
                    moonveilCooldown = 200;
                }
            } else {
                this.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.8D);
            }

            if (this.isSaddled && aegisCooldown == 0 && this.getControllingPassenger() instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
                this.level().playSound(null, this.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1.0F);
                aegisCooldown = 600;
            }

            if (moonveilCooldown > 0) moonveilCooldown--;
            if (aegisCooldown > 0) aegisCooldown--;
            if (lunarRoarCooldown > 0) lunarRoarCooldown--;
        }
    }

    @Override
    public void travel( Vec3 travelVector ) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
                this.setYRot(player.getYRot());
                this.setXRot(player.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());

                float forward = player.zza * 0.5F;
                Vec3 look = this.getLookAngle();
                Vec3 moveVec = new Vec3(look.x * forward, look.y * forward, look.z * forward);
                this.setDeltaMovement(moveVec);

                super.travel(Vec3.ZERO);
                this.calculateEntityAnimation(false);

                if (this.level().isClientSide) {
                    this.level().addParticle(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 0.05D, 0.0D);
                }
            } else {
                super.travel(travelVector);
            }
        }
    }

    @Override
    public boolean doHurtTarget( Entity target ) {
        if (target instanceof LivingEntity living && this.isTame()) {
            this.setTarget(living);
        }
        return super.doHurtTarget(target);
    }

    @Override
    public void setTarget( @Nullable LivingEntity target ) {
        super.setTarget(target);
    }

    @Override
    public InteractionResult mobInteract( Player player, InteractionHand hand ) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (player.isCrouching() && this.isTame() && this.isSaddled && this.isVehicle() && lunarRoarCooldown == 0) {
            if (!level().isClientSide) {
                this.performLunarRoar();
                lunarRoarCooldown = 400;
            }
            return InteractionResult.SUCCESS;
        }

        if (!this.isTame() && itemstack.is(ModItems.NOCTRIUM_BONE.get())) {
            if (!this.level().isClientSide) {
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 1.0F, 1.0F);
                }
            }
            if (!player.getAbilities().instabuild) itemstack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && !this.isSaddled && itemstack.is(Items.SADDLE)) {
            if (!player.level().isClientSide) {
                this.isSaddled = true;
                this.level().playSound(null, this.blockPosition(), SoundEvents.HORSE_SADDLE, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            if (!player.getAbilities().instabuild) itemstack.shrink(1);
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && this.isSaddled && !this.isBaby()) {
            if (!player.level().isClientSide) {
                player.startRiding(this);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    public void performLunarRoar() {
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 2.0F, 1.0F);

        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5))) {
            if (entity != this && entity != this.getControllingPassenger() && this.canAttack(entity)) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                entity.hurt(this.damageSources().mobAttack(this), 6.0F);
                entity.knockback(1.0F, this.getX() - entity.getX(), this.getZ() - entity.getZ());
            }
        }

        this.level().addParticle(ParticleTypes.SONIC_BOOM, this.getX(), this.getY() + 1.0D, this.getZ(), 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean causeFallDamage( float fallDistance, float damageMultiplier, DamageSource source ) {
        return false;
    }

    @Override
    protected void checkFallDamage( double y, boolean onGround, BlockState state, BlockPos pos ) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return this.isVehicle() && this.getControllingPassenger() instanceof Player;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return (LivingEntity) this.getFirstPassenger();
    }

    @Override
    public @org.jetbrains.annotations.Nullable AgeableMob getBreedOffspring( ServerLevel level, AgeableMob parent ) {
        return ModEntities.LUNAREON.get().create(level);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint( Entity entity, EntityDimensions dimensions, float partialTick ) {
        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick)
                .add(new Vec3(0.0, -0.5D, -0.15D)
                        .yRot(-this.getYRot() * (float) (Math.PI / 180.0)));
    }

    @Override
    public void addAdditionalSaveData( CompoundTag tag ) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", this.isSaddled);
    }

    @Override
    public void readAdditionalSaveData( CompoundTag tag ) {
        super.readAdditionalSaveData(tag);
        this.isSaddled = tag.getBoolean("Saddled");
    }

    @Override
    public boolean isFood( ItemStack stack ) {
        return stack.is(ModItems.MOONSTONE.get());
    }

    public boolean isSaddled() {
        return this.isSaddled;
    }

    public static boolean checkLunareonSpawnRules( EntityType<LunareonEntity> type, Level level, MobSpawnType reason, BlockPos pos, RandomSource random ) {
        return level.getBrightness(LightLayer.BLOCK, pos) < 8 && pos.getY() > 100;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState blockState ) {
        this.playSound(SoundEvents.SOUL_SAND_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround() && !this.isInWater();
    }
}
