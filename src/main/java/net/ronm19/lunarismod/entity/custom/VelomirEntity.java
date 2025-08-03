package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class VelomirEntity extends AbstractHorse {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public VelomirEntity( EntityType<? extends AbstractHorse> pEntityType, Level pLevel ) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 70D)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 8f)
                .add(Attributes.FOLLOW_RANGE, 40D)
                .add(Attributes.JUMP_STRENGTH, 0.57f)
                .add(Attributes.ARMOR, 0.8D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.8D);
    }


    @Override
    public boolean isFood( ItemStack pStack ) {
        return pStack.is(ModItems.MOONPPLE.get());
    }


    @Override
    public @Nullable AgeableMob getBreedOffspring( ServerLevel pLevel, AgeableMob pOtherParent ) {
        return ModEntities.VELOMIR.get().create(pLevel);
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

    //--------- Ride Able Methods --------- //

    @Override
    public boolean canUseSlot( EquipmentSlot pSlot ) {
        return super.canUseSlot(pSlot);
    }

    @Override
    public InteractionResult mobInteract( Player pPlayer, InteractionHand pHand) {
        boolean flag = !this.isBaby() && this.isTamed() && pPlayer.isSecondaryUseActive();
        if (!this.isVehicle() && !flag) {
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            if (!itemstack.isEmpty()) {
                if (this.isFood(itemstack)) {
                    if (!this.isTamed()) {
                        this.tame(pPlayer);
                        this.level().broadcastEntityEvent(this, (byte) 7); // play heart particles
                    }

                    if (!this.isVehicle()) {
                        pPlayer.startRiding(this); // mount the horse immediately
                    }

                    itemstack.shrink(1); // consume the Moonpple
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
                if (!this.isTamed()) {
                    this.makeMad();
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }

            return super.mobInteract(pPlayer, pHand);
        } else {
            return super.mobInteract(pPlayer, pHand);
        }

    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isOnSurfaceOfWater()) {
            // Boost slightly if dipping too low
            if (this.getY() < this.blockPosition().below().getY() + 0.9D) {
                this.setDeltaMovement(this.getDeltaMovement().add(0, 0.1D, 0));
            }

            this.setOnGround(true);
            this.fallDistance = 0.0F;
        }

        super.travel(travelVector);
    }


    public boolean isOnSurfaceOfWater() {
        BlockPos belowPos = this.blockPosition().below();
        FluidState fluidBelow = this.level().getFluidState(belowPos);

        // Basic fluid check — ensure its water
        if (!fluidBelow.is(Fluids.WATER)) return false;

        // Get fluid height relative to block
        float fluidHeight = fluidBelow.getHeight(this.level(), belowPos);
        double expectedY = belowPos.getY() + fluidHeight;

        // Check if Velomir is roughly at water surface — tight vertical range
        return Math.abs(this.getY() - expectedY) <= 0.1D;
    }

    public void tame(Player player) {
        this.setOwnerUUID(player.getUUID());
        this.setTamed(true);
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick)
                .add(new Vec3(0.0, -0.18D, -0.0D)
                        .yRot(-this.getYRot() * (float) (Math.PI / 180.0)));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound( DamageSource damageSource ) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HORSE_DEATH;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SOUL_SAND_STEP, 0.15F, 1.0F);
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








