package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.gui.KornrSettingsScreen;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public abstract class KornrButton extends Button {

	private int gridX;
	private int gridY;

	public KornrButton(int gridX, int gridY, String title) {
		super(0, 0, KornrSettingsScreen.buttonWidth, KornrSettingsScreen.buttonHeight, new StringTextComponent(title),
				(button) -> {
				});
		this.gridX = gridX;
		this.gridY = gridY;
	}

	public int gridX() {
		return gridX;
	}

	public int gridY() {
		return gridY;
	}

}
