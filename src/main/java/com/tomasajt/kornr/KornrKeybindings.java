package com.tomasajt.kornr;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.tomasajt.kornr.gui.KornrSettingsScreen;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Bus.MOD)
public class KornrKeybindings {
	public static List<KeyBinding> keyBindings = new ArrayList<>();
	public static KeyBinding keyBindingOpenSettings = new KeyBinding("key.kornr.openMenu",
			KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_MULTIPLY, "key.categories.kornr");
	static {
		keyBindings.add(keyBindingOpenSettings);
	}
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		for (KeyBinding keyBinding : keyBindings) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
		KornrSettingsScreen.instance.loadSettings();
	}
}
