package com.tomasajt.kornr.keybindings;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybindings {
	public static List<KeyBinding> keyBindings = new ArrayList<>();
	public static KeyBinding keyBindingOpenKornrMenu = new KeyBinding("key.kornr.openMenu",
			KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_MULTIPLY, "key.categories.kornr");
	static {
		keyBindings.add(keyBindingOpenKornrMenu);
	}

}
