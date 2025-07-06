package net.ronm19.lunarismod.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.ronm19.lunarismod.entity.ai.goal.CallPackGoal;
import net.ronm19.lunarismod.entity.ai.goal.HowlToBuffAlliesGoal;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;

public class VoidHowlerEntity extends TamableAnimal {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public VoidHowlerEntity( EntityType<? extends TamableAnimal> pEntityType, Level pLevel ) {
        super(pEntityType, pLevel);
    }

    // -------------------
    // Goals
    // -------------------
    @Override
    protected void registerGoals() {
        // --- Goal Selectors (behavior) ---
        this.goalSelector.addGoal(0, new FloatGoal(this)); // Always highest priority to keep afloat
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true)); // Combat attack
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F)); // Follow owner if tamed
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Natural roaming
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Look at nearby players
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this)); // Idle look behavior
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D)); // Follow parent if baby
        this.goalSelector.addGoal(7, new HowlToBuffAlliesGoal(this)); // Special alpha ability to buff allies
        this.goalSelector.addGoal(8, new CallPackGoal(this)); // Alpha howls to gather and buff the pack

// --- Target Selectors (aggression/retaliation) ---
        this.targetSelector.addGoal(9, new OwnerHurtByTargetGoal(this)); // Protect owner when hurt
        this.targetSelector.addGoal(10, new OwnerHurtTargetGoal(this));   // Attack target owner attacks
        this.targetSelector.addGoal(11, new HurtByTargetGoal(this).setAlertOthers()); // Retaliate when attacked
        this.targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this, Monster.class, true)); // Attack hostile mobs// Protect self and owner

    }

    // -------------------
    // Attributes
    // -------------------
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

    // -------------------
    // Interactions
    // -------------------
    @Override
    public InteractionResult mobInteract( Player player, InteractionHand hand ) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.isTame()) {
            if (this.isOwnedBy(player) && this.getHealth() < this.getMaxHealth() && stack.is(Items.COOKED_BEEF)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                this.heal(4.0F);
                return InteractionResult.SUCCESS;
            }
        } else if (stack.is(Items.BONE)) {
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

    // -------------------
// Animation States
// -------------------
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

    // -------------------
// Sounds
// -------------------
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

    private boolean isLeader;

    public boolean isLeader() {
        return this.isLeader;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData) {
        SpawnGroupData finalData = super.finalizeSpawn(level, difficulty, reason, spawnData);

        // Only assign leader if no other VoidHowler nearby is marked as leader
        boolean hasNearbyLeader = level.getEntitiesOfClass(VoidHowlerEntity.class, this.getBoundingBox().inflate(20.0D))
                .stream()
                .anyMatch(VoidHowlerEntity::isLeader);

        if (!hasNearbyLeader) {
            this.setLeader(true);
            // System.out.println("Void Howler promoted to leader.");
        }

        return finalData;
    }


    public void setLeader(boolean leader) {
        this.isLeader = leader;
    }



    private long lastCallTime = 0;

    public long getLastCallTime() {
        return this.lastCallTime;
    }

    public void setLastCallTime(long time) {
        this.lastCallTime = time;
    }
}


