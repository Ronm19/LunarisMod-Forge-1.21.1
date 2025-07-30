package net.ronm19.lunarismod.entity.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.item.ModItems;

import java.util.List;
import java.util.stream.Collectors;

public class PackRitualHandler {

    private static final double PACK_RADIUS = 12.0;

    public static boolean canPerformRitual(ServerPlayer player, BlockPos ritualPos) {
        Level level = player.level();

        return isFullMoon(level) &&
                hasMoonstoneItem(player) &&
                isNightTime(level) &&
                isStandingAtRitualSite(player, ritualPos) &&
                getNearbyBondedWolves(player, ritualPos).size() >= 3;
    }

    public static boolean performRitual( ServerPlayer player, BlockPos ritualPos) {
        ServerLevel level = player.serverLevel(); // FIXED
        List<LunarWolfEntity> bondedWolves = getNearbyBondedWolves(player, ritualPos);

        if (bondedWolves.isEmpty()) return false;

        LunarWolfEntity alpha = bondedWolves.getFirst();
        alpha.promoteToAlpha(); // Ensure this method exists

        for (LunarWolfEntity wolf : bondedWolves) {
            wolf.addPackMemoryBoost(); // Ensure this method exists
            wolf.level().broadcastEntityEvent(wolf, (byte) 18);
        }

        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
        level.levelEvent(null, 2003, ritualPos, 0);
        return false;
    }

    public static boolean isFullMoon( Level level ) {
        return level.getMoonBrightness() >= 1.0F;
    }

    private static boolean isNightTime(Level level) {
        long time = level.getDayTime() % 24000;
        return time >= 13000 && time <= 23000;
    }

    private static boolean hasMoonstoneItem(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModItems.MOONSTONE.get()) return true;
        }
        return false;
    }

    private static boolean isStandingAtRitualSite(Player player, BlockPos ritualPos) {
        return player.blockPosition().closerThan(ritualPos, 2.0);
    }

    private static List<LunarWolfEntity> getNearbyBondedWolves(Player player, BlockPos center) {
        AABB box = new AABB(center).inflate(PACK_RADIUS);
        return player.level().getEntitiesOfClass(LunarWolfEntity.class, box).stream()
                .filter(wolf -> wolf.isAlive() && wolf.isBondTo(player)) // Ensure method exists
                .collect(Collectors.toList());
    }

    public static LunarWolfEntity getNearbyWolf(Level level, BlockPos pos, double radius) {
        return level.getEntitiesOfClass(LunarWolfEntity.class,
                new AABB(pos).inflate(radius),
                wolf -> true).stream().findFirst().orElse(null);

    }
        private static final long RITUAL_COOLDOWN_TICKS = 20L * 60 * 30; // 30 minutes

        public static boolean attemptPackRitual(ServerLevel level, LunarWolfEntity wolf, Player player) {
            long currentTime = level.getGameTime();
            long lastTime = wolf.getLastRitualTimestamp();

            if (lastTime != -1 && currentTime - lastTime < RITUAL_COOLDOWN_TICKS) {
                long remainingTicks = RITUAL_COOLDOWN_TICKS - (currentTime - lastTime);
                long minutes = remainingTicks / 1200;
                long seconds = (remainingTicks % 1200) / 20;
                player.displayClientMessage(Component.literal("The Lunar Wolf is still recovering. Try again in " +
                        minutes + "m " + seconds + "s."), true);
                return false;
            }

            // ✅ TODO: Add checks here (e.g., full moon, pack proximity, etc.)

            wolf.promoteToAlpha(); // Custom promotion method
            wolf.setLastRitualTimestamp(currentTime);

            player.displayClientMessage(Component.literal("The ritual succeeded!"), true);

            return true;
        }
    }