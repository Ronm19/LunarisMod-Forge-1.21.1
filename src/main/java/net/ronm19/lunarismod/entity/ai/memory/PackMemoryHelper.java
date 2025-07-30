package net.ronm19.lunarismod.entity.ai.memory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.ronm19.lunarismod.entity.ai.goal.PatrolWithMemoryGoal;
import net.ronm19.lunarismod.entity.custom.LunarWolfEntity;
import net.ronm19.lunarismod.entity.ai.GoalUtils;

import java.util.List;

public class PackMemoryHelper {

    public static void shareIntrusion(Mob sender, BlockPos intrusionPos) {
        if (sender.level().isClientSide()) return;

        List<Mob> packMates = sender.level().getEntitiesOfClass(
                Mob.class,
                sender.getBoundingBox().inflate(20),
                mob -> mob instanceof LunarWolfEntity && mob != sender
        );

        for (Mob mate : packMates) {
            if (!(mate instanceof PathfinderMob wolf)) continue;

            List<Goal> goals = GoalUtils.getAvailableGoals(wolf.goalSelector);
            if (goals == null) continue;

            for (Goal goal : goals) {
                if (goal instanceof PatrolWithMemoryGoal memoryGoal) {
                    memoryGoal.rememberIntrusion(intrusionPos);
                }
            }
        }
    }

    public static void shareScentTrail(Mob sender, ScentTrail trail) {
        if (sender.level().isClientSide()) return;

        List<Mob> packMates = sender.level().getEntitiesOfClass(
                Mob.class,
                sender.getBoundingBox().inflate(20),
                mob -> mob instanceof LunarWolfEntity && mob != sender
        );

        for (Mob mate : packMates) {
            // TODO: Hook up trail merging logic when LunarWolfEntity exposes getScentTrail()
            // Example:
            // if (mate instanceof LunarWolfEntity lunarWolf) {
            //     lunarWolf.getScentTrail().merge(trail);
            // }
        }
    }
}