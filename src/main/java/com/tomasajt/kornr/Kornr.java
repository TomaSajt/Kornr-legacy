package com.tomasajt.kornr;

import com.tomasajt.kornr.keybindings.Keybindings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
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

	public Kornr() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void sendMessagge(Object message) {
		Minecraft mc = Minecraft.getInstance();
		mc.ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(message.toString()));
	}
	
	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		for (KeyBinding keyBinding : Keybindings.keyBindings) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
}
