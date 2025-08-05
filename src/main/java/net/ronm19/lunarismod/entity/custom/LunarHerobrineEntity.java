package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.item.ModItems;
import net.ronm19.lunarismod.sound.ModSounds;

import java.util.List;

public class LunarHerobrineEntity extends Monster {

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.literal("Lunar Herobrine"),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS);


    private boolean phase2Started = false;
    private boolean phase3Started = false;
    private boolean musicStarted = false;

    public final AnimationState idleAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private int teleportCooldown = 0;
    private int slamCooldown = 0;
    private int fangsCooldown = 0;
    private int meleeComboStep = 0;
    private int meleeComboCooldown = 0;

    private int minionKills = 0;


    public LunarHerobrineEntity(EntityType<? extends LunarHerobrineEntity> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        equipDefaultGear(); // Equip sword
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setAlertOthers(LunarKnightEntity.class));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            updateAnimation();
        }
        if (!level().isClientSide) {
            bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            handleBossMusic(); // Fixed music
            if (--teleportCooldown <= 0 && getTarget() != null) {
                teleportBehindTarget();
                teleportCooldown = 120;
            }
            handlePhases();
            if (slamCooldown-- <= 0) performGroundSlamAOE();
            if (fangsCooldown-- <= 0) summonEvokerFangs();
            handleMeleeCombo();
        }
    }

    private void updateAnimation() {
        if (idleAnimationTimeout <= 0) {
            idleAnimationTimeout = 40;
            idleAnimationState.start(this.tickCount);
        } else {
            idleAnimationTimeout--;
        }
    }

    private void handleMeleeCombo() {
        LivingEntity target = getTarget();
        if (target != null && distanceTo(target) < 3.5) {
            if (meleeComboCooldown-- <= 0) {
                swing(InteractionHand.MAIN_HAND);
                doHurtTarget(target);
                meleeComboStep++;
                meleeComboCooldown = meleeComboStep >= 3 ? 20 : 10;
                if (meleeComboStep >= 3) meleeComboStep = 0;
            }
        } else {
            meleeComboStep = 0;
        }
    }

    private void handlePhases() {
        double hpPercent = getHealth() / getMaxHealth();
        if (hpPercent < 0.6 && !phase2Started) activatePhase2();
        if (hpPercent < 0.2 && !phase3Started) activatePhase3();
    }

    private void activatePhase2() {
        summonLunarSentinels();
        addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 1));
        level().playSound(null, blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1F, 1F);
        phase2Started = true;
    }

    private void activatePhase3() {
        summonZombieKings();
        addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 4));
        addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1));
        level().playSound(null, blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.HOSTILE, 1.2F, 0.8F);
        phase3Started = true;
    }

    private void handleBossMusic() {
        for (ServerPlayer player : bossEvent.getPlayers()) {
            double dist = this.distanceToSqr(player);
            if (dist <= 64 * 64) {
                if (!musicStarted && ModSounds.LUNAR_HEROBRINE_BOSS_MUSIC.getHolder().isPresent()) {
                    player.connection.send(new ClientboundSoundPacket(
                            ModSounds.LUNAR_HEROBRINE_BOSS_MUSIC.getHolder().get(),
                            SoundSource.RECORDS, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F, 0));
                    musicStarted = true;
                }
            } else {
                stopAllMusic(player);
                musicStarted = false;
            }
        }
    }

    private void stopAllMusic(ServerPlayer player) {
        if (player != null && player.connection != null) {
            player.connection.send(new ClientboundStopSoundPacket(null, SoundSource.RECORDS));
        }
    }

    private void teleportBehindTarget() {
        LivingEntity target = getTarget();
        if (!(target instanceof Player player)) return;
        Vec3 behind = player.position().subtract(player.getLookAngle().normalize().scale(4.0));
        BlockPos pos = BlockPos.containing(behind);
        if (isSafeTeleportPosition(pos)) {
            level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
            teleportTo(behind.x, behind.y, behind.z);
        }
    }

    private boolean isSafeTeleportPosition(BlockPos pos) {
        return level().getBlockState(pos).isAir() && level().getBlockState(pos.above()).isAir();
    }

    private void performGroundSlamAOE() {
        slamCooldown = 100;
        if (!(level() instanceof ServerLevel serverLevel)) return;
        serverLevel.sendParticles(ParticleTypes.SMOKE, getX(), getY(), getZ(), 4, 2.0, 0.5, 2.0, 0.03);
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4), e -> e != this);
        for (LivingEntity e : entities) {
            e.hurt(createMobAttackDamageSource(), 8.0F);
            e.push(e.getX() - getX(), 0.4, e.getZ() - getZ());
        }
    }

    private void summonEvokerFangs() {
        fangsCooldown = 200;
        if (!(level() instanceof ServerLevel serverLevel)) return;
        for (Player player : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(10))) {
            EvokerFangs fangs = new EvokerFangs(level(), player.getX(), player.getY(), player.getZ(), 0.0F, 0, this);
            level().addFreshEntity(fangs);
            serverLevel.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY() + 1, player.getZ(), 5, 0.2, 0.2, 0.2, 0.01);
        }
        level().playSound(null, blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 0.6F, 1.0F);
    }

    private DamageSource createMobAttackDamageSource() {
        return new DamageSource(level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.MOB_ATTACK), this);
    }

    private void summonLunarSentinels() {
        for (int i = 0; i < 2; i++) {
            LunarSentinelEntity sentinel = new LunarSentinelEntity(ModEntities.LUNARSENTINEL.get(), level());
            sentinel.setPos(getX() + randomOffset(), getY(), getZ() + randomOffset());
            level().addFreshEntity(sentinel);
        }
    }

    public void onMinionKilled() {
        minionKills++;
        if (minionKills >= 4) {
            if (this.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                this.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            }
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1));
        }
    }


    private void summonZombieKings() {
        for (int i = 0; i < 2; i++) {
            LunarZombieKingEntity king = new LunarZombieKingEntity(ModEntities.LUNAR_ZOMBIE_KING.get(), level());
            king.setPos(getX() + randomOffset(), getY(), getZ() + randomOffset());
            level().addFreshEntity(king);
        }
    }

    private double randomOffset() {
        return (random.nextDouble() * 6.0) - 3.0;
    }

    private void equipDefaultGear() {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LUNAR_HEROBRINE_SWORD.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.ARMOR, 20.0)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.FOLLOW_RANGE, 50.0);
    }

    @Override public boolean fireImmune() { return true; }
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.WITHER_AMBIENT; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.WITHER_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.WITHER_DEATH; }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
        stopAllMusic(player);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        for (ServerPlayer player : bossEvent.getPlayers()) {
            stopAllMusic(player);
        }
    }
}
