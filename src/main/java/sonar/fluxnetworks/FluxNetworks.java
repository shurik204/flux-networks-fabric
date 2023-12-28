package sonar.fluxnetworks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sonar.fluxnetworks.register.EventHandler;
import sonar.fluxnetworks.register.Registration;

import javax.annotation.Nonnull;

public class FluxNetworks implements ModInitializer {
    public static final String MODID = "fluxnetworks";
    public static final String NAME = "Flux Networks";
    public static final String NAME_CPT = "FluxNetworks";

    public static final Logger LOGGER = LogManager.getLogger(NAME_CPT);

    private static MinecraftServer sServer;
    private static boolean sCuriosLoaded;
    private static boolean sModernUILoaded;

    public void onInitialize() {
        sCuriosLoaded = FabricLoader.getInstance().isModLoaded("trinkets");
        sModernUILoaded = FabricLoader.getInstance().isModLoaded("modernui");

        FluxConfig.init();
        Registration.init();
    }

    public static boolean isCuriosLoaded() {
        return sCuriosLoaded;
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
