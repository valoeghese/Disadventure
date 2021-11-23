package valoeghese.healthyhealth.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


/**
 * Mixin into the minecraft-national health system display.
 */
@Mixin(Gui.class)
public abstract class MixinNHSDisplay {
	@Shadow
	protected abstract int getVehicleMaxHearts(LivingEntity livingEntity);

	@Shadow
	protected abstract int getVisibleVehicleHeartRows(int i);

	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
	private int disableHungerRendering(Gui gui, LivingEntity entity) {
		if (entity == null || !entity.showVehicleHealth()) {
			return 42069;
		} else {
			return this.getVehicleMaxHearts(entity);
		}
	}

	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVisibleVehicleHeartRows(I)I"))
	private int ductTapeTheConsequencesOfTheAbove(Gui gui, int i) {
		return this.getVisibleVehicleHeartRows(i == 42069 ? 0 : i);
	}

	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getArmorValue()I"))
	private int makeArmourValueAir(LivingEntity entity) {
		int maxAir = entity.getMaxAirSupply();
		int trueAir = entity.getAirSupply();

		if (trueAir >= maxAir && !entity.isEyeInFluid(FluidTags.WATER)) {
			return 0;
		}

		float progress = (float)trueAir / (float)maxAir;
		int roundedResult = Mth.ceil(progress * 10.0f);

		return roundedResult * 2 + (progress % 0.1f > 0.075f ? 1 : 0);
	}

	// TODO flip half-armour image. it seems like it's meant to be that way anyway
	@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiComponent;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V"))
	private void screwWithEverything(GuiComponent guiComponent, PoseStack poseStack, int x, int y, int u, int v, int xsize, int ysize) {
		// air: 16,25 | 18
		// armour: 16,25,34 | 9
		if (v == 9) { // if trying to render armour
			switch (u) { // nothing
			case 16:
				return; // don't render
			case 34: // 9,(34,25) actually need overriding
				u = 16;
			case 25:
				v = 18;
				break;
			default:
				break;
			}
		}

		guiComponent.blit(poseStack, x, y, u, v, xsize, ysize);
	}

}
