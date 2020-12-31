package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.util.Toggleable;

public class ToggleableKornrButton extends KornrButton {

	private Toggleable toggleable;

	public ToggleableKornrButton(int gridX, int gridY, String title, Toggleable toggleable) {
		super(gridX, gridY, title, (button) -> {
		});
		this.toggleable = toggleable;
		updateState();
	}
	public ToggleableKornrButton(int gridX, int gridY, String title, Toggleable toggleable, String tooltip) {
		super(gridX, gridY, title, (button) -> {
		}, tooltip);
		this.toggleable = toggleable;
		updateState();
	}
	

	@Override
	public void onPress() {
		toggleable.toggle();
		updateState();
	}

	public void updateState() {
		if (toggleable.isOn()) {
			this.setAlpha(1f);
		} else {
			this.setAlpha(0.5f);
		}
	}

}
