package com.tomasajt.kornr.toggleables;

import com.tomasajt.kornr.util.KornrHelper;
import com.tomasajt.kornr.util.Toggleable;

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

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END && instance.isOn()) {
			float partialTicks = mc.getTickLength();
			Entity closestEntity = null;
			Entity viewEntity = mc.getRenderViewEntity();
			if (viewEntity != null) {
				if (mc.world != null) {
					double reachDistance = (double) mc.playerController.getBlockReachDistance();
					AxisAlignedBB axisalignedbb = viewEntity.getBoundingBox().grow(reachDistance);
					double minDist = Double.MAX_VALUE;
					for (Entity entity : mc.world.getEntitiesInAABBexcluding(viewEntity, axisalignedbb,
							(p_215312_0_) -> !p_215312_0_.isSpectator() && p_215312_0_.canBeCollidedWith())) {
						double dist = KornrHelper.getPartialPosition(entity, partialTicks)
								.distanceTo(viewEntity.getEyePosition(partialTicks));
						if (dist < minDist && dist < reachDistance) {
							minDist = dist;
							closestEntity = entity;
						}
					}
				}
			}

			if (mc.world != null && mc.player != null && closestEntity != null
					&& mc.player.getCooledAttackStrength(0.5f) >= 1.0f) {

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
							if (inputEvent.shouldSwingHand())
								mc.player.swingArm(Hand.MAIN_HAND);
						}
					}
				}
			}
		}
	}
}
