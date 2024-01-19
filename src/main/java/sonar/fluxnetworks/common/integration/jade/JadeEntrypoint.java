package sonar.fluxnetworks.common.integration.jade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.block.FluxDeviceBlock;
import sonar.fluxnetworks.common.device.TileFluxDevice;
import sonar.fluxnetworks.common.device.TileFluxStorage;

@WailaPlugin
public class JadeEntrypoint implements IWailaPlugin {
    public static final ResourceLocation DEVICE_DATA_PROVIDER_ID = FluxNetworks.location("device_data_provider");
    public static final ResourceLocation ENERGY_STORAGE_PROVIDER_ID = FluxNetworks.location("energy_storage_provider");

    @Override
    @Environment(EnvType.CLIENT)
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeFluxDeviceDataProvider.INSTANCE, FluxDeviceBlock.class);

        registration.registerEnergyStorageClient(JadeEnergyStorageProvider.INSTANCE);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeFluxDeviceDataProvider.INSTANCE, TileFluxDevice.class);
        registration.registerEnergyStorage(JadeEnergyStorageProvider.INSTANCE, TileFluxStorage.class);
    }
}