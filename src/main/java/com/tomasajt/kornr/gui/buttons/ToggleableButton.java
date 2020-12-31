package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.util.Toggleable;

public class ToggleableButton extends KornrButton {

	private Toggleable toggleable;

	public ToggleableButton(int gridX, int gridY, String title, Toggleable toggleable) {
		super(gridX, gridY, title);
		this.toggleable = toggleable;
		updateState();
	}

	@Override
	public void onPress() {
		toggleable.toggle();
		updateState();
	}
	
	private void updateState() {
		if (toggleable.isOn()) {
			this.setAlpha(1f);
		} else {
			this.setAlpha(0.5f);
		}
	}

}
