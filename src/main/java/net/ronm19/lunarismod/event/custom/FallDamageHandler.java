package net.ronm19.lunarismod.event.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ronm19.lunarismod.LunarisMod;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = LunarisMod.MOD_ID)
public class FallDamageHandler {

    private static final Map<UUID, Integer> fallImmunityMap = new WeakHashMap<>();

    /** Call this when player dismounts the Void Phantom */
    public static void grantImmunity(ServerPlayer player) {
        fallImmunityMap.put(player.getUUID(), 120); // - More fair and highly depends on from where they're dismounting from
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            UUID uuid = player.getUUID();
            if (fallImmunityMap.containsKey(uuid)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            fallImmunityMap.replaceAll((uuid, ticks) -> ticks > 0 ? ticks - 1 : 0);
            fallImmunityMap.entrySet().removeIf(entry -> entry.getValue() <= 0);
        }
    }
}