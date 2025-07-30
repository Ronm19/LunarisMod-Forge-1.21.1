package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.ronm19.lunarismod.entity.ai.goal.IntelligentMobGoal;
import net.ronm19.lunarismod.entity.ai.goal.LunarEndermanTeleportGoal;

public class LunarEndermanEntity extends Monster  {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;


    public LunarEndermanEntity( EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setNoAi(false); // inside constructor or tick if needed
        this.xpReward = 10;

    }

    public static AttributeSupplier.Builder createAttributes() {
        return EnderMan.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2F)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.STEP_HEIGHT, 4.6F)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 0.1F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new LunarEndermanTeleportGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // PRIORITY 2
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TamableAnimal.class, true));
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

    public static boolean checkMonsterSpawnRules(
            EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom
    ) {
        return pLevel.getDifficulty() != Difficulty.PEACEFUL
                && (MobSpawnType.ignoresLightRequirements(pSpawnType) || isDarkEnoughToSpawn(pLevel, pPos, pRandom))
                && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    public boolean customTeleport(double radius) {
        if (!this.level().isClientSide() && this.isAlive()) {
            for (int attempt = 0; attempt < 16; attempt++) { // Try up to 16 times
                double targetX = this.getX() + (this.random.nextDouble() - 0.5D) * radius * 2;
                double targetY = this.getY() + (this.random.nextInt((int) radius) - radius / 2);
                double targetZ = this.getZ() + (this.random.nextDouble() - 0.5D) * radius * 2;

                BlockPos targetPos = new BlockPos((int) targetX, (int) targetY, (int) targetZ);
                if (this.canTeleportTo(targetPos)) {
                    Vec3 oldPos = this.position();

                    boolean teleported = this.randomTeleport(targetX, targetY, targetZ, true);

                    if (teleported) {
                        this.level().gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(this));
                        if (!this.isSilent()) {
                            this.level().playSound(null, oldPos.x, oldPos.y, oldPos.z, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        }
                        return true;
                    }
                }
            }
            return false; // Failed to teleport
        }
        return false;
    }

    private boolean canTeleportTo(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        boolean solid = state.blocksMotion();
        boolean water = state.getFluidState().is(FluidTags.WATER);
        boolean airAbove = this.level().getBlockState(pos.above()).isAir();

        return solid && !water && airAbove;
    }

    public boolean teleportTowards(Entity target) {
        if (target == null) return false;

        // Direction vector from Enderman to target
        Vec3 direction = target.position().subtract(this.position()).normalize();

        double teleportDistance = 8.0D; // How far away from target it tries to land

        double targetX = this.getX() + (this.random.nextDouble() - 0.5D) * 4.0D - direction.x * teleportDistance;
        double targetY = this.getY() + (double)(this.random.nextInt(8) - 4);
        double targetZ = this.getZ() + (this.random.nextDouble() - 0.5D) * 4.0D - direction.z * teleportDistance;

        return this.attemptTeleport(targetX, targetY, targetZ);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Play hurt sound
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_HURT, SoundSource.HOSTILE, 1.0F, 1.0F);

        // 50% chance to teleport away when hit
        if (!this.level().isClientSide && this.random.nextFloat() < 0.5F) {
            double targetX = this.getX() + (this.random.nextDouble() - 0.5D) * 16.0;
            double targetY = this.getY() + (this.random.nextInt(16) - 8);
            double targetZ = this.getZ() + (this.random.nextDouble() - 0.5D) * 16.0;

            this.attemptTeleport(targetX, targetY, targetZ);
        }

        // Set attacker as target
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity livingAttacker && this.canAttack(livingAttacker)) {
            this.setTarget(livingAttacker);
        }

        return super.hurt(source, amount);
    }


    public boolean attemptTeleport(double targetX, double targetY, double targetZ) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(targetX, targetY, targetZ);

        // Move down until we find a solid block
        while (mutablePos.getY() > this.level().getMinBuildHeight() &&
                !this.level().getBlockState(mutablePos).blocksMotion()) {
            mutablePos.move(Direction.DOWN);
        }

        BlockState blockState = this.level().getBlockState(mutablePos);

        boolean solidGround = blockState.blocksMotion();
        boolean isWater = blockState.getFluidState().is(FluidTags.WATER);

        if (solidGround && !isWater) {
            // Fire Forge Teleport Event
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(this, targetX, targetY, targetZ);
            if (event.isCanceled()) return false;

            Vec3 previousPos = this.position();

            // Perform teleportation
            if (this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                this.level().gameEvent(GameEvent.TELEPORT, previousPos, GameEvent.Context.of(this));

                // Play Teleport Sound
                if (!this.isSilent()) {
                    this.level().playSound(null, previousPos.x, previousPos.y, previousPos.z,
                            SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }

                return true;
            }
        }

        return false;
    }



    // Optionally override sound events
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDERMAN_DEATH;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.SOUL_SAND_STEP, 0.15F, 1.0F);
    }

    // Optional: Save/load animation state (not necessary unless you're tracking something persistent)
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
}
