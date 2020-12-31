package com.tomasajt.kornr.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeHooks;

public class KornrHelper {

	private static Minecraft mc = Minecraft.getInstance();

	public static boolean shouldDrawTracers(PlayerEntity player, Entity entity) {
		return entity != player && entity instanceof LivingEntity && !entity.isInvisibleToPlayer(mc.player);
	}

	public static boolean shouldDrawBoundingBox(PlayerEntity player, Entity entity) {
		return entity != player && entity instanceof LivingEntity && !entity.isInvisibleToPlayer(mc.player);
	}

	public static boolean shouldShowNamePlate(PlayerEntity player, Entity entity) {
		return entity != player && entity instanceof LivingEntity && !entity.isInvisibleToPlayer(mc.player);
	}

	public static Vector3d getCamPos() {
		return mc.getRenderManager().info.getProjectedView();
	}

	@SuppressWarnings("deprecation")
	public static void enableTextThroughWall() {
		RenderSystem.disableLighting();
		RenderSystem.depthMask(false);
		RenderSystem.disableDepthTest();
		RenderSystem.disableTexture();
	}

	@SuppressWarnings("deprecation")
	public static void disableTextThroughWall() {
		RenderSystem.enableLighting();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.enableTexture();
	}

	public static void drawLine(MatrixStack matrixStack, BufferBuilder buffer, double x1, double y1, double z1,
			double x2, double y2, double z2) {
		Matrix4f matrix4f = matrixStack.getLast().getMatrix();
		buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		buffer.pos(matrix4f, (float) x1, (float) y1, (float) z1).endVertex();
		buffer.pos(matrix4f, (float) x2, (float) y2, (float) z2).endVertex();
	}

	@SuppressWarnings("deprecation")
	public static void enableLinesThroughWallColored(float red, float green, float blue, float alpha, float lineWidth) {
		RenderSystem.enableBlend();
		RenderSystem.disableLighting();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		RenderSystem.color4f(red, green, blue, alpha);
		RenderSystem.lineWidth(lineWidth);
		RenderSystem.disableTexture();
		RenderSystem.depthMask(false);
		RenderSystem.disableDepthTest();
	}

	@SuppressWarnings("deprecation")
	public static void disableLinesThroughWall() {
		RenderSystem.enableDepthTest();
		RenderSystem.depthMask(true);
		RenderSystem.enableTexture();
		RenderSystem.enableLighting();
		RenderSystem.disableBlend();
	}

	public static AxisAlignedBB getBoundingBoxPartial(Entity entity, float partialTicks) {
		return getBoundingBoxCentered(entity).offset(getPartialPosition(entity, partialTicks));
	}

	public static Vector3d getPartialPosition(Entity entity, float partialTicks) {
		double partialPosX = MathHelper.lerp((double) partialTicks, entity.lastTickPosX, entity.getPosX());
		double partialPosY = MathHelper.lerp((double) partialTicks, entity.lastTickPosY, entity.getPosY());
		double partialPosZ = MathHelper.lerp((double) partialTicks, entity.lastTickPosZ, entity.getPosZ());
		return new Vector3d(partialPosX, partialPosY, partialPosZ);
	}

	public static AxisAlignedBB getBoundingBoxCentered(Entity entity) {
		return entity.getBoundingBox().offset(-entity.getPosX(), -entity.getPosY(), -entity.getPosZ());
	}

	public static void leftClick() {
		if (!mc.player.isRowingBoat()) {
			InputEvent.ClickInputEvent inputEvent = ForgeHooksClient.onClickInput(0, mc.gameSettings.keyBindAttack,
					Hand.MAIN_HAND);
			if (!inputEvent.isCanceled()) {
				switch (mc.objectMouseOver.getType()) {
				case ENTITY:
					mc.playerController.attackEntity(mc.player,
							((EntityRayTraceResult) mc.objectMouseOver).getEntity());
					break;
				case BLOCK:
					BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) mc.objectMouseOver;
					BlockPos blockpos = blockraytraceresult.getPos();
					if (!mc.world.isAirBlock(blockpos)) {
						mc.playerController.clickBlock(blockpos, blockraytraceresult.getFace());
						break;
					}
				case MISS:
					mc.player.resetCooldown();
					ForgeHooks.onEmptyLeftClick(mc.player);
				}
				if (inputEvent.shouldSwingHand())
					mc.player.swingArm(Hand.MAIN_HAND);
			}
		}

	}

	public static void rightClick() {
		if (!mc.playerController.getIsHittingBlock()) {
			if (!mc.player.isRowingBoat()) {
				for (Hand hand : Hand.values()) {
					net.minecraftforge.client.event.InputEvent.ClickInputEvent inputEvent = net.minecraftforge.client.ForgeHooksClient
							.onClickInput(1, mc.gameSettings.keyBindUseItem, hand);
					if (inputEvent.isCanceled()) {
						if (inputEvent.shouldSwingHand())
							mc.player.swingArm(hand);
						return;
					}
					ItemStack itemstack = mc.player.getHeldItem(hand);
					if (mc.objectMouseOver != null) {
						switch (mc.objectMouseOver.getType()) {
						case ENTITY:
							EntityRayTraceResult entityraytraceresult = (EntityRayTraceResult) mc.objectMouseOver;
							Entity entity = entityraytraceresult.getEntity();
							ActionResultType actionresulttype = mc.playerController.interactWithEntity(mc.player,
									entity, entityraytraceresult, hand);
							if (!actionresulttype.isSuccessOrConsume()) {
								actionresulttype = mc.playerController.interactWithEntity(mc.player, entity, hand);
							}

							if (actionresulttype.isSuccessOrConsume()) {
								if (actionresulttype.isSuccess()) {
									if (inputEvent.shouldSwingHand())
										mc.player.swingArm(hand);
								}

								return;
							}
							break;
						case BLOCK:
							BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) mc.objectMouseOver;
							int i = itemstack.getCount();
							ActionResultType actionresulttype1 = mc.playerController.func_217292_a(mc.player, mc.world,
									hand, blockraytraceresult);
							if (actionresulttype1.isSuccessOrConsume()) {
								if (actionresulttype1.isSuccess()) {
									if (inputEvent.shouldSwingHand())
										mc.player.swingArm(hand);
									if (!itemstack.isEmpty()
											&& (itemstack.getCount() != i || mc.playerController.isInCreativeMode())) {
										mc.gameRenderer.itemRenderer.resetEquippedProgress(hand);
									}
								}

								return;
							}

							if (actionresulttype1 == ActionResultType.FAIL) {
								return;
							}
						default:
							break;
						}
					}

					if (itemstack.isEmpty()
							&& (mc.objectMouseOver == null || mc.objectMouseOver.getType() == RayTraceResult.Type.MISS))
						net.minecraftforge.common.ForgeHooks.onEmptyClick(mc.player, hand);

					if (!itemstack.isEmpty()) {
						ActionResultType actionresulttype2 = mc.playerController.processRightClick(mc.player, mc.world,
								hand);
						if (actionresulttype2.isSuccessOrConsume()) {
							if (actionresulttype2.isSuccess()) {
								mc.player.swingArm(hand);
							}

							mc.gameRenderer.itemRenderer.resetEquippedProgress(hand);
							return;
						}
					}
				}

			}
		}
	}

	public static int getBinomial(int min, int max, double p) {
		int x = 0;
		for (int i = 0; i < max - min + 1; i++) {
			if (Math.random() < p)
				x++;
		}
		return x + min;
	}

	public static void renderNamePlate(FontRenderer fontrenderer, ITextComponent text, MatrixStack matrixStack,
			IRenderTypeBuffer.Impl impl) {
		renderNamePlate(fontrenderer, text, matrixStack, impl, 0.0f);
	}

	public static void renderNamePlate(FontRenderer fontrenderer, ITextComponent text, MatrixStack matrixStack,
			IRenderTypeBuffer.Impl impl, float backgroundOpacity) {
		float offset = (float) (-fontrenderer.getStringPropertyWidth(text) / 2);
		int j = (int) (mc.gameSettings.getTextBackgroundOpacity(backgroundOpacity) * 255.0F) << 24;
		fontrenderer.func_243247_a(text, offset, (float) 0, -1, false, matrixStack.getLast().getMatrix(), impl, true, j,
				15728880);
	}

	public static void drawTextCentered(FontRenderer fontRenderer, ITextComponent text, MatrixStack matrixStack,
			float x, float y, int color) {
		float offset = (float) (-fontRenderer.getStringPropertyWidth(text) / 2);
		matrixStack.translate(offset, -4.5, 0);
		fontRenderer.drawString(matrixStack, text.getString(), 0, 0, Color.red.getRGB());
		matrixStack.translate(-offset, 4.5, 0);
	}

	public static void drawTextCenteredTwoSided(FontRenderer fontRenderer, ITextComponent text, MatrixStack matrixStack,
			float x, float y, int color) {
		drawTextCentered(fontRenderer, text, matrixStack, x, y, color);
		matrixStack.rotate(new Quaternion(0, 180, 0, true));
		drawTextCentered(fontRenderer, text, matrixStack, x, y, color);

	}

	@SuppressWarnings("deprecation")
	public static void drawTexture(MatrixStack matrixStack, float x, float y, float uOffset, float vOffset, float width,
			float height, float blitOffset, ResourceLocation textureResource) {
		float x1 = x;
		float x2 = x + width;
		float y1 = y;
		float y2 = y + height;
		float minU = uOffset;
		float maxU = uOffset + width;
		float minV = vOffset;
		float maxV = vOffset + height;
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		mc.getTextureManager().bindTexture(textureResource);
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(matrix, x1, y2, blitOffset).tex(minU / 256, maxV / 256).endVertex();
		bufferbuilder.pos(matrix, x2, y2, blitOffset).tex(maxU / 256, maxV / 256).endVertex();
		bufferbuilder.pos(matrix, x2, y1, blitOffset).tex(maxU / 256, minV / 256).endVertex();
		bufferbuilder.pos(matrix, x1, y1, blitOffset).tex(minU / 256, minV / 256).endVertex();
		tessellator.draw();
		RenderSystem.enableAlphaTest();
	}
}
