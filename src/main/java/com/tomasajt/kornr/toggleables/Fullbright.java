package com.tomasajt.kornr.toggleables;

import com.tomasajt.kornr.util.Toggleable;

import net.minecraft.client.Minecraft;

public class Fullbright extends Toggleable {

	public static final Fullbright instance = new Fullbright();
	Minecraft mc = Minecraft.getInstance();

	@Override
	public void on() {
		super.on();
		if (mc.gameSettings.gamma < 100) {
			mc.gameSettings.gamma = 100;
		}
	}

	@Override
	public void off() {
		super.off();
		mc.gameSettings.gamma = 1;
	}

}
