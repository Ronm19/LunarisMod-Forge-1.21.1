package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.ronm19.lunarismod.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class LunarSentinelEntity extends Monster {

    /* ─────────────── Animation states ─────────────── */
    public final AnimationState idleAnimationState   = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;                  // controls idle loop
    private int attackAnimationTimeout = 0;

    /* ─────────────── Constructor ─────────────── */
    public LunarSentinelEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.xpReward = 10;
    }

    /* ─────────────── AI Goals ─────────────── */
    @Override
    protected void registerGoals() {
        // Movement / combat
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        // Targeting
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TamableAnimal.class, true));
        /* add more target goals here if you want it to attack other mobs */
    }

    /* ─────────────── Attributes ─────────────── */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE,     8.0D)
                .add(Attributes.ATTACK_KNOCKBACK,   0.9D)
                .add(Attributes.MAX_HEALTH,        80.0D)
                .add(Attributes.MOVEMENT_SPEED,     0.5D)
                .add(Attributes.FOLLOW_RANGE,      32.0D)
                .add(Attributes.ARMOR,              9.0D)
                .add(Attributes.ARMOR_TOUGHNESS,    0.9D);
    }

    /* ─────────────── Animation helpers ─────────────── */
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout-- <= 0) {
            this.idleAnimationTimeout = 80 + this.random.nextInt(40); // 4‑6 s delay
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    private void setupAttackAnimation() {
        if (this.attackAnimationTimeout-- <= 0) {
            if (this.swinging && !this.attackAnimationState.isStarted()) {
                this.attackAnimationState.start(this.tickCount);
                this.attackAnimationTimeout = 45; // 2.25 seconds cooldown
            }
        }
    }


    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            setupAnimationStates();
            setupAttackAnimation();
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (!this.level().isClientSide) {
            float dropChance = 0.05F; // 5% drop chance
            if (this.random.nextFloat() < dropChance) {
                this.spawnAtLocation(ModItems.LUNAR_EDGE.get());
            }
            if (this.level() instanceof ServerLevel serverLevel) {
                for (LunarHerobrineEntity herobrine : serverLevel.getEntitiesOfClass(LunarHerobrineEntity.class, this.getBoundingBox().inflate(50))) {
                    herobrine.onMinionKilled();
                }
            }
        }
    }

    public static boolean canSpawn( EntityType<LunarSentinelEntity> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolid() &&
                level.getRawBrightness(pos, 0) < 8; // darkness required for MONSTER spawn
    }


    /* ─────────────── Sounds ─────────────── */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound( DamageSource damageSource ) {
        return SoundEvents.WARDEN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }
}

