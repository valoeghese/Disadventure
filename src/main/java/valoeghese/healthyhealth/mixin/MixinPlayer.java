package valoeghese.healthyhealth.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(at = @At("RETURN"), method = "canEat", cancellable = true)
	private void canEatWhenHurt(boolean bl, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(info.getReturnValueZ() || (this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth()));
	}
}
