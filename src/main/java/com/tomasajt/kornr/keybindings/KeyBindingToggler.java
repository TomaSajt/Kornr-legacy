package com.tomasajt.kornr.keybindings;

import com.tomasajt.kornr.util.IToggleable;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingToggler implements IToggleable {
	
	private boolean isOn = false;
	private KeyBinding keyBinding;
	public KeyBindingToggler(KeyBinding keyBinding) {
		this.keyBinding = keyBinding;
	}
	
	public void on() {
		KeybindHolder.keyBindings.add(keyBinding);
	}

	public void off() {
		KeybindHolder.keyBindings.remove(keyBinding);
	}
	
	public boolean isOn() {
		return isOn;
	}
}
