package com.thatdudo.omniscience.util;

public class RenderContext {

    private static final ThreadLocal<Boolean> OMNISCIENCE_RENDERING =
            ThreadLocal.withInitial(() -> false);

    public static void setOmniscienceRendering(boolean value) {
        OMNISCIENCE_RENDERING.set(value);
    }

    public static boolean isOmniscienceRendering() {
        return OMNISCIENCE_RENDERING.get();
    }

    public static void clear() {
        OMNISCIENCE_RENDERING.remove();
    }
}