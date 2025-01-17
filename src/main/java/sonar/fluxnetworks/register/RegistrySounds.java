package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import sonar.fluxnetworks.FluxNetworks;

public class RegistrySounds {
    public static final ResourceLocation BUTTON_CLICK_KEY = FluxNetworks.location("button");

    public static final SoundEvent BUTTON_CLICK = register(BUTTON_CLICK_KEY);

    private static SoundEvent register(ResourceLocation name) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, name, SoundEvent.createVariableRangeEvent(name));
    }

    public static void init() {}
}
