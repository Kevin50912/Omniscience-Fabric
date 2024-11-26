package com.thatdudo.omniscience.mixin;

import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BackgroundRenderer.class)
public class LivingEntityMixin {

//    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"), method = "render")
//    private static boolean onRender(LivingEntity instance, StatusEffect effect) {
//        if (ConfigManager.getConfig().isEnabled()) {
//            if (ConfigManager.getConfig().removeBlindnessEffect) {
//                return false;
//            }
//        }
//        return instance.hasStatusEffect(effect);
//    }
//
//    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"), method = "applyFog")
//    private static boolean onApplyFog(LivingEntity instance, StatusEffect effect) {
//        if (ConfigManager.getConfig().isEnabled()) {
//            if (ConfigManager.getConfig().removeBlindnessEffect) {
//                return false;
//            }
//        }
//        return instance.hasStatusEffect(effect);
//    }
}
