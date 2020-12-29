package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.keybindings.KeyBindingToggler;

import net.minecraft.client.settings.KeyBinding;

public class KeyBHolderButton extends ToggleableButton {

	public KeyBHolderButton(int gridX, int gridY, String title, KeyBinding keyBinding) {
		super(gridX, gridY, title, new KeyBindingToggler(keyBinding));
	}
}
