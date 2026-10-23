package io.github.n4th4not.gravity.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/** Optional-mod loading is contained here, on both the client and dedicated server. */
public final class GravityPlatform implements ModInitializer {
    private static GravityAdapter adapter = new GravityAdapter();
    private static boolean initialized;
    public static synchronized void init() {
        if (initialized) return;
        if (FabricLoader.getInstance().isModLoaded("gravity_changer")) adapter = new GravityChangerAdapter();
        initialized = true;
    }
    public static GravityAdapter adapter() { return adapter; }
    @Override
    public void onInitialize() { init(); }
}
