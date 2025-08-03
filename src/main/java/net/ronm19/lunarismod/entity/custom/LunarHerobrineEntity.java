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
import net.ronm19.lunarismod.event.LunarEventManager;
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
    private int musicTimer = 0;
    private final int MUSIC_DURATION = 1800;

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int teleportCooldown = 0;
    private int minionKills = 0;

    public LunarHerobrineEntity( EntityType<? extends LunarHerobrineEntity> type, Level level ) {
        super(type, level);
        this.setPersistenceRequired();
        equipDefaultGear();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new FloatGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            updateAnimation();
        }
    }

    private void updateAnimation() {
        if (idleAnimationTimeout <= 0) {
            idleAnimationTimeout = 80;
            idleAnimationState.start(this.tickCount);
        } else {
            idleAnimationTimeout--;
        }
    }

    private int slamCooldown = 0;
    private int fangsCooldown = 0;

    @Override
    public void aiStep() {
        super.aiStep();
        bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        if (!this.level().isClientSide) {
            updateBossMusic();

            // Teleportation logic
            if (--teleportCooldown <= 0 && this.getTarget() != null) {
                teleportBehindTarget();
                teleportCooldown = 120;
            }

            // Phase Checks
            double healthPercent = this.getHealth() / this.getMaxHealth();
            if (healthPercent < 0.6 && healthPercent >= 0.2) applyPhase2Effects();
            if (healthPercent < 0.2) applyPhase3Effects();

            // Ground Slam Cooldown
            if (slamCooldown > 0) slamCooldown--;
            if (slamCooldown <= 0) {
                performGroundSlamAOE();
            }

            // Fangs Cooldown
            if (fangsCooldown > 0) fangsCooldown--;
            if (fangsCooldown <= 0) {
                summonEvokerFangs();
            }

            // Melee Combo - perform only if close
            LivingEntity target = this.getTarget();
            if (target != null) {
                this.getNavigation().moveTo(target, 1.0);
                if (this.distanceTo(target) < 3.5) {
                    performMeleeCombo();
                }
            }
        }
    }


    private int meleeComboStep = 0;
    private int meleeComboCooldown = 0;

    private void performMeleeCombo() {
        if (meleeComboCooldown > 0) {
            meleeComboCooldown--;
            return;
        }

        LivingEntity target = this.getTarget();
        if (target != null && this.distanceTo(target) < 3.5) {
            this.swing(InteractionHand.MAIN_HAND);
            this.doHurtTarget(target);
            meleeComboStep++;
            meleeComboCooldown = 10; // short delay between hits

            if (meleeComboStep >= 3) {
                meleeComboStep = 0; // reset combo after 3 hits
                meleeComboCooldown = 20; // short pause before new combo
            }
        } else {
            meleeComboStep = 0; // reset combo if target out of range
        }
    }



    private void performGroundSlamAOE() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        slamCooldown = 100; // Cooldown 5 seconds (100 ticks)

        double radius = 4.0;
        serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.1, this.getZ(),
                4, radius / 2, 0.5, radius / 2, 0.03); // Reduced particles

        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(radius), e -> e != this && e.isAlive());

        DamageSource source = createMobAttackDamageSource();
        double healthPercent = this.getHealth() / this.getMaxHealth();
        double knockbackStrength = (healthPercent > 0.6) ? 0.8 : (healthPercent > 0.2 ? 0.3 : 0.0);

        for (LivingEntity entity : entities) {
            entity.hurt(source, 8.0F);
            if (knockbackStrength > 0) {
                Vec3 dir = entity.position().subtract(this.position()).normalize();
                entity.push(dir.x * knockbackStrength, 0.4, dir.z * knockbackStrength);
            }
        }
    }




    private DamageSource createMobAttackDamageSource() {
        return new DamageSource(
                this.level().registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(DamageTypes.MOB_ATTACK),
                this
        );
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float maxDamage = 8.0F;
        float finalDamage = Math.min(amount, maxDamage);

        Entity sourceEntity = source.getEntity();
        if (sourceEntity instanceof Player sourcePlayer) {
            if (sourcePlayer.isBlocking()) {
                sourcePlayer.disableShield();
                level().playSound(null, sourcePlayer.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

        if (phase2Started && sourceEntity instanceof Player player) {
            float reflected = finalDamage * 0.25F;
            player.hurt(source, reflected);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
        }

        return super.hurt(source, finalDamage);
    }

    private void breakPlayerShield(Player player) {
        if (player == null) return;

        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() instanceof ShieldItem) {
            player.getCooldowns().addCooldown(offhand.getItem(), 30);

            level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY() + 1, player.getZ(),
                        10, 0.3, 0.3, 0.3, 0.02);
            }
        }
    }

    private void updateBossMusic() {
        musicTimer--;
        for (ServerPlayer player : bossEvent.getPlayers()) {
            double dist = this.distanceToSqr(player);
            boolean inRange = dist <= 64 * 64;

            if (inRange) {
                stopAllMusic(player); // ensure background music is muted

                if (!musicStarted) {
                    playBossMusic(player);
                    musicTimer = MUSIC_DURATION;
                    musicStarted = true;
                } else if (musicTimer <= 0) {
                    playBossMusic(player);
                    musicTimer = MUSIC_DURATION;
                }
            } else {
                stopAllMusic(player); // stops boss music
                musicStarted = false; // allows it to restart if re-entering range
            }
        }
    }


    private void applyPhase2Effects() {
        if (!phase2Started) {
            summonLunarSentinels();
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 1));
            this.level().playSound(null, this.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1.0F, 1.0F);
            phase2Started = true;
        }

        if (this.tickCount % 60 == 0) {
            for (Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10))) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
            }
        }

        if (this.getHealth() < this.getMaxHealth() && this.tickCount % 100 == 0) {
            this.heal(4.0F);
            this.level().playSound(null, this.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.HOSTILE, 0.5F, 1.0F);
        }
    }

    private void applyPhase3Effects() {
        if (!phase3Started) {
            summonZombieKings();
            this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 4));
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1));
            this.level().playSound(null, this.blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.HOSTILE, 1.2F, 0.8F);
            phase3Started = true;
        }
    }

    private void summonLunarSentinels() {
        for (int i = 0; i < 2; i++) {
            LunarSentinelEntity sentinel = new LunarSentinelEntity(ModEntities.LUNARSENTINEL.get(), this.level());
            sentinel.setPos(this.getX() + randomOffset(), this.getY(), this.getZ() + randomOffset());
            sentinel.setPersistenceRequired();
            sentinel.setCustomName(Component.literal("Herobrine's Sentinel"));
            this.level().addFreshEntity(sentinel);
        }
    }

    private void summonZombieKings() {
        for (int i = 0; i < 2; i++) {
            LunarZombieKingEntity king = new LunarZombieKingEntity(ModEntities.LUNAR_ZOMBIE_KING.get(), this.level());
            king.setPos(this.getX() + randomOffset(), this.getY(), this.getZ() + randomOffset());
            king.setPersistenceRequired();
            king.setCustomName(Component.literal("Herobrine's Champion"));
            this.level().addFreshEntity(king);
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

    private void teleportBehindTarget() {
        LivingEntity target = this.getTarget();
        if (!(target instanceof Player player)) return;

        Vec3 behind = player.position().subtract(player.getLookAngle().normalize().scale(4.0));
        BlockPos pos = BlockPos.containing(behind);

        if (isSafeTeleportPosition(pos)) {
            playTeleportParticles();
            this.teleportTo(behind.x, behind.y, behind.z);
            level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
    }

    private boolean isSafeTeleportPosition(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        BlockState above = this.level().getBlockState(pos.above());
        BlockState below = this.level().getBlockState(pos.below());
        return state.isAir() && above.isAir() && below.canOcclude();
    }

    private void playTeleportParticles() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(),
                    20, 0.5, 0.5, 0.5, 0.1);
        }
    }

    private double randomOffset() {
        return (this.random.nextDouble() * 6.0) - 3.0;
    }

    private void summonEvokerFangs() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        fangsCooldown = 200; // 10 sec cooldown

        List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10));
        int fangsSpawned = 0;

        for (Player player : players) {
            if (fangsSpawned >= 3) break; // Max 3 fangs per summon

            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            EvokerFangs fangs = new EvokerFangs(this.level(), x, y, z, 0.0F, 0, this);
            this.level().addFreshEntity(fangs);

            serverLevel.sendParticles(ParticleTypes.CRIT, x, y + 0.5, z, 5, 0.2, 0.1, 0.2, 0.01);
            fangsSpawned++;
        }

        this.level().playSound(null, this.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 0.6F, 1.0F);
    }



    private void stopAllMusic(ServerPlayer player) {
        if (player != null && player.connection != null) {
            player.connection.send(new ClientboundStopSoundPacket(null, SoundSource.MUSIC));
        }
    }

    private void playBossMusic(ServerPlayer player) {
        if (player != null && player.connection != null && ModSounds.LUNAR_HEROBRINE_BOSS_MUSIC.getHolder().isPresent()) {
            player.connection.send(new ClientboundSoundPacket(
                    ModSounds.LUNAR_HEROBRINE_BOSS_MUSIC.getHolder().get(),
                    SoundSource.MUSIC, player.getX(), player.getY(), player.getZ(), 5.0F, 1.0F, 0));
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level().isClientSide) {
            for (ServerPlayer player : bossEvent.getPlayers()) {
                stopAllMusic(player);
            }
        }
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

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }

    public static void trySummonLunarHerobrine(Level level, BlockPos pos, Player player) {
        if (LunarEventManager.isBloodMoon(level) && player.getMainHandItem().is(ModItems.SOUL_TOME.get())) {
            LunarHerobrineEntity herobrine = new LunarHerobrineEntity(ModEntities.LUNAR_HEROBRINE.get(), level);
            herobrine.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
            level.addFreshEntity(herobrine);
            level.playSound(null, pos, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.5F, 0.5F);
        }
    }
}