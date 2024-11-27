package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.config.ConfigManager;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Inject(at = @At("HEAD"), method = "getFogModifier", cancellable = true)
    private static void getFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Class> cir) {
        if (ConfigManager.getConfig().removeBlindnessEffect) {
            cir.setReturnValue(null);
        }
    }
}
