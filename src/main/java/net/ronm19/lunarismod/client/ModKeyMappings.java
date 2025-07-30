package net.ronm19.lunarismod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final KeyMapping DESCEND_KEY = new KeyMapping("key.lunarismod.descend", // Translation key
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.movement"
    );


    public static void registerKeyMappings( RegisterKeyMappingsEvent event) {
        event.register(DESCEND_KEY);
    }
}
