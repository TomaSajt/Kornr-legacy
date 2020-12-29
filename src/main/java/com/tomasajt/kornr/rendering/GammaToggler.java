package com.tomasajt.kornr.rendering;

import com.tomasajt.kornr.util.IToggleable;

import net.minecraft.client.Minecraft;

public class GammaToggler implements IToggleable {

	Minecraft mc = Minecraft.getInstance();
	
	@Override
	public void on() {
		if (mc.gameSettings.gamma < 100) {
			mc.gameSettings.gamma = 100;
		}
	}

	@Override
	public void off() {
		mc.gameSettings.gamma = 1;
	}

}
