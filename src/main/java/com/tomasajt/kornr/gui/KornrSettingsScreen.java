package com.tomasajt.kornr.gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.KornrHelper;
import com.tomasajt.kornr.KornrKeybindings;
import com.tomasajt.kornr.gui.buttons.KornrButton;
import com.tomasajt.kornr.gui.buttons.ToggleableKornrButton;
import com.tomasajt.kornr.toggleables.AutoCrit;
import com.tomasajt.kornr.toggleables.AutoJump;
import com.tomasajt.kornr.toggleables.AutoSprint;
import com.tomasajt.kornr.toggleables.BoundingBoxes;
import com.tomasajt.kornr.toggleables.Clicker;
import com.tomasajt.kornr.toggleables.Fullbright;
import com.tomasajt.kornr.toggleables.Hotbars;
import com.tomasajt.kornr.toggleables.NamePlates;
import com.tomasajt.kornr.toggleables.Tracers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.nbt.CompoundNBT;
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
	private File kornrDir = new File(mc.gameDir, "kornr");
	private File optionsFile = new File(kornrDir, "settings.txt");

	private KornrSettingsScreen() {
		super(new StringTextComponent("Kornr Settings"));
		buttons.add(new ToggleableKornrButton(0, 0, "Tracers", Tracers.instance));
		buttons.add(new ToggleableKornrButton(0, 1, "BoundingBoxes", BoundingBoxes.instance));
		buttons.add(new ToggleableKornrButton(0, 2, "NamePlates", NamePlates.instance));
		buttons.add(new ToggleableKornrButton(1, 0, "AutoSprint", AutoSprint.instance));
		buttons.add(new ToggleableKornrButton(1, 1, "AutoJump", AutoJump.instance));
		buttons.add(new ToggleableKornrButton(1, 2, "Fullbright", Fullbright.instance));
		buttons.add(new ToggleableKornrButton(2, 0, "Clicker", Clicker.instance));
		buttons.add(new ToggleableKornrButton(2, 1, "AutoCrit", AutoCrit.instance,
				"Remember, critical hits only happen when you are not sprinting"));
		buttons.add(new ToggleableKornrButton(2, 2, "Hotbars", Hotbars.instance));
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
			if (button instanceof ToggleableKornrButton) {
				((ToggleableKornrButton) button).updateState();
			}
			setKornrButtonPositionMiddle(button, /**/
					this.width / 2 - buttonWidth * (gridSizeX - 1) / 2 + buttonWidth * button.gridX(),
					this.height / 2 - buttonHeight * (gridSizeY - 1) / 2 + buttonHeight * button.gridY());
			this.addButton(button);
		}
		KornrButton saveButton = new KornrButton(1, 4, "Save setup", (button) -> this.saveSettings(),
				"Save your current setup as the default setup");
		setKornrButtonPositionMiddle(saveButton, /**/
				this.width / 2, this.height / 2 - buttonHeight * (gridSizeY - 1) / 2 + buttonHeight * (gridSizeY + 1));
		this.addButton(saveButton);

	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		FontRenderer fontRenderer = mc.fontRenderer;
		matrixStack.push();
		ITextComponent text = new StringTextComponent("Kornr");
		float offset = (float) (fontRenderer.getStringPropertyWidth(text) / 2);
		matrixStack.translate(this.width / 2, (this.height - buttonHeight * (gridSizeY - 1)) / 4, 0);
		matrixStack.scale(3, 3, 3);
		matrixStack.translate(0, 0, offset);
		matrixStack.rotate(new Quaternion((ticks + partialTicks) * 9, (ticks + partialTicks) * 5, 0, true));
		KornrHelper.drawTextCenteredTwoSided(fontRenderer, text, matrixStack, 0, 0, Color.red.getRGB());
		matrixStack.pop();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		}
		if (KornrKeybindings.keyBindingOpenSettings.isActiveAndMatches(mouseKey)) {
			this.closeScreen();
		}
		return true;
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

	public void saveSettings() {
		
		try {
			kornrDir.mkdir();
			optionsFile.createNewFile();
			try (PrintWriter printwriter = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(optionsFile), StandardCharsets.UTF_8))) {
				printwriter.println("tracers:" + Tracers.instance.isOn());
				printwriter.println("boundingBoxes:" + BoundingBoxes.instance.isOn());
				printwriter.println("namePlates:" + NamePlates.instance.isOn());
				printwriter.println("autoSprint:" + AutoSprint.instance.isOn());
				printwriter.println("autoJump:" + AutoJump.instance.isOn());
				printwriter.println("fullbright:" + Fullbright.instance.isOn());
				printwriter.println("clicker:" + Clicker.instance.isOn());
				printwriter.println("autoCrit:" + AutoCrit.instance.isOn());
				printwriter.println("hotbars:" + Hotbars.instance.isOn());
			} catch (Exception exception) {
				KornrHelper.sendMessage("Something went wrong with saving");
			}
		} catch (IOException e) {
			KornrHelper.sendMessage("Something went wrong with creating the file");
		}

	}

	public void loadSettings() {
		try {
			if (!this.optionsFile.exists()) {
				return;
			}
			CompoundNBT compoundnbt = new CompoundNBT();

			try (BufferedReader bufferedreader = Files.newReader(optionsFile, Charsets.UTF_8)) {
				bufferedreader.lines().forEach((optionString) -> {
					try {
						Iterator<String> iterator = Splitter.on(':').limit(2).split(optionString).iterator();
						compoundnbt.putString(iterator.next(), iterator.next());
					} catch (Exception exception2) {

					}

				});
			}
			for (String s : compoundnbt.keySet()) {
				String s1 = compoundnbt.getString(s);
				switch (s) {
				case "tracers":
					if (s1.equals("true"))
						Tracers.instance.on();
					else
						Tracers.instance.off();
					break;
				case "boundingBoxes":
					if (s1.equals("true"))
						BoundingBoxes.instance.on();
					else
						BoundingBoxes.instance.off();
					break;
				case "namePlates":
					if (s1.equals("true"))
						NamePlates.instance.on();
					else
						NamePlates.instance.off();
					break;
				case "autoSprint":
					if (s1.equals("true"))
						AutoSprint.instance.on();
					else
						AutoSprint.instance.off();
					break;
				case "autoJump":
					if (s1.equals("true"))
						AutoJump.instance.on();
					else
						AutoJump.instance.off();
					break;
				case "fullbright":
					if (s1.equals("true"))
						Fullbright.instance.on();
					else
						Fullbright.instance.off();
					break;
				case "clicker":
					if (s1.equals("true"))
						Clicker.instance.on();
					else
						Clicker.instance.off();
					break;
				case "autoCrit":
					if (s1.equals("true"))
						AutoCrit.instance.on();
					else
						AutoCrit.instance.off();
					break;
				case "hotbars":
					if (s1.equals("true"))
						Hotbars.instance.on();
					else
						Hotbars.instance.off();
					break;
				}

			}
		} catch (Exception e) {
			KornrHelper.sendMessage("Failed to load options");
		}
	}
}
