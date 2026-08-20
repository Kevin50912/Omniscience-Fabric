package com.thatdudo.omniscience.mixin;

import com.thatdudo.omniscience.access.LivingEntityRenderStateAccess;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin
        implements LivingEntityRenderStateAccess {

    private boolean omniscience$wasInvisible;

    @Override
    public boolean omniscience$wasInvisible() {
        return omniscience$wasInvisible;
    }

    @Override
    public void omniscience$setWasInvisible(boolean value) {
        omniscience$wasInvisible = value;
    }
}