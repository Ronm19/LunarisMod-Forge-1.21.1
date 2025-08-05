package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.ai.CommandMode;
import net.ronm19.lunarismod.entity.ai.goal.PatrolAroundGoal;
import net.ronm19.lunarismod.entity.ai.goal.HoldPositionGoal;
import net.ronm19.lunarismod.entity.ai.goal.ShieldBlockGoal;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.sound.ModSounds;
import org.jetbrains.annotations.Nullable;

public class LunarKnightEntity extends TamableAnimal {

    // Animation
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    // Blocking
    private boolean isBlocking = false;
    private int blockCooldown = 0;
    private boolean tomeSummoned = false;

    // Declare these as fields so you can manage them later
    private final FollowOwnerGoal followOwnerGoal = new FollowOwnerGoal(this, 1.2D, 5.0F, 2.0F);
    private final HoldPositionGoal holdPositionGoal = new HoldPositionGoal(this);
    private final PatrolAroundGoal patrolGoal = new PatrolAroundGoal(this);


    public LunarKnightEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.6F));
        this.goalSelector.addGoal(3, new ShieldBlockGoal(this));

        // Other goals that are always active (like LookAtPlayerGoal, RandomLookAroundGoal)
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        // Target goals...
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Slime.class, true));
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this).setAlertOthers(LunarKnightEntity.class));

        // Add the command-related goal according to current command mode
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 150.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.isTame() && stack.is(ModItems.LUNAR_HEROBRINE_GEM.get())) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.playSummonVoiceline();
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }

        if (this.isTame() && this.isOwnedBy(player) && stack.is(ModItems.MOONPPLE.get()) && this.getHealth() < this.getMaxHealth()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            this.heal(4.0F);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    // Tick logic
    @Override
    public void tick() {
        super.tick();

        if (commandMode == CommandMode.HOLD) {
            this.getNavigation().stop();
        }

        if (getMainHandItem().isEmpty()) {
            setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.NOCTRIUM_SWORD.get()));
        }
        if (getOffhandItem().isEmpty()) {
            setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.SHIELD));
        }

        if (this.level().isClientSide()) setupAnimationStates();

        if (!level().isClientSide && tomeSummoned && tickCount % 60 == 0) {
            ((ServerLevel) level()).sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    getX(), getY() + 1.0, getZ(), 10, 0.2, 0.5, 0.2, 0.01);
        }

        if (blockCooldown > 0) blockCooldown--;
    }

    public CommandMode commandMode = CommandMode.FOLLOW;

    public CommandMode getCommandMode() {
        return commandMode;
    }

    public void setCommandMode( CommandMode newMode) {
        if (this.commandMode != newMode) {
            this.commandMode = newMode;
            updateCommandGoals();
        }
    }
    public void updateCommandGoals() {
        // Remove all command-related goals first
        this.goalSelector.removeGoal(followOwnerGoal);
        this.goalSelector.removeGoal(holdPositionGoal);
        this.goalSelector.removeGoal(patrolGoal);

        // Add only the goal matching current command mode
        switch (this.commandMode) {
            case FOLLOW -> this.goalSelector.addGoal(4, followOwnerGoal);
            case HOLD -> this.goalSelector.addGoal(4, holdPositionGoal);
            case PATROL -> this.goalSelector.addGoal(4, patrolGoal);
            default -> throw new IllegalStateException("Unexpected value: " + this.commandMode);
        }
    }


    public boolean canBlock() {
        return blockCooldown <= 0;
    }

    public void setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }

    public boolean isBlocking() {
        return this.isBlocking;
    }

    public void triggerBlockCooldown(float damageBlocked) {
        this.blockCooldown = Mth.clamp((int)(damageBlocked * 10), 40, 100);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isBlocking() && source.is(DamageTypeTags.IS_PROJECTILE)) {
            float reduced = amount * 0.5F;
            triggerBlockCooldown(reduced);

            if (!level().isClientSide) {
                ((ServerLevel) level()).sendParticles(ParticleTypes.ENCHANT, getX(), getY() + 1.0D, getZ(),
                        10, 0.2D, 0.2D, 0.2D, 0.01D);
            }

            level().playSound(null, blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0F, 1.0F);
            return super.hurt(source, reduced);
        }
        return super.hurt(source, amount);
    }

    private void setupAnimationStates() {
        if (getDeltaMovement().lengthSqr() > 0.01D) {
            idleAnimationState.stop();
            idleAnimationTimeout = 0;
        } else {
            if (idleAnimationTimeout <= 0) {
                idleAnimationTimeout = 40;
                idleAnimationState.start(tickCount);
            } else {
                idleAnimationTimeout--;
            }
        }
    }

    private void playSummonVoiceline() {
        if (!level().isClientSide) {
            level().playSound(null, blockPosition(), ModSounds.LUNAR_KNIGHT_SUMMON.get(), SoundSource.NEUTRAL, 7.0F, 1.0F);
        }
    }

    public boolean isTomeSummoned() {
        return tomeSummoned;
    }

    public void setTomeSummoned(boolean value) {
        this.tomeSummoned = value;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("TomeSummoned", this.tomeSummoned);
        tag.putInt("BlockCooldown", this.blockCooldown);
        tag.putString("CommandMode", this.commandMode.name());

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.tomeSummoned = tag.getBoolean("TomeSummoned");
        this.blockCooldown = tag.getInt("BlockCooldown");
        try {
            this.commandMode = CommandMode.valueOf(tag.getString("CommandMode"));
        } catch (IllegalArgumentException e) {
            this.commandMode = CommandMode.FOLLOW;
        }
        updateCommandGoals();

    }

    @Override public boolean isFood(ItemStack stack) { return false; }
    @Override public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) { return null; }
    @Override public boolean canMate(Animal other) { return false; }
}
