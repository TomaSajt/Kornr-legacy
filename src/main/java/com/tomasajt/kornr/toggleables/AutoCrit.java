package com.tomasajt.kornr.toggleables;

import com.tomasajt.kornr.KornrHelper;
import com.tomasajt.kornr.Toggleable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AutoCrit extends Toggleable {

	public static final AutoCrit instance = new AutoCrit();
	private static Minecraft mc = Minecraft.getInstance();
	private static int tick = 0;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END && instance.isOn() && mc.player != null && mc.world != null) {
			if (tick > 0)
				tick--;

			float partialTicks = mc.getTickLength();
			Entity closestEntity = null;
			double reachDistance = (double) mc.playerController.getBlockReachDistance();
			AxisAlignedBB axisalignedbb = mc.player.getBoundingBox().grow(reachDistance);
			double minDist = Double.MAX_VALUE;
			for (Entity entity : mc.world.getEntitiesInAABBexcluding(mc.player, axisalignedbb,
					(entity) -> !entity.isSpectator() && entity.canBeCollidedWith() && !entity.hitByEntity(mc.player))) {
				double dist = KornrHelper.getPartialPosition(entity, partialTicks)
						.distanceTo(mc.player.getEyePosition(partialTicks));
				if (dist < minDist && dist < reachDistance) {
					minDist = dist;
					closestEntity = entity;
				}

			}
			if (tick == 0 && closestEntity != null && mc.player.getCooledAttackStrength(0.5f) >= 1.0f) {

				boolean willCrit = mc.player.fallDistance > 0.0F && !mc.player.isOnGround() && !mc.player.isOnLadder()
						&& !mc.player.isInWater() && !mc.player.isPotionActive(Effects.BLINDNESS)
						&& !mc.player.isPassenger() && closestEntity instanceof LivingEntity
						&& !mc.player.isSprinting();
				if (willCrit) {
					if (!mc.player.isRowingBoat()) {
						InputEvent.ClickInputEvent inputEvent = ForgeHooksClient.onClickInput(0,
								mc.gameSettings.keyBindAttack, Hand.MAIN_HAND);
						if (!inputEvent.isCanceled()) {
							mc.playerController.attackEntity(mc.player, closestEntity);
							tick = 5;
							if (inputEvent.shouldSwingHand())
								mc.player.swingArm(Hand.MAIN_HAND);
						}
					}
				}
			}
			if (mc.player.isOnGround()) {
				tick = 0;
			}
		} else {
			tick = 0;
		}
	}
}
