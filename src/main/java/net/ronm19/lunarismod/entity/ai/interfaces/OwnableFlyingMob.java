package net.ronm19.lunarismod.entity.ai.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface OwnableFlyingMob {
    // ✅ Riding Setup
    boolean canBeRiddenInWater( Entity rider );

    boolean canBeRidden( Player player );

    void updatePassengerPosition( Entity passenger );

    double getPassengersRidingOffset();

    boolean canBeControlledByRider();

    Vec3 getRiderPosition();

    boolean canBeLeashed( Player player );

    boolean isTamed();                      // Check if the mob is tamed
    void setTamed(boolean tamed);          // Set tame state

    UUID getOwnerUUID();                   // Needed for saving/loading owner
    void setOwnerUUID(UUID uuid);          // Set the owner's UUID

    LivingEntity getOwner();               // Get the owner entity

    MoveControl getMoveControl();

    boolean isFood( ItemStack stack );

    boolean canBeRidden();

    SpawnGroupData finalizeSpawn( ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag );

    @Nullable SpawnGroupData finalizeSpawn( ServerLevelAccessor level, DifficultyInstance difficulty, Monster reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag );

    void setTame( boolean b );

    boolean changeDimension( ServerLevel targetLevel );

    // ✅ Flying & Movement
    boolean isFlying();

    boolean isOnGround();

    @Nullable SoundEvent getFlapSound();
}
