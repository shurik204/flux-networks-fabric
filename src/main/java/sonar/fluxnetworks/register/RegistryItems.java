package sonar.fluxnetworks.register;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import sonar.fluxnetworks.FluxNetworks;
import sonar.fluxnetworks.common.item.*;

public class RegistryItems {
    private static final ResourceLocation FLUX_DUST_KEY = FluxNetworks.location("flux_dust");
    private static final ResourceLocation FLUX_CORE_KEY = FluxNetworks.location("flux_core");
    private static final ResourceLocation FLUX_CONFIGURATOR_KEY = FluxNetworks.location("flux_configurator");
    private static final ResourceLocation ADMIN_CONFIGURATOR_KEY = FluxNetworks.location("admin_configurator");
    private static final Item.Properties ITEM_PROPS = new Item.Properties().fireResistant();
    private static final Item.Properties TOOL_PROPS = new Item.Properties().fireResistant().stacksTo(1);

    public static final Item FLUX_BLOCK = register(RegistryBlocks.FLUX_BLOCK_KEY, new BlockItem(RegistryBlocks.FLUX_BLOCK, ITEM_PROPS));
    public static final Item FLUX_PLUG = register(RegistryBlocks.FLUX_PLUG_KEY, new FluxDeviceItem(RegistryBlocks.FLUX_PLUG, ITEM_PROPS));
    public static final Item FLUX_POINT = register(RegistryBlocks.FLUX_POINT_KEY, new FluxDeviceItem(RegistryBlocks.FLUX_POINT, ITEM_PROPS));
    public static final Item FLUX_CONTROLLER = register(RegistryBlocks.FLUX_CONTROLLER_KEY, new FluxDeviceItem(RegistryBlocks.FLUX_CONTROLLER, ITEM_PROPS));
    public static final Item BASIC_FLUX_STORAGE = register(RegistryBlocks.BASIC_FLUX_STORAGE_KEY, new FluxStorageItem(RegistryBlocks.BASIC_FLUX_STORAGE, ITEM_PROPS));
    public static final Item HERCULEAN_FLUX_STORAGE = register(RegistryBlocks.HERCULEAN_FLUX_STORAGE_KEY, new FluxStorageItem(RegistryBlocks.HERCULEAN_FLUX_STORAGE, ITEM_PROPS));
    public static final Item GARGANTUAN_FLUX_STORAGE = register(RegistryBlocks.GARGANTUAN_FLUX_STORAGE_KEY, new FluxStorageItem(RegistryBlocks.GARGANTUAN_FLUX_STORAGE, ITEM_PROPS));
    public static final Item FLUX_DUST = register(FLUX_DUST_KEY, new FluxDustItem(ITEM_PROPS));
    public static final Item FLUX_CORE = register(FLUX_CORE_KEY, new Item(ITEM_PROPS));
    public static final Item FLUX_CONFIGURATOR = register(FLUX_CONFIGURATOR_KEY, new ItemFluxConfigurator(TOOL_PROPS));
    public static final Item ADMIN_CONFIGURATOR = register(ADMIN_CONFIGURATOR_KEY, new ItemAdminConfigurator(TOOL_PROPS));

    public static Item register(ResourceLocation key, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, key,item);
    }

    public static void init() {}
}