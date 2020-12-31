package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.gui.KornrSettingsScreen;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class KornrButton extends Button {

	private int gridX;
	private int gridY;

	public KornrButton(int gridX, int gridY, String title, IPressable pressable) {
		super(0, 0, KornrSettingsScreen.buttonWidth, KornrSettingsScreen.buttonHeight, new StringTextComponent(title),
				pressable);
		this.gridX = gridX;
		this.gridY = gridY;
	}
	
	public KornrButton(int gridX, int gridY, String title, IPressable pressable, ITooltip tooltip) {
		super(0, 0, KornrSettingsScreen.buttonWidth, KornrSettingsScreen.buttonHeight, new StringTextComponent(title),
				pressable, tooltip);
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
