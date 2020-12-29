package com.tomasajt.kornr.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.gui.buttons.KeyBHolderButton;
import com.tomasajt.kornr.gui.buttons.KornrButton;
import com.tomasajt.kornr.gui.buttons.ToggleableButton;
import com.tomasajt.kornr.keybindings.Clicker;
import com.tomasajt.kornr.rendering.BoundingBoxes;
import com.tomasajt.kornr.rendering.GammaToggler;
import com.tomasajt.kornr.rendering.NamePlates;
import com.tomasajt.kornr.rendering.Tracers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class KornrSettingsScreen extends Screen {

	private static Minecraft mc = Minecraft.getInstance();
	private List<KornrButton> buttons = new ArrayList<>();
	public static final KornrSettingsScreen instance = new KornrSettingsScreen();
	public final int gridSizeX;
	public final int gridSizeY;
	public static final int buttonWidth = 100;
	public static final int buttonHeight = 20;

	private KornrSettingsScreen() {
		super(new StringTextComponent("Kornr Settings"));
		buttons.add(new ToggleableButton(0, 0, "Tracers", new Tracers()));
		buttons.add(new ToggleableButton(0, 1, "BoundingBoxes", new BoundingBoxes()));
		buttons.add(new ToggleableButton(0, 2, "NamePlates", new NamePlates()));
		buttons.add(new KeyBHolderButton(1, 0, "AutoSprint", mc.gameSettings.keyBindSprint));
		buttons.add(new KeyBHolderButton(1, 1, "AutoJump", mc.gameSettings.keyBindJump));
		buttons.add(new ToggleableButton(1, 2, "Fullbright", new GammaToggler()));
		buttons.add(new ToggleableButton(2, 0, "Clicker", new Clicker()));
		int maxX = -1;
		int maxY = -1;
		for (KornrButton button : buttons) {
			if (button.gridX() > maxX)
				maxX = button.gridX();
			if (button.gridY() > maxY)
				maxY = button.gridY();
		}
		gridSizeX = maxX + 1;
		gridSizeY = maxY + 1;

	}

	@Override
	protected void init() {
		for (KornrButton button : buttons) {
			setKornrButtonPositionMiddle(button, /**/
					this.width / 2 - buttonWidth * (gridSizeX - 1) / 2 + buttonWidth * button.gridX(),
					this.height / 2 - buttonHeight * (gridSizeY - 1) / 2 + buttonHeight * button.gridY());
			this.addButton(button);
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	public static void setKornrButtonPositionMiddle(KornrButton button, int x, int y) {
		button.x = x - buttonWidth / 2;
		button.y = y - buttonHeight / 2;
	}
}
