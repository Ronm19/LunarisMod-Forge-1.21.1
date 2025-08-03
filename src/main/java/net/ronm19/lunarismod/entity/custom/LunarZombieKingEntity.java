package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.ronm19.lunarismod.item.ModItems;

import javax.annotation.Nullable;
import java.util.List;

public class LunarZombieKingEntity extends Monster {
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Lunar Zombie King"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.NOTCHED_10
    );

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;


    public LunarZombieKingEntity(EntityType<? extends LunarZombieKingEntity> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.xpReward = 50;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1D, false));
        this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 0.9D));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
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

        @Override
    public void aiStep() {
        super.aiStep();

        // Sync boss bar health
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        // Soul fire aura
        if (level().isClientSide && tickCount % 10 == 0) {
            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    getX() + random.nextDouble() - 0.5,
                    getY() + 1.0,
                    getZ() + random.nextDouble() - 0.5,
                    0, 0.05, 0);
        }

        // Aura debuff: slows and weakens nearby players
        if (!level().isClientSide && tickCount % 40 == 0) {
            List<Player> players = level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(6.0));
            for (Player p : players) {
                p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0));
                p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            }
        }

        // Full moon buff: enraged stats
        if (!level().isClientSide) {
            boolean fullMoon = level().getMoonPhase() == 0;
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(fullMoon ? 25.0D : 15.0D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(fullMoon ? 0.3D : 0.23D);
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        if (!level.isClientSide) {
            this.spawnAtLocation(ModItems.LUNAR_HEROBRINE_GEM.get());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }
}
