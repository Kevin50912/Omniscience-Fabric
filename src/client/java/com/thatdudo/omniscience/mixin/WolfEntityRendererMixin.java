package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.access.LivingEntityRenderStateAccess;
import com.thatdudo.omniscience.config.Config;
import com.thatdudo.omniscience.config.ConfigManager;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.client.render.entity.state.WolfEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntityRenderer.class)
public class WolfEntityRendererMixin {

    @Inject(
            method = "getMixColor",
            at = @At("RETURN"),
            cancellable = true
    )
    private void omniscience$modifyWolfAlpha(
            WolfEntityRenderState state,
            CallbackInfoReturnable<Integer> cir
    ) {
        Config config = ConfigManager.getConfig();

        if (!config.isEnabled()) {
            return;
        }

        LivingEntityRenderStateAccess access =
                (LivingEntityRenderStateAccess) state;

        if (!access.omniscience$wasInvisible()) {
            return;
        }

        int originalColor = cir.getReturnValue();

        int alpha = Math.round(config.alpha * 255.0F);
        alpha = Math.max(0, Math.min(255, alpha));

        int color =
                (alpha << 24)
                        | (originalColor & 0x00FFFFFF);

        cir.setReturnValue(color);
    }
}