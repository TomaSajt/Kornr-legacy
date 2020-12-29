package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.util.IToggleable;

public class ToggleableButton extends KornrButton {

	private boolean isOn;
	private IToggleable toggleable;

	public ToggleableButton(int gridX, int gridY, String title, IToggleable toggleable) {
		super(gridX, gridY, title);
		this.toggleable = toggleable;
		this.setAlpha(0.5f);
	}

	@Override
	public void onPress() {
		isOn = !isOn;
		if (isOn) {
			this.setAlpha(1f);
			toggleable.on();
		} else {
			this.setAlpha(0.5f);
			toggleable.off();
		}
	}

}
