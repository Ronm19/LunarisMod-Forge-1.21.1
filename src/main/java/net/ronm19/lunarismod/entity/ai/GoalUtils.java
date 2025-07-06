package net.ronm19.lunarismod.entity.ai;  // or your chosen package

import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.Goal;

import java.lang.reflect.Field;
import java.util.List;

public class GoalUtils {

    private static Field availableGoalsField;

    static {
        try {
            // The field name "availableGoals" might differ by version or mappings,
            // If this errors, you may need to check your mappings for the right name.
            availableGoalsField = GoalSelector.class.getDeclaredField("availableGoals");
            availableGoalsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Goal> getAvailableGoals(GoalSelector goalSelector) {
        try {
            return (List<Goal>) availableGoalsField.get(goalSelector);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
