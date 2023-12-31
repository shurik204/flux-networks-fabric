package sonar.fluxnetworks.register;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
    }

//    TODO: FIX CHUNK LOADING
//    Plan A: port ForgeChunkManager to Fabric
//    Plan B: reinvent the wheel
//    public static void setup(FMLCommonSetupEvent event) {
//        Channel.sChannel = new FMLChannel();
//        event.enqueueWork(() -> ForgeChunkManager.setForcedChunkLoadingCallback(FluxNetworks.MODID, (level, helper) -> {
//            if (!FluxConfig.enableChunkLoading) {
//                helper.getBlockTickets().keySet().forEach(helper::removeAllTickets);
//                FluxNetworks.LOGGER.info("Removed all chunk loaders because chunk loading is disabled");
//            } else {
//                int chunks = 0;
//                for (var entry : helper.getBlockTickets().entrySet()) {
//                    // this also loads the chunk
//                    if (level.getBlockEntity(entry.getKey()) instanceof TileFluxDevice e) {
//                        e.setForcedLoading(true);
//                        var pair = entry.getValue();
//                        int count = 0;
//                        count += pair.getFirst().size();
//                        count += pair.getSecond().size();
//                        if (count != 1) {
//                            FluxNetworks.LOGGER.warn("{} in {} didn't load just one chunk {}",
//                                    entry.getValue(), level.dimension().location(), pair);
//                        }
//                        chunks += count;
//                    } else {
//                        helper.removeAllTickets(entry.getKey());
//                    }
//                }
//                FluxNetworks.LOGGER.info("Load {} chunks by {} flux devices in {}",
//                        chunks, helper.getBlockTickets().size(), level.dimension().location());
//            }
//        }));
//    }

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
