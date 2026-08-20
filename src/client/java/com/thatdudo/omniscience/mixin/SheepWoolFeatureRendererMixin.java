package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.config.Config;
import com.thatdudo.omniscience.config.ConfigManager;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.SheepEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepWoolFeatureRenderer.class)
public abstract class SheepWoolFeatureRendererMixin {

    @Shadow @Final
    private static Identifier TEXTURE;

    @Shadow @Final
    private EntityModel<SheepEntityRenderState> woolModel;

    @Shadow @Final
    private EntityModel<SheepEntityRenderState> babyWoolModel;

    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void omniscience$renderInvisibleWool(
            MatrixStack matrixStack,
            OrderedRenderCommandQueue queue,
            int light,
            SheepEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci
    ) {
        Config config = ConfigManager.getConfig();

        if (!config.isEnabled()
                || !state.invisible
                || state.sheared) {
            return;
        }

        EntityModel<SheepEntityRenderState> model =
                state.baby ? this.babyWoolModel : this.woolModel;

        int alpha = (int) (config.alpha * 255.0F);
        alpha = Math.max(0, Math.min(255, alpha));

        int color =
                (alpha << 24)
                        | (state.getRgbColor() & 0x00FFFFFF);

        queue.submitModel(
                model,
                state,
                matrixStack,
                RenderLayers.entityTranslucent(TEXTURE),
                light,
                LivingEntityRenderer.getOverlay(state, 0.0F),
                color,
                (Sprite) null,
                state.outlineColor,
                (ModelCommandRenderer.CrumblingOverlayCommand) null
        );

        ci.cancel();
    }
}