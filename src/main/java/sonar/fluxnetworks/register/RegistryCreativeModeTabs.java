package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.api.FluxConstants;

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
                output.accept(RegistryItems.BOTTOMLESS_FLUX_STORAGE);

                // Filled creative storage
                ItemStack stack = new ItemStack(RegistryItems.BOTTOMLESS_FLUX_STORAGE);

                CompoundTag fluxData = stack.getOrCreateTagElement(FluxConstants.TAG_FLUX_DATA);
                CompoundTag display = stack.getOrCreateTagElement("display");
                display.putString("Name", "{\"translate\":\"block.fluxnetworks.bottomless_flux_storage.filled\",\"italic\":false}");
                stack.getOrCreateTag().putBoolean(FluxConstants.FLUX_COLOR, false);
                fluxData.putInt(FluxConstants.CLIENT_COLOR, 0xb148d2);
                fluxData.putLong(FluxConstants.ENERGY, Long.MAX_VALUE);

                output.accept(stack);

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