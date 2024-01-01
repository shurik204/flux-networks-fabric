package sonar.fluxnetworks.register;

import me.shurik.simplechunkmanager.api.BlockChunkLoader;
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
                manager.getChunkLoaders(FluxNetworks.MODID, level).forEach(chunkLoader -> manager.getAllChunkLoaders(level).remove(chunkLoader));
                FluxNetworks.LOGGER.info("Removed all chunk loaders because chunk loading is disabled");
            } else {
                int chunks = 0;
                for (BlockChunkLoader chunkLoader : manager.getChunkLoaders(FluxNetworks.MODID, level)) {
                    if (level.getBlockEntity(chunkLoader.getPos()) instanceof TileFluxDevice e) {
                        e.setForcedLoading(true);
                        chunks++;
                    } else {
                        manager.getAllChunkLoaders(level).remove(chunkLoader);
                    }
                }
                FluxNetworks.LOGGER.info("Loaded {} chunks by {} flux devices in {}",
                        chunks, manager.getChunkLoaders(FluxNetworks.MODID, level).size(), level.dimension().location());
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
