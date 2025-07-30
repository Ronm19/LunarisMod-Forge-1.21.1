package net.ronm19.lunarismod.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.ronm19.lunarismod.entity.ModEntities;
import net.ronm19.lunarismod.event.LunarEventManager;
import net.ronm19.lunarismod.item.ModItems;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.world.item.enchantment.Enchantments.SHARPNESS;
import static net.ronm19.lunarismod.event.LunarEventManager.isBloodMoon;

public class LunarHerobrineEntity extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;

    public LunarHerobrineEntity( EntityType<? extends LunarHerobrineEntity> type, Level level ) {
        super(type, level);
        this.setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.6, true));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.2));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
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

        if (!this.level().isClientSide) {
            if (this.level().isNight() && this.tickCount % 80 == 0) {
                teleportBehindNearestPlayer();
            }

            applyFearAura();
        }
    }

    private void teleportBehindNearestPlayer() {
        Player target = this.level().getNearestPlayer(this, 40.0);
        if (target != null && !this.level().isClientSide) {
            Vec3 look = target.getLookAngle().normalize();
            Vec3 behind = target.position().subtract(look.scale(4.0));

            BlockPos safePos = new BlockPos((int) behind.x, (int) behind.y, (int) behind.z);
            BlockState blockState = this.level().getBlockState(safePos);

            // Safe if block is air OR non-solid (e.g., grass, replaceable blocks)
            boolean isSafe = blockState.isAir()
                    || (!blockState.isCollisionShapeFullBlock(this.level(), safePos)
                    && this.level().getBlockState(safePos.above()).isAir());

            if (isSafe) {
                this.teleportTo(behind.x, behind.y, behind.z);
                this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
                ((ServerLevel) this.level()).sendParticles(
                        ParticleTypes.PORTAL,
                        this.getX(), this.getY(), this.getZ(),
                        10, 0.5, 0.5, 0.5, 0.1
                );
            } else {
                System.out.println("Teleport blocked — unsafe location!");
            }
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    protected void equipDefaultGear() {
        ItemStack sword = new ItemStack(ModItems.LUNAR_HEROBRINE_SWORD.get());

        this.setItemSlot(EquipmentSlot.MAINHAND, sword);
    }


public static void trySummonLunarHerobrine( Level level, BlockPos pos, Player player) {
        if (LunarEventManager.isBloodMoon(level) && player.getMainHandItem().is(ModItems.SOUL_TOME.get())) {
            LunarHerobrineEntity herobrine = new LunarHerobrineEntity(ModEntities.LUNAR_HEROBRINE.get(), level);
            herobrine.setPos(pos.getX(), pos.getY() + 1, pos.getZ());
            level.addFreshEntity(herobrine);
            level.playSound(null, pos, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.5F, 0.5F);
        }
    }

    private void applyFearAura() {
        for (Player nearby : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(8.0))) {
            nearby.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, true));
        }
    }

    @Override
    public boolean hurt( DamageSource source, float amount ) {
        if (!this.level().isClientSide && source.getEntity() instanceof LivingEntity) {
            Vec3 offset = this.position().add(this.level().random.nextInt(5) - 2, 0, this.level().random.nextInt(5) - 2);
            this.teleportTo(offset.x, offset.y, offset.z);
            this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
        return super.hurt(source, amount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0)
                .add(Attributes.MOVEMENT_SPEED, 0.45)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
    }
}