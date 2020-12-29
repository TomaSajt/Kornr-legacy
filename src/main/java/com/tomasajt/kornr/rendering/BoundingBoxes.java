package com.tomasajt.kornr.rendering;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.util.IToggleable;
import com.tomasajt.kornr.util.KornrHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BoundingBoxes implements IToggleable {

	private static Minecraft mc = Minecraft.getInstance();
	private static Tessellator tessellator = Tessellator.getInstance();
	private static BufferBuilder buffer = tessellator.getBuffer();
	public static boolean isOn = false;

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		if (isOn) {
			float partialTicks = event.getPartialTicks();
			PlayerEntity player = mc.player;
			ClientWorld clientWorld = mc.world;
			MatrixStack matrixStack = event.getMatrixStack();

			KornrHelper.enableLinesThroughWallColored(1.0f, 0.0f, 1.0f, 0.9f, 1f);
			for (Entity entity : clientWorld.getAllEntities()) {
				if (KornrHelper.shouldDrawTracers(player, entity)) {
					drawBoundingBox(matrixStack, entity, partialTicks);
				}
			}
			KornrHelper.disableLinesThroughWall();
		}
	}

	private static void drawBoundingBox(MatrixStack matrixStack, Entity entity, float partialTicks) {
		matrixStack.push();
		Vector3d camPos = KornrHelper.getCamPos();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
		AxisAlignedBB aabb = KornrHelper.getBoundingBoxPartial(entity, partialTicks);
		Matrix4f matrix4f = matrixStack.getLast().getMatrix();
		float minX = (float) aabb.minX;
		float minY = (float) aabb.minY;
		float minZ = (float) aabb.minZ;
		float maxX = (float) aabb.maxX;
		float maxY = (float) aabb.maxY;
		float maxZ = (float) aabb.maxZ;
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		buffer.pos(matrix4f, minX, minY, minZ).endVertex();
		buffer.pos(matrix4f, minX, minY, maxZ).endVertex();
		buffer.pos(matrix4f, minX, minY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, minY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, minY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, minY, minZ).endVertex();
		buffer.pos(matrix4f, maxX, minY, minZ).endVertex();
		buffer.pos(matrix4f, minX, minY, minZ).endVertex();
		buffer.pos(matrix4f, minX, maxY, minZ).endVertex();
		buffer.pos(matrix4f, minX, maxY, maxZ).endVertex();
		buffer.pos(matrix4f, minX, maxY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, maxY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, maxY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, maxY, minZ).endVertex();
		buffer.pos(matrix4f, maxX, maxY, minZ).endVertex();
		buffer.pos(matrix4f, minX, maxY, minZ).endVertex();
		buffer.pos(matrix4f, minX, minY, minZ).endVertex();
		buffer.pos(matrix4f, minX, maxY, minZ).endVertex();
		buffer.pos(matrix4f, minX, minY, maxZ).endVertex();
		buffer.pos(matrix4f, minX, maxY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, minY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, maxY, maxZ).endVertex();
		buffer.pos(matrix4f, maxX, minY, minZ).endVertex();
		buffer.pos(matrix4f, maxX, maxY, minZ).endVertex();
		tessellator.draw();
		matrixStack.pop();
	}

	@Override
	public void on() {
		isOn = true;
	}

	@Override
	public void off() {
		isOn = false;
	}
}