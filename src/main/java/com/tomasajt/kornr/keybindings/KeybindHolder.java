package com.tomasajt.kornr.keybindings;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class KeybindHolder {

	public static Set<KeyBinding> keyBindings = new HashSet<KeyBinding>();

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			keyBindings.forEach((keyBinding) -> keyBinding.setPressed(true));
		}
	}
}
