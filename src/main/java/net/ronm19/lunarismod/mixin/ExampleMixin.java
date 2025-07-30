package net.ronm19.lunarismod.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class ExampleMixin {

    @Inject(method = "jumpFromGround", at = @At("HEAD"))
    private void onPlayerJump(CallbackInfo ci) {
        System.out.println("[LunarisMod] Player jumped!");
    }
}
