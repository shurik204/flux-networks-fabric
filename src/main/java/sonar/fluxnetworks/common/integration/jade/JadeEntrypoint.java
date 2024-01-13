package sonar.fluxnetworks.common.integration.jade;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import sonar.fluxnetworks.common.block.FluxDeviceBlock;

@WailaPlugin
public class JadeEntrypoint implements IWailaPlugin {
    public static final ResourceLocation ID = new ResourceLocation("fluxnetworks", "jade");

    @Override
    @Environment(EnvType.CLIENT)
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeFluxDeviceDataProvider.INSTANCE, FluxDeviceBlock.class);
    }
}