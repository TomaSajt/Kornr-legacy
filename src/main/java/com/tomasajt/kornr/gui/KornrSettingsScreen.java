package com.tomasajt.kornr.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.AutoJump;
import com.tomasajt.kornr.AutoSprint;
import com.tomasajt.kornr.BoundingBoxes;
import com.tomasajt.kornr.Clicker;
import com.tomasajt.kornr.Fullbright;
import com.tomasajt.kornr.NamePlates;
import com.tomasajt.kornr.Tracers;
import com.tomasajt.kornr.gui.buttons.KornrButton;
import com.tomasajt.kornr.gui.buttons.ToggleableButton;
import com.tomasajt.kornr.util.KornrHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class KornrSettingsScreen extends Screen {

	private static Minecraft mc = Minecraft.getInstance();
	private List<KornrButton> buttons = new ArrayList<>();
	public static final KornrSettingsScreen instance = new KornrSettingsScreen();
	private int ticks = 0;
	public final int gridSizeX;
	public final int gridSizeY;
	public static final int buttonWidth = 100;
	public static final int buttonHeight = 20;

	private KornrSettingsScreen() {
		super(new StringTextComponent("Kornr Settings"));
		buttons.add(new ToggleableButton(0, 0, "Tracers", Tracers.instance));
		buttons.add(new ToggleableButton(0, 1, "BoundingBoxes", BoundingBoxes.instance));
		buttons.add(new ToggleableButton(0, 2, "NamePlates", NamePlates.instance));
		buttons.add(new ToggleableButton(1, 0, "AutoSprint", AutoSprint.instance));
		buttons.add(new ToggleableButton(1, 1, "AutoJump", AutoJump.instance));
		buttons.add(new ToggleableButton(1, 2, "Fullbright", Fullbright.instance));
		buttons.add(new ToggleableButton(2, 0, "Clicker", Clicker.instance));
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
		FontRenderer fontRenderer = mc.fontRenderer;
		matrixStack.push();
		ITextComponent text = new StringTextComponent("Poggers");
		float offset = (float) (fontRenderer.getStringPropertyWidth(text) / 2);
		matrixStack.translate(this.width / 2, this.height / 2, 0);
		matrixStack.scale(3, 3, 3);
		matrixStack.translate(0, 0, offset);
		matrixStack.rotate(new Quaternion((ticks + partialTicks) * 9, (ticks + partialTicks) * 5, 0, true));
		KornrHelper.drawTextCenteredTwoSided(fontRenderer, text, matrixStack, 0, 0, Color.red.getRGB());
		matrixStack.pop();
	}

	@Override
	public void onClose() {
		ticks = 0;
	}

	@Override
	public void tick() {
		ticks++;
	}

	public static void setKornrButtonPositionMiddle(KornrButton button, int x, int y) {
		button.x = x - buttonWidth / 2;
		button.y = y - buttonHeight / 2;
	}
}
