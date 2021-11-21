package valoeghese.healthyhealth.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinFoodItem {
	@Shadow @Nullable public abstract FoodProperties getFoodProperties();

	@Shadow public abstract boolean isEdible();

	@Inject(at = @At("HEAD"), method = "use", cancellable = true)
	public void use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info) {
		if (this.isEdible()) {
			ItemStack itemStack = player.getItemInHand(interactionHand);

			if (player.canEat(this.getFoodProperties().canAlwaysEat())) {
				player.heal(this.getFoodProperties().getNutrition());
				info.setReturnValue(InteractionResultHolder.consume(itemStack));
			} else {
				info.setReturnValue(InteractionResultHolder.fail(itemStack));
			}
		}
	}
}
