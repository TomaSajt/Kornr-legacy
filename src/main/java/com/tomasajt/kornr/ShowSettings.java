package com.tomasajt.kornr;

import com.tomasajt.kornr.gui.KornrSettingsScreen;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ShowSettings {
	private static Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (KornrKeybindings.keyBindingOpenSettings.isPressed()) {
			mc.displayGuiScreen(KornrSettingsScreen.instance);
		}
	}
}
