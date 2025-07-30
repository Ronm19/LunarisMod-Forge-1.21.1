package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.ai.goal.FlyingWanderAroundGoal;
import net.ronm19.lunarismod.entity.ai.goal.VoidOrbAttackGoal;
import net.ronm19.lunarismod.entity.ai.interfaces.RangedShooter;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.entity.monster.Monster.isDarkEnoughToSpawn;

public class VoidEyeEntity extends TamableAnimal implements FlyingAnimal, RangedAttackMob, RangedShooter {


    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private boolean glowing;
    private LivingEntity target;
    private float distanceFactor;

    public VoidEyeEntity( EntityType<? extends TamableAnimal> entityType, Level level ) {
        super(entityType, level);
        this.setPersistenceRequired();
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.FOLLOW_RANGE, 1200) // You can increase this for longer detection
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.FLYING_SPEED, 0.18D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
    }

    @Override
    protected PathNavigation createNavigation( Level level ) {
        // Use FlyingPathNavigation for proper flying pathfinding
        return new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 2.0D, 20, 15.0F)); // attack with 15 blocks max range
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.5D, 5.0F, 2.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new FlyingWanderAroundGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new VoidOrbAttackGoal(this, 16.0D, 40)); // every 2 seconds at 16 block range
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, VoidPhantomEntity.class, false));

    }

    @Override
    public InteractionResult mobInteract( Player player, InteractionHand hand ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.level().isClientSide) {
            if (!this.isTame() && stack.is(ModItems.VOID_PULSE.get())) {
                this.tame(player);
                this.setOwnerUUID(player.getUUID());
                this.level().broadcastEntityEvent(this, (byte) 7);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood( ItemStack stack ) {
        return stack.is(ModItems.VOID_PULSE.get());
    }

    private void setAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 80;
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
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring( ServerLevel level, AgeableMob otherParent ) {
        return ModEntities.VOID_EYE.get().create(level);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround() && !this.isInWater();
    }

    @Override
    public void travel( Vec3 travelVector ) {
        if (this.isFlying()) {
            Vec3 adjusted = travelVector.add(0, 0.05, 0); // Small upward push so it flies a bit higher
            super.travel(adjusted);
        } else {
            super.travel(travelVector);
        }
    }

    public static boolean canSpawn(EntityType<VoidPhantomEntity> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isAir() || spawnType == MobSpawnType.SPAWNER; // basic airborne check
    }


    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo( DamageSource source ) {
        return source.is(DamageTypes.MAGIC) ||
                source.is(DamageTypes.INDIRECT_MAGIC) ||
                source.is(DamageTypes.ON_FIRE) ||
                super.isInvulnerableTo(source);
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
        super.setGlowingTag(glowing); // Optional if you want vanilla glow effect
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            boolean hostileNearby = this.level().getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(16),
                    mob -> mob.isAlive() && this.hasLineOfSight(mob) && this.distanceTo(mob) < 16).size() > 0;

            this.setGlowing(hostileNearby);
        }


        if (this.level().isClientSide) {
            if (this.swinging) {
                this.level().addParticle(ParticleTypes.ENCHANT, this.getX(), this.getY() + 1.0D, this.getZ(), 0, 0, 0);
            } else {
                this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY() + 0.5D, this.getZ(), 0, 0, 0);
            }


            boolean hostileNearby = false;
            this.setGlowing(hostileNearby);

        }
    }

    public static boolean checkMonsterSpawnRules(
            EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom ) {
        {
    }
        return false;
    }

    @Override
    public void performRangedAttack( LivingEntity target, float distanceFactor ) {
        this.target = target;
        this.distanceFactor = distanceFactor;
        if (!this.level().isClientSide) {
            Vec3 direction = target.position().subtract(this.position()).normalize();
            double velocity = 1.5;  // Adjust for sniper feel, increase for faster projectile

            VoidOrbEntity orb = new VoidOrbEntity(this.level(), this);
            orb.setBaseDamage(8.0); // Optional: higher damage for sniper feel
            orb.setPos(this.getX(), this.getEyeY() - 0.1, this.getZ());
            orb.setDeltaMovement(direction.scale(velocity));

            this.level().addFreshEntity(orb);
            this.level().playSound(null, this.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn( ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                         @Nullable SpawnGroupData data, @Nullable CompoundTag tag ) {
        this.setPersistenceRequired();
        return super.finalizeSpawn(level, difficulty, reason, data);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound( DamageSource damageSource ) {
        return SoundEvents.PHANTOM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    public boolean isGlowing() {
        return glowing;
    }

    @Override
    public void shootAtTarget( LivingEntity target ) {
        if (!this.level().isClientSide) {
            Vec3 direction = target.position().subtract(this.position()).normalize();
            double velocity = 9.5;  // Adjust velocity for sniper feel

            VoidOrbEntity orb = new VoidOrbEntity(this.level(), this);
            orb.setBaseDamage(11.0);  // Optional: stronger damage for sniper effect
            orb.setPos(this.getX(), this.getEyeY() - 0.1, this.getZ());
            orb.setDeltaMovement(direction.scale(velocity));

            this.level().addFreshEntity(orb);
            this.level().playSound(null, this.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }
}

