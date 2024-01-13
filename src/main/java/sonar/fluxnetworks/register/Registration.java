package sonar.fluxnetworks.register;

import me.shurik.simplechunkmanager.api.BlockChunkLoader;
import me.shurik.simplechunkmanager.api.ChunkLoader;
import me.shurik.simplechunkmanager.api.SimpleChunkManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import sonar.fluxnetworks.FluxConfig;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.util.EnergyUtils;

public class Registration {
    public static void init() {
        RegistryBlocks.init();
        RegistryItems.init();
        RegistryBlockEntityTypes.init();
        RegistryMenuTypes.init();
        RegistryRecipes.init();
        RegistrySounds.init();
        RegistryCreativeModeTabs.init();
        EventHandler.init();
        // Moved from FMLCommonSetupEvent
        EnergyUtils.register();
        ServerPlayNetworking.registerGlobalReceiver(Channel.CHANNEL_NAME, (server, player, handler, buf, responseSender) ->
                Messages.msg(buf.readShort(), buf, () -> player));
        Channel.init();

        SimpleChunkManager.VALIDATION.register((level, manager) -> {
            if (!FluxConfig.enableChunkLoading) {
                if (manager.removeModChunkLoaderBlocks(FluxNetworks.MODID)) {
                    // Only display this message if we actually removed any chunk loader
                    FluxNetworks.LOGGER.info("Removed all chunk loaders from world '" + level + "' because chunk loading is disabled");
                }
            } else {
                int chunks = 0;
                for (ChunkLoader<?> chunkLoader : manager.getModChunkLoaders(FluxNetworks.MODID)) {
                    if (chunkLoader instanceof BlockChunkLoader blockChunkLoader &&
                            level.getBlockEntity(blockChunkLoader.getPos()) instanceof TileFluxDevice e) {
                        e.setForcedLoading(true);
                        chunks++;
                    } else {
                        manager.removeChunkLoader(chunkLoader);
                    }
                }
                FluxNetworks.LOGGER.info("Loaded {} chunks by {} flux devices in {}",
                        chunks, manager.getModChunkLoaders(FluxNetworks.MODID).size(), level.dimension().location());
            }
        });
    }

//        TODO: replace with carryon:block_blacklist tag
//        if (ModList.get().isLoaded("carryon")) {
//            InterModComms.sendTo("carryon", "blacklistBlock", () -> FluxNetworks.MODID + ":*");
//        }
//        TODO: replace with Jade integration
//        if (ModList.get().isLoaded("theoneprobe")) {
//            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPIntegration::new);
//        }

//    TODO: setup fabric datagen
//    @SubscribeEvent
//    public static void gatherData(@Nonnull GatherDataEvent event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput packOutput = generator.getPackOutput();
//        if (event.includeServer()) {
//            generator.addProvider(true, new FluxLootTableProvider(packOutput));
//            generator.addProvider(true, new FluxBlockTagsProvider(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
//        }
//    }
}
