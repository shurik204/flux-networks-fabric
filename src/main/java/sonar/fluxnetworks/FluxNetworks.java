package sonar.fluxnetworks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sonar.fluxnetworks.register.Registration;

import javax.annotation.Nonnull;

public class FluxNetworks implements ModInitializer {
    public static final String MODID = "fluxnetworks";
    public static final String NAME = "Flux Networks";
    public static final String NAME_CPT = "FluxNetworks";

    public static final Logger LOGGER = LogManager.getLogger(NAME_CPT);

    private static MinecraftServer sServer;
    private static boolean sTrinketsLoaded;
    private static boolean sModernUILoaded;

    public void onInitialize() {
        sTrinketsLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        sModernUILoaded = FabricLoader.getInstance().isModLoaded("modernui");

        FluxConfig.init();
        Registration.init();
    }

    public static boolean isTrinketsLoaded() {
        return sTrinketsLoaded;
    }

    public static boolean isModernUILoaded() {
        return sModernUILoaded;
    }

    public static void setServer(MinecraftServer server) {
        sServer = server;
    }

    public static MinecraftServer getServer() {
        return sServer;
    }

    @Nonnull
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}
