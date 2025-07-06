package net.ronm19.lunarismod.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.ai.goal.FollowAlphaGoal;
import net.ronm19.lunarismod.entity.ai.goal.ProtectAlphaAndOwnerGoal;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class LunarWolfEntity extends TamableAnimal {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public LunarWolfEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    // -------------------
    // Goals
    // -------------------
    @Override
    protected void registerGoals() {
        // --- Goal Selectors (behavior) ---
        this.goalSelector.addGoal(0, new FloatGoal(this)); // Always highest priority, for swimming
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true)); // Combat — highest after float
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.1D, 8.0F, 2.0F)); // Owner follow — high priority
        this.goalSelector.addGoal(3, new FollowAlphaGoal(this, 1.4D, 4.0F, 24.0F)); // Follow VoidHowler leader — just below owner follow
        this.goalSelector.addGoal(4, new SitWhenOrderedToGoal(this)); // Sit behavior
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, Ingredient.of(Items.BONE), false)); // Taming interaction
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.0D)); // Breeding
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.25D)); // For babies
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Wandering (lower priority)
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Look at player
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this)); // Idle look around

        // --- Target Selectors (aggression/retaliation) ---
        this.targetSelector.addGoal(11, new HurtByTargetGoal(this).setAlertOthers()); // React when attacked
        this.targetSelector.addGoal(12, new OwnerHurtByTargetGoal(this)); // Protect owner
        this.targetSelector.addGoal(13, new OwnerHurtTargetGoal(this)); // Retaliate for owner
        this.targetSelector.addGoal(14, new NearestAttackableTargetGoal<>(this, Monster.class, true)); // Attack hostile mobs
        this.targetSelector.addGoal(15, new ProtectAlphaAndOwnerGoal(this)); // Protect VoidHowler leader and owner
    }


    // -------------------
    // Attributes
    // -------------------
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    // -------------------
    // Food
    // -------------------
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.MOON_FRUIT_STEW.get());
    }

    // -------------------
    // Breeding
    // -------------------
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntities.LUNARWOLF.get().create(level);
    }

    // -------------------
    // Interactions
    // -------------------
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
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
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    public boolean isLeader() {
        return false;
    }
}
