package com.tomasajt.kornr;

import com.tomasajt.kornr.util.KornrHelper;
import com.tomasajt.kornr.util.Toggleable;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Clicker extends Toggleable {

	
	public static final Clicker instance = new Clicker();
	private static Minecraft mc = Minecraft.getInstance();
	public static int minTPCL = 3;
	public static int maxTPCL = 6;
	public static int currentTPCL;
	private static int tickCounterL = 0;
	public static int minTPCR = 3;
	public static int maxTPCR = 6;
	public static int currentTPCR;
	private static int tickCounterR = 0;

	@SubscribeEvent
	public static void onClientTickEvent(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			if (instance.isOn && mc.player != null && mc.currentScreen == null && mc.gameSettings.keyBindAttack.isKeyDown()) {
				if (tickCounterL >= currentTPCL) {
					KornrHelper.leftClick();
					tickCounterL = 0;
					currentTPCL = KornrHelper.getBinomial(minTPCL, maxTPCL, 0.1f);
				}
				tickCounterL++;
			} else if (tickCounterL != 0) {
				currentTPCL = KornrHelper.getBinomial(minTPCL, maxTPCL, 0.1f);
				tickCounterL = 0;
			}
			if (instance.isOn && mc.player != null && mc.currentScreen == null && mc.gameSettings.keyBindUseItem.isKeyDown()) {
				if (tickCounterR >= currentTPCR) {
					KornrHelper.rightClick();
					tickCounterR = 0;
					currentTPCR = KornrHelper.getBinomial(minTPCR, maxTPCR, 0.1f);
				}
				tickCounterR++;
			} else if (tickCounterR != 0) {
				currentTPCR = KornrHelper.getBinomial(minTPCR, maxTPCR, 0.1f);
				tickCounterR = 0;
			}
		}
	}
}
