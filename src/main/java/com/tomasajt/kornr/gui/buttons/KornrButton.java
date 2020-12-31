package com.tomasajt.kornr.gui.buttons;

import com.tomasajt.kornr.gui.KornrSettingsScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class KornrButton extends Button {

	private int gridX;
	private int gridY;
	private static Minecraft mc = Minecraft.getInstance();

	public KornrButton(int gridX, int gridY, String title, IPressable pressable) {
		super(0, 0, KornrSettingsScreen.buttonWidth, KornrSettingsScreen.buttonHeight, new StringTextComponent(title),
				pressable);
		this.gridX = gridX;
		this.gridY = gridY;

	}

	public KornrButton(int gridX, int gridY, String title, IPressable pressable, String tooltip) {
		super(0, 0, KornrSettingsScreen.buttonWidth, KornrSettingsScreen.buttonHeight, new StringTextComponent(title),
				pressable, (button, matrixStack, mouseX, mouseY) -> {
					KornrSettingsScreen screen = KornrSettingsScreen.instance;
					if (screen != null) {
						screen.renderTooltip(matrixStack,
								mc.fontRenderer.trimStringToWidth(new StringTextComponent(tooltip),
										Math.max(screen.width / 2 - 43, 170)),
								mouseX, mouseY);

					}
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
