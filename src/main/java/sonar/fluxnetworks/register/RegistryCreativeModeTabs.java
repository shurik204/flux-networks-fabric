package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.FluxNetworks;

public class RegistryCreativeModeTabs {
    public static final ResourceLocation CREATIVE_MODE_TAB_KEY = FluxNetworks.location("tab");

    public static final CreativeModeTab CREATIVE_MODE_TAB = register(CREATIVE_MODE_TAB_KEY, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup." + FluxNetworks.MODID))
            .icon(() -> new ItemStack(RegistryItems.FLUX_CORE))
            .displayItems((parameters, output) -> {
                output.accept(RegistryItems.FLUX_BLOCK);
                output.accept(RegistryItems.FLUX_PLUG);
                output.accept(RegistryItems.FLUX_POINT);
                output.accept(RegistryItems.FLUX_CONTROLLER);
                output.accept(RegistryItems.BASIC_FLUX_STORAGE);
                output.accept(RegistryItems.HERCULEAN_FLUX_STORAGE);
                output.accept(RegistryItems.GARGANTUAN_FLUX_STORAGE);
                output.accept(RegistryItems.FLUX_DUST);
                output.accept(RegistryItems.FLUX_CORE);
                output.accept(RegistryItems.FLUX_CONFIGURATOR);
                output.accept(RegistryItems.ADMIN_CONFIGURATOR);
            }).build());

    private static CreativeModeTab register(ResourceLocation key, CreativeModeTab creativeModeTab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, creativeModeTab);
    }

    public static void init() {}
}