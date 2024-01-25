package sonar.fluxnetworks.register;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sonar.fluxnetworks.FluxNetworks;

public class RegistryCreativeModeTabs {
    public static final ResourceLocation CREATIVE_MODE_TAB_KEY = FluxNetworks.location("tab");

    public static final CreativeModeTab CREATIVE_MODE_TAB = FabricItemGroupBuilder.create(CREATIVE_MODE_TAB_KEY)
            .icon(() -> new ItemStack(RegistryItems.FLUX_CORE))
            .appendItems((stacks) -> {
                stacks.add(new ItemStack(RegistryItems.FLUX_BLOCK));
                stacks.add(new ItemStack(RegistryItems.FLUX_PLUG));
                stacks.add(new ItemStack(RegistryItems.FLUX_POINT));
                stacks.add(new ItemStack(RegistryItems.FLUX_CONTROLLER));
                stacks.add(new ItemStack(RegistryItems.BASIC_FLUX_STORAGE));
                stacks.add(new ItemStack(RegistryItems.HERCULEAN_FLUX_STORAGE));
                stacks.add(new ItemStack(RegistryItems.GARGANTUAN_FLUX_STORAGE));
                stacks.add(new ItemStack(RegistryItems.FLUX_DUST));
                stacks.add(new ItemStack(RegistryItems.FLUX_CORE));
                stacks.add(new ItemStack(RegistryItems.FLUX_CONFIGURATOR));
                stacks.add(new ItemStack(RegistryItems.ADMIN_CONFIGURATOR));
            })
            .build();
    
    public static void init() {}
}