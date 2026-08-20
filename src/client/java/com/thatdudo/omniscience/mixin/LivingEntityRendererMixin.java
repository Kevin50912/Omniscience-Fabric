package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.access.LivingEntityRenderStateAccess;
import com.thatdudo.omniscience.config.Config;
import com.thatdudo.omniscience.config.ConfigManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(
            method = "updateRenderState",
            at = @At("TAIL")
    )
    private void omniscience$modifyRenderState(
            LivingEntity entity,
            LivingEntityRenderState state,
            float tickProgress,
            CallbackInfo ci
    ) {
        Config config = ConfigManager.getConfig();

        if (!config.isEnabled()) {
            return;
        }

        LivingEntityRenderStateAccess access =
                (LivingEntityRenderStateAccess) state;

        boolean shouldReveal =
                entity.isInvisible()
                        && (
                        config.isEntityTargeted(entity)
                                || entity.getType() == EntityType.DOLPHIN
                );

        access.omniscience$setWasInvisible(shouldReveal);

        /*
         * 只修改 invisible。
         *
         * 不要修改 invisibleToPlayer。
         */
        if (shouldReveal) {
            state.invisible = false;
        }
    }


    @Inject(
            method = "getRenderLayer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void omniscience$modifyRenderLayer(
            LivingEntityRenderState state,
            boolean showBody,
            boolean translucent,
            boolean showOutline,
            CallbackInfoReturnable<RenderLayer> cir
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
         * 這裡不能直接：
         *
         * LivingEntityRenderer<?> renderer
         *
         * 否則會遇到 CAP#1 泛型問題。
         *
         * Raw type 可以安全地避開這個問題。
         */
        LivingEntityRenderer renderer =
                (LivingEntityRenderer) (Object) this;

        Identifier texture =
                renderer.getTexture(state);

        /*
         * 讓原本完全不透明的 entity
         * 改走透明 RenderLayer。
         */
        cir.setReturnValue(
                RenderLayers.entityTranslucent(texture)
        );
    }


    @Inject(
            method = "getMixColor",
            at = @At("RETURN"),
            cancellable = true
    )
    private void omniscience$modifyBodyAlpha(
            LivingEntityRenderState state,
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


    @Inject(
            method = "hasLabel",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onShouldRenderName(
            LivingEntity livingEntity,
            double squaredDistance,
            CallbackInfoReturnable<Boolean> info
    ) {
        Config config = ConfigManager.getConfig();

        if (!config.isEnabled()) {
            return;
        }

        if (config.getForceRenderNameTags() == 2
                && config.shouldEntityGlow(livingEntity)) {
            info.setReturnValue(true);
        }
    }
}