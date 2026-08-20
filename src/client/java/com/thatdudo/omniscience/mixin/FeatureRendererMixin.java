package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.access.LivingEntityRenderStateAccess;
import com.thatdudo.omniscience.config.Config;
import com.thatdudo.omniscience.config.ConfigManager;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderer.class)
public class FeatureRendererMixin {

    @Inject(
            method = "renderModel",
            at = @At("HEAD"),
            cancellable = true
    )
    private static <S extends LivingEntityRenderState> void omniscience$renderModel(
            Model<? super S> model,
            Identifier texture,
            MatrixStack matrices,
            OrderedRenderCommandQueue queue,
            int light,
            S state,
            int color,
            int queueOrder,
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

        int alpha = Math.round(config.alpha * 255.0F);
        alpha = Math.max(0, Math.min(255, alpha));

        int tintedColor =
                (alpha << 24) | (color & 0x00FFFFFF);

        queue.getBatchingQueue(queueOrder).submitModel(
                model,
                state,
                matrices,
                RenderLayers.entityTranslucent(texture),
                light,
                LivingEntityRenderer.getOverlay(state, 0.0F),
                tintedColor,
                (Sprite) null,
                state.outlineColor,
                (ModelCommandRenderer.CrumblingOverlayCommand) null
        );

        ci.cancel();
    }
}