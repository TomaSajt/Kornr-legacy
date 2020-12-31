package com.tomasajt.kornr.toggleables;

import com.tomasajt.kornr.util.KeyBindingToggler;

import net.minecraft.client.Minecraft;

public class AutoSprint {

	private static Minecraft mc = Minecraft.getInstance();
	public static final KeyBindingToggler instance = new KeyBindingToggler(mc.gameSettings.keyBindSprint);
}
