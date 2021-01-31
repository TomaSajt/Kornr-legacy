package com.tomasajt.kornr.toggleables;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.KornrHelper;
import com.tomasajt.kornr.Toggleable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Tracers extends Toggleable {
	private static Minecraft mc = Minecraft.getInstance();
	private static Tessellator tessellator = Tessellator.getInstance();
	private static BufferBuilder buffer = tessellator.getBuffer();
	public static final Tracers instance = new Tracers();

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		if (instance.isOn()) {
			float partialTicks = event.getPartialTicks();
			MatrixStack matrixStack = event.getMatrixStack();

			for (Entity entity : mc.world.getAllEntities()) {
				if (KornrHelper.shouldDrawTracers(entity)) {
					KornrHelper.enableLinesThroughWallColored(1.0f, 1.0f, 1.0f, 0.3f, 3f);
					drawTracers(matrixStack, mc.player, entity, partialTicks);
				}
			}
			KornrHelper.disableLinesThroughWall();
		}

	}

	public static void drawTracers(MatrixStack matrixStack, PlayerEntity player, Entity entity, float partialTicks) {
		Vector3d camPos = KornrHelper.getCamPos();
		Vector3d selfPosShifted = mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON
				? camPos.add(player.getLookVec().scale(3))
				: player.getEyePosition(partialTicks);

		AxisAlignedBB aabb = KornrHelper.getPartialBoundingBox(entity, partialTicks);
		double averageX = (aabb.minX + aabb.maxX) / 2;
		double averageY = (aabb.minY + aabb.maxY) / 2;
		double averageZ = (aabb.minZ + aabb.maxZ) / 2;

		matrixStack.push();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
		KornrHelper.drawLine(matrixStack, buffer, selfPosShifted.x, selfPosShifted.y, selfPosShifted.z, averageX,
				averageY, averageZ);
		tessellator.draw();
		matrixStack.pop();
	}
}
