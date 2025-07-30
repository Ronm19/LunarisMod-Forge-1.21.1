package net.ronm19.lunarismod.entity.ai.interfaces;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public interface AquaticTamableHorseMount {

    boolean isSaddled();

    boolean canBeControlledByRider();

    boolean isFluidWalkable();

    boolean canStandOnFluid( Fluid fluid );

    boolean isOnWater();


    boolean canBeSaddled();
}
