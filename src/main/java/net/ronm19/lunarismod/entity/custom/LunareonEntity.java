package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.ai.goal.FlyingWanderAroundGoal;
import net.ronm19.lunarismod.entity.ai.goal.SafeFlyGoal;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class LunareonEntity extends AbstractHorse implements FlyingAnimal, OwnableEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public LunareonEntity(EntityType<? extends AbstractHorse> type, Level level) {
        super(type, level);
    }

    // ========== ATTRIBUTES ========== //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 78D)
                .add(Attributes.MOVEMENT_SPEED, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 8f)
                .add(Attributes.FOLLOW_RANGE, 50D)
                .add(Attributes.JUMP_STRENGTH, 0.57f)
                .add(Attributes.ARMOR, 0.8D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.8D)
                .add(Attributes.FLYING_SPEED, 0.6f);  // Flying speed
    }

    // ========== GOALS ========== //
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SafeFlyGoal(this));
        this.goalSelector.addGoal(2, new FlyingWanderAroundGoal(this, 1.0F));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.3D, true));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(Items.GOLDEN_CARROT), false));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    private void setupAnimationStates() {
        // Check if entity is standing still
        if (this.getDeltaMovement().lengthSqr() < 0.01D) {
            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = 48;
                this.idleAnimationState.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
        } else {
            this.idleAnimationState.stop();
            this.idleAnimationTimeout = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            this.setupAnimationStates();
        }

    }

    // ========== FLYING LOGIC ========== //
    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            LivingEntity rider = (LivingEntity) this.getControllingPassenger();
            if (rider != null) {
                this.setRot(rider.getYRot(), rider.getXRot() * 0.5F);
                this.setYHeadRot(this.getYRot());

                float forward = rider.zza;
                float strafe = rider.xxa;
                float vertical = 0f;

                if (rider.getXRot() < -10f) {
                    vertical = 0.7f; // Ascend
                } else if (rider.getXRot() > 10f) {
                    vertical = -0.7f; // Descend
                }

                if (rider.isSprinting()) {
                    vertical -= 0.5f; // Faster descend
                }

                Vec3 moveVec = new Vec3(strafe, vertical, forward);
                if (!moveVec.equals(Vec3.ZERO)) {
                    moveVec = moveVec.normalize().scale(0.6f);
                    moveVec = moveVec.yRot(-this.getYRot() * ((float) Math.PI / 180F));
                }

                this.setDeltaMovement(moveVec);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setSpeed(0.6f);
                return;
            }
        }
        super.travel(travelVector);
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // Immune to fall damage
    }

    // ========== BREEDING ========== //
    @Override
    public @Nullable AgeableMob getBreedOffspring( ServerLevel level, AgeableMob otherParent) {
        return ModEntities.LUNAREON.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModItems.MOONPPLE.get());
    }

    public void tame(Player player) {
        this.setOwnerUUID(player.getUUID());
        this.setTamed(true);
    }

    // ========== PLAYER INTERACT ========== //
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean isTamed = this.isTamed();

        if (!this.isVehicle() && (!this.isBaby() && isTamed && player.isSecondaryUseActive())) {
            return super.mobInteract(player, hand);
        }

        if (!itemstack.isEmpty()) {
            if (this.isFood(itemstack)) {
                if (!isTamed) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7); // hearts
                }

                if (!this.isVehicle()) {
                    player.startRiding(this);
                }

                itemstack.shrink(1);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (!isTamed) {
                this.makeMad();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        return super.mobInteract(player, hand);
    }

    // ========== SOUNDS ========== //
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SOUL_SAND_STEP, 0.15F, 1.0F);
    }

    // ========== MISCELLANEOUS ========== //
    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick)
                .add(new Vec3(0.0, -0.5D, -0.1D)
                        .yRot((float) (-this.getYRot() * ((float) Math.PI / 180.0))));
    }

    @Override
    public boolean isLeashed() {
        return super.isLeashed();
    }

    @Override
    public boolean mayBeLeashed() {
        return super.mayBeLeashed();
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        return super.getOwner();
    }
}
