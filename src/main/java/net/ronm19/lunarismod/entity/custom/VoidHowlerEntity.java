package net.ronm19.lunarismod.entity.custom;

import net.ronm19.lunarismod.entity.ai.PackRole;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.ronm19.lunarismod.entity.ai.goal.*;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidHowlerEntity extends TamableAnimal {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private boolean isLeader;
    private long lastCallTime = 0;
    private BlockPos denPosition;
    private LivingEntity packTarget;
    private PackRole packRole = PackRole.LEADER;

    public VoidHowlerEntity( EntityType<? extends TamableAnimal> pEntityType, Level pLevel ) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new HowlToBuffAlliesGoal(this));
        this.goalSelector.addGoal(3, new VoidHowlerPatrolWithMemoryGoal(this));
        this.goalSelector.addGoal(4, new CallPackGoal(this));
        this.goalSelector.addGoal(5, new PatrolTerritoryGoal<>(this, 1.0D, this::getDenPosition));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.1D, 10.0F, 2.0F));
        this.goalSelector.addGoal(7, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(12, new MoonPhaseBuffGoal(this));

        this.targetSelector.addGoal(13, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(14, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(15, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(16, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 85.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 26.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.5D);
    }

    @Override
    public boolean isFood( ItemStack stack ) {
        return stack.is(ModItems.MOON_FRUIT_STEW.get());
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring( ServerLevel pLevel, AgeableMob pOtherParent ) {
        return null;
    }

    @Override
    public InteractionResult mobInteract( Player player, InteractionHand hand ) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.level().isClientSide && player.isCrouching()) {
            this.setPackTarget(player);
            player.sendSystemMessage(Component.literal("Void Howler: Target locked on player."));
            return InteractionResult.SUCCESS;
        }

        if (this.isTame()) {
            if (this.isOwnedBy(player) && this.getHealth() < this.getMaxHealth() && stack.is(Items.COOKED_BEEF)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(4.0F);
                return InteractionResult.SUCCESS;
            }
        } else if (stack.is(ModItems.NOCTRIUM_BONE.get())) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
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
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound( DamageSource damageSource ) {
        return SoundEvents.ALLAY_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    public boolean isLeader() {
        return this.packRole == PackRole.LEADER;
    }

    public void setLeader( boolean leader ) {
        if (leader) {
            this.packRole = PackRole.LEADER;
        }
    }

    public PackRole getPackRole() {
        return packRole != null ? packRole : PackRole.FOLLOWER;
    }

    public void setPackRole( PackRole role ) {
        this.packRole = role != null ? role : PackRole.FOLLOWER;
    }

    @Override
    public SpawnGroupData finalizeSpawn( ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                         @Nullable SpawnGroupData spawnData ) {
        SpawnGroupData finalData = super.finalizeSpawn(level, difficulty, reason, spawnData);

        boolean hasNearbyLeader = level.getEntitiesOfClass(VoidHowlerEntity.class, this.getBoundingBox().inflate(20.0D))
                .stream()
                .anyMatch(VoidHowlerEntity :: isLeader);

        if (!hasNearbyLeader) {
            this.setLeader(true);
        }

        return finalData;
    }

    public void setDenPosition( BlockPos pos ) {
        this.denPosition = pos;
    }

    @Nullable
    public BlockPos getDenPosition() {
        return this.denPosition;
    }

    public long getLastCallTime() {
        return this.lastCallTime;
    }

    public void setLastCallTime( long time ) {
        this.lastCallTime = time;
    }

    public void setPackTarget( LivingEntity target ) {
        this.packTarget = target;
    }

    @Nullable
    public LivingEntity getPackTarget() {
        return this.packTarget;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        LivingEntity currentTarget = this.getTarget();
        super.setTarget(target);

        if (target != null && target != currentTarget && this.isLeader()) {
            long gameTime = this.level().getGameTime();

            if (gameTime - this.getLastCallTime() >= 600) {
                this.setLastCallTime(gameTime);
                this.level().playSound(null, this.blockPosition(), ModSounds.VOID_HOWLER_HOWL.get(), SoundSource.HOSTILE, 1.5F, 1.0F);
            }
        }
    }

    public int getEyeGlowColor() {
        return 0;
    }

// ----------------------------------
// PACK COMMAND SYSTEM
// ----------------------------------

    public enum PackCommand {
        ATTACK, RETREAT, RALLY, BUFF
    }

    public void issueCommand(PackCommand command) {
        List<LunarWolfEntity> nearbyWolves = this.level().getEntitiesOfClass(
                LunarWolfEntity.class,
                this.getBoundingBox().inflate(40),
                wolf -> wolf.isAlive() && wolf.isTame()
        );

        for (LunarWolfEntity wolf : nearbyWolves) {
            wolf.receiveCommand(command, this); // Make sure this exists
        }

        // Play unique howl sound depending on command
        switch (command) {
            case ATTACK -> this.level().playSound(null, this.blockPosition(), ModSounds.VOID_HOWLER_HOWL.get(), SoundSource.HOSTILE, 1.5F, 1.0F);
            case RETREAT -> this.level().playSound(null, this.blockPosition(), SoundEvents.WOLF_WHINE, SoundSource.HOSTILE, 1.0F, 1.0F);
            case RALLY   -> this.level().playSound(null, this.blockPosition(), SoundEvents.WOLF_HOWL, SoundSource.HOSTILE, 1.0F, 1.0F);
            case BUFF    -> this.level().playSound(null, this.blockPosition(), SoundEvents.WOLF_GROWL, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }
}
