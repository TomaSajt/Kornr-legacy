package com.tomasajt.kornr.keybindings;

import com.tomasajt.kornr.util.Toggleable;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingToggler extends Toggleable {

	
	private KeyBinding keyBinding;

	public KeyBindingToggler(KeyBinding keyBinding) {
		this.keyBinding = keyBinding;
	}

	@Override
	public void on() {
		super.on();
		KeybindHolder.keyBindings.add(keyBinding);
	}

	@Override
	public void off() {
		super.off();
		KeybindHolder.keyBindings.remove(keyBinding);
	}
}
