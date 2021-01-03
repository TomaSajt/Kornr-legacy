package com.tomasajt.kornr.toggleables;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tomasajt.kornr.KornrHelper;
import com.tomasajt.kornr.Toggleable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class Hotbars extends Toggleable {

	public static final Hotbars instance = new Hotbars();
	public static Minecraft mc = Minecraft.getInstance();

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event) {
		if (instance.isOn()) {
			MatrixStack matrixStack = event.getMatrixStack();
			float partialTicks = event.getPartialTicks();
			matrixStack.push();
			Vector3d camPos = KornrHelper.getCamPos();
			matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
			for (PlayerEntity player : mc.world.getPlayers()) {
				if (player != mc.player) {
					matrixStack.push();
					Vector3d namePos = KornrHelper.getPartialPosition(player, partialTicks).add(0,
							player.getHeight() + 0.5f, 0);
					matrixStack.translate(namePos.x, namePos.y, namePos.z);
					matrixStack.rotate(mc.getRenderManager().getCameraOrientation());
					matrixStack.scale(-0.025f, -0.025f, 0.025f);
					matrixStack.translate(0, -30.0F, 0);
					Scoreboard scoreboard = player.getWorldScoreboard();
					ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
					if (scoreobjective != null) {
						matrixStack.translate(0.0D, (double) -9.0F * 1.15F, 0.0D);
					}
					mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/widgets.png"));
					ItemStack itemstack = player.getHeldItemOffhand();
					HandSide handside = player.getPrimaryHand().opposite();

					KornrHelper.enableThroughWall();
					KornrHelper.blit(matrixStack, -91, 0, 0, 0, 0, 182, 22);
					KornrHelper.blit(matrixStack, -91 - 1 + player.inventory.currentItem * 20, -1, 0, 0, 22, 24, 22);
					if (!itemstack.isEmpty()) {
						if (handside == HandSide.LEFT) {
							KornrHelper.blit(matrixStack, -91 - 29, 0, -1, 24, 22, 29, 24);
						} else {
							KornrHelper.blit(matrixStack, 91, -1, 0, 53, 22, 29, 24);
						}
					}
					KornrHelper.disableThroughWall();
					matrixStack.pop();
				}
			}
			matrixStack.pop();
		}

	}

}
