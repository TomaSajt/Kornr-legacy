package com.tomasajt.kornr;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.util.KornrHelper;
import com.tomasajt.kornr.util.Toggleable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@EventBusSubscriber
public class NamePlates extends Toggleable {

	private static Minecraft mc = Minecraft.getInstance();
	public static final NamePlates instance = new NamePlates();

	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		if (instance.isOn) {
			float partialTicks = event.getPartialTicks();
			ClientWorld clientWorld = mc.world;
			MatrixStack matrixStack = event.getMatrixStack();
			PlayerEntity player = mc.player;
			EntityRendererManager renderManager = mc.getRenderManager();
			KornrHelper.enableTextThroughWall();
			for (Entity entity : clientWorld.getAllEntities()) {
				if (KornrHelper.shouldShowNamePlate(player, entity)) {
					drawNamePlate(matrixStack, entity, partialTicks, renderManager);
				}
			}
			KornrHelper.disableTextThroughWall();
		}
	}

	@SubscribeEvent
	public static void onNamePlate(RenderNameplateEvent event) {
		if (instance.isOn) {
			Entity entity = event.getEntity();
			if (KornrHelper.shouldShowNamePlate(mc.player, entity)) {
				event.setResult(Result.DENY);
			}
		}
	}

	private static void drawNamePlate(MatrixStack matrixStack, Entity entity, float partialTicks,
			EntityRendererManager renderManager) {
		Vector3d namePos = KornrHelper.getPartialPosition(entity, partialTicks).add(0, entity.getHeight() + 0.5f, 0);
		Vector3d camPos = KornrHelper.getCamPos();
		matrixStack.push();
		matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
		matrixStack.translate(namePos.x, namePos.y, namePos.z);
		matrixStack.rotate(renderManager.getCameraOrientation());
		matrixStack.scale(-0.025F, -0.025F, 0.025F);
		FontRenderer fontrenderer = mc.fontRenderer;
		IRenderTypeBuffer.Impl impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
		ITextComponent displayName = entity.getDisplayName();
		if (entity instanceof PlayerEntity) {
			PlayerEntity pEntity = (PlayerEntity) entity;
			Scoreboard scoreboard = pEntity.getWorldScoreboard();
			ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
			if (scoreobjective != null) {
				Score score = scoreboard.getOrCreateScore(pEntity.getScoreboardName(), scoreobjective);
				KornrHelper.renderNamePlate(fontrenderer,
						new StringTextComponent(Integer.toString(score.getScorePoints())).appendString(" ")
								.append(scoreobjective.getDisplayName()),
						matrixStack, impl);
				matrixStack.translate(0.0D, (double) -9.0F * 1.15F, 0.0D);
			}
		}
		KornrHelper.renderNamePlate(fontrenderer, displayName, matrixStack, impl);
		impl.finish();
		matrixStack.pop();
	}
}
