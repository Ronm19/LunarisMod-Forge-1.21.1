package net.ronm19.lunarismod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_LUNARIS = "key.category.lunarismod.lunaris";
    public static final String KEY_DESCEND = "key.lunarismod.descend";
    public static final String KEY_ASCEND = "key.lunarismod.ascend";
    public static final String KEY_TOGGLE_ABILITY = "key.lunarismod.toggle_ability";

    public static final KeyMapping DESCENDING_KEY = new KeyMapping(KEY_DESCEND, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, KEY_CATEGORY_LUNARIS);

}
