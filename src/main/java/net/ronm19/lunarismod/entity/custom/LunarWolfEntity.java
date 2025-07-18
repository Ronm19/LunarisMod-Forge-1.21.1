package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ai.PackRole;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.entity.ai.goal.*;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

import static net.ronm19.lunarismod.entity.custom.VoidHowlerEntity.PackCommand.BUFF;
import static net.ronm19.lunarismod.entity.custom.VoidHowlerEntity.PackCommand.RALLY;

public class LunarWolfEntity extends TamableAnimal {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    // -------------------
    // Pack behavior fields
    // -------------------
    private LivingEntity packTarget = null;
    private BlockPos territoryCenter = null;

    // Default to SCOUT, change as needed
    private PackRole packRole = PackRole.SCOUT;

    private boolean isRetreating = false;
    private int attackCooldownTicks = 0;

    public LunarWolfEntity( EntityType<? extends TamableAnimal> type, Level level ) {
        super(type, level);
    }

    public int getEyeGlowColor() {
        if (this.packRole == null) {
            return 0xFFFFFF; // Default white if no role
        }

        return switch (this.packRole) {
            case LEADER -> 0x800080;   // Purple
            case SCOUT -> 0x00FFFF;    // Cyan
            case GUARDIAN -> 0xFF0000; // Red
            case FOLLOWER -> 0xFFFFFF; // White
        };
    }
    // -------------------
    // Goals
    // -------------------
    @Override
    protected void registerGoals() {
        // --- Movement & retreat survival ---
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicAttackGoal(this, 1.5f));
        this.goalSelector.addGoal(2, new PackRetreatGoal(this));

        // --- Combat & pack synergy ---
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.3D, true));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(5, new PatrolWithMemoryGoal(this));
        this.targetSelector.addGoal(6, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(7, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Monster.class, true));
        this.targetSelector.addGoal(9, new ProtectAlphaAndOwnerGoal(this));
        this.goalSelector.addGoal(10, new DynamicSwitchRoleGoal(this));
        this.targetSelector.addGoal(11, new PackAttackGoal(this));
        this.targetSelector.addGoal(12, new PackFlankGoal(this, 2.0));
        this.targetSelector.addGoal(13, new SmartTargetGoal(this));


        // --- Buffs based on lunar phases ---
        this.goalSelector.addGoal(14, new MoonPhaseBuffGoal(this));

        // --- Following & loyalty ---
        this.goalSelector.addGoal(15, new FollowOwnerGoal(this, 1.1D, 8.0F, 2.0F));
        this.goalSelector.addGoal(16, new FollowAlphaGoal(this, 1.4D, 4.0F, 24.0F));
        this.goalSelector.addGoal(17, new SitWhenOrderedToGoal(this));

        // --- Basic AI and interactions ---
        this.goalSelector.addGoal(18, new TemptGoal(this, 1.25D, Ingredient.of(Items.BONE), false));
        this.goalSelector.addGoal(19, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(20, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(21, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(22, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(23, new RandomLookAroundGoal(this));
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
    public boolean isFood( ItemStack stack ) {
        return stack.is(ModItems.MOON_FRUIT_STEW.get());
    }

    // -------------------
    // Breeding
    // -------------------
    @Nullable
    @Override
    public AgeableMob getBreedOffspring( ServerLevel level, AgeableMob otherParent ) {
        return ModEntities.LUNARWOLF.get().create(level);
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

        // Update attack cooldown
        if (this.attackCooldownTicks > 0) {
            this.attackCooldownTicks--;
        }
    }

    // -------------------
    // Pack Role & Combat Logic
    // -------------------

    public LivingEntity getPackTarget() {
        return packTarget;
    }

    public void setPackTarget( LivingEntity target ) {
        this.packTarget = target;
    }

    public boolean isRetreating() {
        return isRetreating;
    }

    public void setRetreating( boolean retreating ) {
        isRetreating = retreating;
    }

    public int getAttackCooldownTicks() {
        return attackCooldownTicks;
    }

    public void setAttackCooldownTicks( int ticks ) {
        attackCooldownTicks = ticks;
    }

    public PackRole getPackRole() {
        return packRole != null ? packRole : PackRole.FOLLOWER;
    }

    public void setPackRole( PackRole role ) {
        packRole = role != null ? role : PackRole.FOLLOWER;
    }

    public boolean isLeader() {
        return this.packRole == PackRole.LEADER;
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
        return SoundEvents.ENDERMAN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    // Dummy swing method placeholder (implement as needed)
    public void swing( ItemStack mainHandItem ) {
        // Implement animation or attack swing here if necessary
    }

    public void setLeader( boolean b ) {
    }

    public void receiveCommand( VoidHowlerEntity.PackCommand command, VoidHowlerEntity sender) {
        switch (command) {
            case ATTACK -> {
                if (sender.getPackTarget() != null) {
                    this.setPackTarget(sender.getPackTarget());
                    this.setTarget(sender.getPackTarget());
                }
            }
            case RETREAT -> {
                this.setTarget(null);
                Vec3 retreatPos = this.position().add(5, 0, 5);
                this.getNavigation().moveTo(retreatPos.x, retreatPos.y, retreatPos.z, 1.2D);
            }
            case RALLY -> {
                this.getNavigation().moveTo(sender, 1.5D);
            }
            case BUFF -> {
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
            }
        }
    }// <-- This closes the receiveCommand method properly


// ⬇️ Now OUTSIDE of receiveCommand — these are regular methods of the class

    /**
     * Sets the central territory position that this Lunar Wolf considers its patrol zone.
     * @param pos The BlockPos to set as the territory center.
     */
    public void setTerritoryCenter(BlockPos pos) {
        this.territoryCenter = pos;
    }

    /**
     * Gets the central territory position of this Lunar Wolf, if assigned.
     * @return The BlockPos of the territory center, or null if not yet set.
     */
    @Nullable
    public BlockPos getTerritoryCenter() {
        return this.territoryCenter;
    }

    private Object mate;
    PathfinderMob wolf = (PathfinderMob) mate;

}



