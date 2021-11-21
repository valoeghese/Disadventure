package valoeghese.healthyhealth.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodData.class)
public class MixinNHSSystems {
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	private void removeTick(Player player, CallbackInfo info) {
		info.cancel();
	}

	@Inject(at = @At("HEAD"), method = "getFoodLevel", cancellable = true)
	private void alwaysCantSprint(CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(6);
	}

	@Inject(at = @At("HEAD"), method = "getLastFoodLevel", cancellable = true)
	private void alwaysCantSprint2(CallbackInfoReturnable<Integer> info) {
		info.setReturnValue(6);
	}
}
