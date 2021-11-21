package valoeghese.healthyhealth.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Overlay;
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
}
