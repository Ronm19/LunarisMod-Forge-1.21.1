package net.ronm19.lunarismod.entity.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.ronm19.lunarismod.entity.ai.goal.FollowKingGoal;
import net.ronm19.lunarismod.entity.ai.goal.HomeToHuskstoneGoal;
import net.ronm19.lunarismod.entity.ai.goal.LunarPackSyncGoal;
import net.ronm19.lunarismod.entity.ai.goal.MoonlightStaggerGoal;

public class LunarZombieEntity extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public LunarZombieEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);

    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D, 3, false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.9D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new HomeToHuskstoneGoal(this));
        this.goalSelector.addGoal(8, new LunarPackSyncGoal(this));
        this.goalSelector.addGoal(9, new MoonlightStaggerGoal(this));
        this.goalSelector.addGoal(10, new FollowKingGoal(this, 0.2D, 40));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(LunarZombieEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
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

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide) {
            if (!level().isNight() && level().canSeeSky(this.blockPosition())) {
                applyDaylightWeakness();
            } else {
                restoreDaylightStats();
            }

            if (isFullMoonNight() && level().canSeeSky(this.blockPosition())) {
                applyMoonMadnessBuff();
                applyCustomGlowColor();     // 🟣 Adds to colored team and enables glow
                setEntityGlowing(true);
            } else {
                removeGlowTeam();           // 🧼 Removes from team and disables glow
                setEntityGlowing(false);
            }
        }
    }

    private void applyCustomGlowColor() {
        if (this.getServer() == null) return;

        var scoreboard = this.getServer().getScoreboard();
        var team = scoreboard.getPlayerTeam("moonmadness");

        if (team == null) {
            team = scoreboard.addPlayerTeam("moonmadness");
            team.setDisplayName(Component.literal("Moon Madness"));
            team.setColor(ChatFormatting.LIGHT_PURPLE); // Custom glow color
        }

        String playerId = this.getUUID().toString(); // Unique identifier

        var currentTeam = scoreboard.getPlayersTeam(playerId);
        if (currentTeam == null || !currentTeam.equals(team)) {
            scoreboard.addPlayerToTeam(playerId, team);
        }

        this.setGlowingTag(true);
    }

    private void removeGlowTeam() {
        if (this.getServer() == null) return;

        String playerId = this.getUUID().toString();
        var team = this.getServer().getScoreboard().getPlayersTeam(playerId);

        if (team != null && "moonmadness".equals(team.getName())) {
            this.getServer().getScoreboard().removePlayerFromTeam(playerId, team);
        }

        this.setGlowingTag(false);
    }

    private void applyDaylightWeakness() {
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);
    }

    private void restoreDaylightStats() {
        getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    private void applyMoonMadnessBuff() {
        if (!this.hasEffect(MobEffects.DAMAGE_BOOST)) {
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
        }
        if (!this.hasEffect(MobEffects.MOVEMENT_SPEED)) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
        }
    }

    private boolean isFullMoonNight() {
        return level().isNight() && level().getMoonPhase() == 0;
    }

    private void setEntityGlowing(boolean glow) {
        this.setGlowingTag(glow); // True enables glow; false disables it
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound( DamageSource source) {
        return SoundEvents.HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.HUSK_STEP, 0.15F, 1.0F);
    }
}