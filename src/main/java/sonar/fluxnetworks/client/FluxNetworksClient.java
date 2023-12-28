package sonar.fluxnetworks.client;

import net.fabricmc.api.ClientModInitializer;
import sonar.fluxnetworks.register.ClientRegistration;

public class FluxNetworksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientRegistration.init();
    }
}