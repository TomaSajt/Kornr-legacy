package com.tomasajt.kornr;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(Kornr.MOD_ID)
@EventBusSubscriber(bus = Bus.MOD)
public class Kornr {
	public static final String MOD_ID = "kornr";
	public static List<KeyBinding> keyBindings = new ArrayList<>();
	public static KeyBinding keyBindingOpenKornrMenu = new KeyBinding("key.kornr.openMenu",
			KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_MULTIPLY, "key.categories.kornr");
	static {
		keyBindings.add(keyBindingOpenKornrMenu);
	}
	
	public Kornr() {
		Fullbright.instance.on();
	}

	public static void sendMessagge(Object message) {
		Minecraft mc = Minecraft.getInstance();
		mc.ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(message.toString()));
	}
	
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		for (KeyBinding keyBinding : keyBindings) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
}
