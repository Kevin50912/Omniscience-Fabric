package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.access.LivingEntityRenderStateAccess;
import com.thatdudo.omniscience.config.Config;
import com.thatdudo.omniscience.config.ConfigManager;
import net.minecraft.client.render.entity.DolphinEntityRenderer;
import net.minecraft.client.render.entity.state.DolphinEntityRenderState;
import net.minecraft.entity.passive.DolphinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DolphinEntityRenderer.class)
public class DolphinEntityRendererMixin {

    @Inject(
            method = "updateRenderState",
            at = @At("TAIL")
    )
    private void omniscience$forceDolphinVisible(
            DolphinEntity entity,
            DolphinEntityRenderState state,
            float tickProgress,
            CallbackInfo ci
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

        /*
         * DolphinEntityRenderer 自己的 updateRenderState
         * 執行完之後，再次確保這兩個值正確。
         */
        state.invisible = false;
        state.invisibleToPlayer = false;
    }
}